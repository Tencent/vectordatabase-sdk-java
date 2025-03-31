/*
 * Copyright (C) 2023 Tencent Cloud.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the vectordb-sdk-java), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tencent.tcvectordb.service;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.*;
import com.tencent.tcvectordb.model.Collection;
import com.tencent.tcvectordb.model.param.collection.*;
import com.tencent.tcvectordb.model.param.collectionView.CreateCollectionViewParam;
import com.tencent.tcvectordb.model.param.collectionView.LoadAndSplitTextParam;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.*;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.model.param.enums.EmbeddingModelEnum;
import com.tencent.tcvectordb.model.param.user.*;
import com.tencent.tcvectordb.rpc.Interceptor.AuthorityInterceptor;
import com.tencent.tcvectordb.rpc.Interceptor.BackendServiceInterceptor;
import com.tencent.tcvectordb.rpc.proto.Olama;
import com.tencent.tcvectordb.rpc.proto.SearchEngineGrpc;
import com.tencent.tcvectordb.service.param.*;
import io.grpc.*;
import io.grpc.okhttp.OkHttpChannelBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GrpcStub extends HttpStub{

    private ManagedChannel channel;
    private SearchEngineGrpc.SearchEngineBlockingStub blockingStub;

    private final int maxSendMessageSize = 100*1024*1024;
    private final int maxReceiveMessageSize = 100*1024*1024;
    private String authorization;
    private int timeout = 10;
    private static final Logger logger = LoggerFactory.getLogger(GrpcStub.class.getName());
    private ConnectParam connectParam;
    public GrpcStub(ConnectParam param){
        super();
        connectParam = param;
        this.authorization = String.format("Bearer account=%s&api_key=%s",param.getUsername(), param.getKey());

        this.channel = OkHttpChannelBuilder.forTarget(this.getAddress(param.getUrl())).
                intercept(new AuthorityInterceptor(this.authorization)).
                flowControlWindow(maxReceiveMessageSize).
                maxInboundMessageSize(maxReceiveMessageSize).
                usePlaintext().build();
        if (param.getTimeout() > 0) {
            this.timeout = param.getTimeout();
        }
        this.blockingStub = SearchEngineGrpc.newBlockingStub(this.channel);
        this.blockingStub.withMaxInboundMessageSize(maxReceiveMessageSize);
        this.blockingStub.withMaxOutboundMessageSize(maxSendMessageSize);

    }

    private String getAddress(String url){
        URL _url = null;
        try {
            _url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        if (_url.getPort()<=0){
            url = url + ":80";
        }
        return url.replaceFirst("http://", "").replaceFirst("https://", "");
    }

    @Override
    public synchronized void close() {
        super.close();
        if (this.channel!=null){
            this.channel.shutdown();
        }
    }


    @Override
    public void createDatabase(Database database) {
        Olama.DatabaseRequest.Builder databaseRequest = Olama.DatabaseRequest.newBuilder();
        if (database.getDatabaseName()!=null){
            databaseRequest.setDatabase(database.getDatabaseName());
        }
        logQuery(ApiPath.DB_CREATE, databaseRequest);
        Olama.DatabaseResponse response = this.blockingStub.withInterceptors(new BackendServiceInterceptor(false))
                .withDeadlineAfter(this.timeout, TimeUnit.SECONDS).
                createDatabase(databaseRequest.build());
        logResponse(ApiPath.DB_CREATE, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer create Database error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
    }

    @Override
    public void dropDatabase(Database database) {
        Olama.DatabaseRequest.Builder databaseRequest = Olama.DatabaseRequest.newBuilder();
        if (database.getDatabaseName()!=null){
            databaseRequest.setDatabase(database.getDatabaseName());
        }
        logQuery(ApiPath.DB_DROP, databaseRequest.build());
        Olama.DatabaseResponse response =  this.blockingStub.withInterceptors(new BackendServiceInterceptor(false)).
                withDeadlineAfter(this.timeout, TimeUnit.SECONDS).dropDatabase(databaseRequest.build());
        logResponse(ApiPath.DB_DROP, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer drop Database error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
    }

    @Override
    public AffectRes createAIDatabase(AIDatabase aiDatabase) {
        super.initHttpStub(this.connectParam);
        return super.createAIDatabase(aiDatabase);
    }

    @Override
    public DataBaseTypeRes describeDatabase(Database database) {
        Olama.DescribeDatabaseRequest.Builder databaseRequest = Olama.DescribeDatabaseRequest.newBuilder();
        if (database.getDatabaseName()!=null){
            databaseRequest.setDatabase(database.getDatabaseName());
        }
        logQuery(ApiPath.DB_DESCRIBE, databaseRequest.build());
        Olama.DescribeDatabaseResponse response = this.blockingStub.withInterceptors(new BackendServiceInterceptor(false))
                .withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                .describeDatabase(databaseRequest.build());
        logResponse(ApiPath.DB_DESCRIBE, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer describeDatabase error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }

        Date date = new Date(response.getDatabase().getCreateTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DataBaseType dataBaseType = new DataBaseType();
        dataBaseType.setCreateTime(sdf.format(date));
        dataBaseType.setDbType(response.getDatabase().getDbType().toString());
        return new DataBaseTypeRes(response.getCode(), response.getMsg(), "", dataBaseType);
    }

    @Override
    public AffectRes dropAIDatabase(AIDatabase aiDatabase) {
        super.initHttpStub(this.connectParam);
        return super.dropAIDatabase(aiDatabase);
    }

    @Override
    public List<String> listDatabases() {

        Olama.DatabaseRequest request = Olama.DatabaseRequest.newBuilder().build();
        logQuery(ApiPath.DB_LIST, request);
        Olama.DatabaseResponse response = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).listDatabases(request);
        logResponse(ApiPath.DB_LIST, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer list Database error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return response.getDatabasesList();
    }

    @Override
    public Map<String, DataBaseType> listDatabaseInfos() {

        Olama.DatabaseRequest request = Olama.DatabaseRequest.newBuilder().build();
        logQuery(ApiPath.DB_LIST, request);
        Olama.DatabaseResponse response =  this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).listDatabases(request);
        logResponse(ApiPath.DB_LIST, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer list Database error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        Map<String, DataBaseType> dataBaseTypeMap = new HashMap<>();
        response.getInfoMap().entrySet().forEach(entry->
        {
            DataBaseType dataBaseType = new DataBaseType();
            dataBaseType.setDbType(entry.getKey());
            Date date = new Date(entry.getValue().getCreateTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dataBaseType.setCreateTime(sdf.format(date));
            dataBaseTypeMap.put(entry.getKey(), dataBaseType);
        });
        return dataBaseTypeMap;
    }

    @Override
    public void createCollection(CreateCollectionParam params) {
        Olama.CreateCollectionRequest.Builder requestOrBuilder = Olama.CreateCollectionRequest.newBuilder();
        if (params.getDatabase()!=null){
            requestOrBuilder.setDatabase(params.getDatabase());
        }
        if (params.getCollection()!=null){
            requestOrBuilder.setCollection(params.getCollection());
        }

        requestOrBuilder.setShardNum(params.getShardNum())
                .setReplicaNum(params.getReplicaNum())
                .setDescription(params.getDescription());

        if (params.getAlias()!=null){
            requestOrBuilder.addAllAliasList(params.getAlias());
        }
        if (params.getEmbedding()!=null){
            Olama.EmbeddingParams.Builder embeddingBuilder = Olama.EmbeddingParams.newBuilder()
                    .setField(params.getEmbedding().getField())
                    .setVectorField(params.getEmbedding().getVectorField());
            if (params.getEmbedding().getModel() != null) {
                embeddingBuilder.setModelName(params.getEmbedding().getModel().getModelName());
            }else{
                embeddingBuilder.setModelName(params.getEmbedding().getModelName());
            }
            requestOrBuilder.setEmbeddingParams(embeddingBuilder.build());
        }
        if (params.getTtlConfig()!=null){
            requestOrBuilder.setTtlConfig(Olama.TTLConfig.newBuilder()
                            .setEnable(params.getTtlConfig().isEnable())
                            .setTimeField(params.getTtlConfig().getTimeField())
                    .build());
        }
        if (params.getFilterIndexConfig()!=null){
            Olama.FilterIndexConfig.Builder filterBuilder = Olama.FilterIndexConfig.newBuilder().
                    setFilterAll(params.getFilterIndexConfig().isFilterAll());
            if (params.getFilterIndexConfig().getFieldsWithoutIndex()!=null){
                filterBuilder.addAllFieldsWithoutIndex(params.getFilterIndexConfig().getFieldsWithoutIndex());
            }
            if(params.getFilterIndexConfig().getMaxStrLen()!=null){
                filterBuilder.setMaxStrLen(params.getFilterIndexConfig().getMaxStrLen());
            }
            requestOrBuilder.setFilterIndexConfig(filterBuilder.build());

        }
        if (params.getIndexes() != null && !params.getIndexes().isEmpty()){
            for (IndexField index : params.getIndexes()) {
                Olama.IndexColumn.Builder indexBuilder = getRpcIndexBuilder(index);
                requestOrBuilder.putIndexes(index.getFieldName(), indexBuilder.build());
            }

        }
        logQuery(ApiPath.COL_CREATE, requestOrBuilder);

        Olama.CreateCollectionResponse response =  this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                .createCollection(requestOrBuilder.build());
        logResponse(ApiPath.COL_CREATE, response);
        if(response==null){
            throw new VectorDBException("VectorDBServer error: CreateCollectionResponse not response");
        }
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
    }

    private static Olama.IndexColumn.Builder getRpcIndexBuilder(IndexField index) {
        Olama.IndexColumn.Builder indexBuilder = Olama.IndexColumn.newBuilder()
                .setFieldName(index.getFieldName())
                .setFieldType(index.getFieldType().getValue());
        if (index.getIndexType()!=null){
            indexBuilder.setIndexType(index.getIndexType().getValue());
        }
        if(index.isVectorField()){
            if(index.getDimension()!=null){
                indexBuilder.setDimension(index.getDimension());
            }
            if (index.getMetricType()!=null) {
                indexBuilder.setMetricType(index.getMetricType().getValue());
            }
            if(index.getParams()!=null){
                if(index.getParams() instanceof HNSWParams) {
                    HNSWParams hnswParams = (HNSWParams) index.getParams();
                    indexBuilder.setParams(Olama.IndexParams.newBuilder()
                            .setM(hnswParams.getM())
                            .setEfConstruction(hnswParams.getEfConstruction()).build());
                }else if(index.getParams() instanceof IVFFLATParams) {
                    IVFFLATParams ivfflatParams = (IVFFLATParams) index.getParams();
                    indexBuilder.setParams(Olama.IndexParams.newBuilder()
                            .setNlist(ivfflatParams.getNList()).build());
                }else if (index.getParams() instanceof IVFPQParams) {
                    IVFPQParams ivfpqParams = (IVFPQParams) index.getParams();
                    indexBuilder.setParams(Olama.IndexParams.newBuilder()
                            .setNlist(ivfpqParams.getNList()).setM(ivfpqParams.getM()).build());
                }else if(index.getParams() instanceof  IVFSQ8Params) {
                    IVFSQ8Params ivfsq8Params = (IVFSQ8Params) index.getParams();
                    indexBuilder.setParams(Olama.IndexParams.newBuilder()
                            .setNlist(ivfsq8Params.getNList()).build());
                }
            }
        }
        if(index.isSparseVectorField()){
            indexBuilder.setMetricType(index.getMetricType().getValue());
        }
        if(index.getFieldType()==FieldType.Array){
            indexBuilder.setFieldElementType(FieldElementType.String.getValue());
        }
        if(index.getAutoId()!=null){
            indexBuilder.setAutoId(index.getAutoId());
        }
        return indexBuilder;
    }

    @Override
    public void createCollectionView(CreateCollectionViewParam params) {
        super.initHttpStub(this.connectParam);
        super.createCollectionView(params);
    }

    @Override
    public List<Collection> listCollections(String databaseName) {

        Olama.ListCollectionsRequest.Builder requestBuilder = Olama.ListCollectionsRequest.newBuilder().setTransfer(false);
        if (databaseName!=null){
            requestBuilder.setDatabase(databaseName);
        }
        logQuery(ApiPath.COL_LIST, requestBuilder.build());
        Olama.ListCollectionsResponse response = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                .listCollections(requestBuilder.build());
        logResponse(ApiPath.COL_LIST, response);
        List<Collection> collections = new ArrayList<>();
        response.getCollectionsList().forEach(collection -> {
            Collection collectionRpc = convertRpcToCollection(collection);
            collectionRpc.setStub(this);
            collections.add(collectionRpc);
        });
        return collections;
    }

    @Override
    public Collection describeCollection(String databaseName, String collectionName) {
        Olama.DescribeCollectionRequest.Builder requestBuilder = Olama.DescribeCollectionRequest.newBuilder();
        if (databaseName!=null){
            requestBuilder.setDatabase(databaseName);
        }
        if (collectionName!=null){
            requestBuilder.setCollection(collectionName);
        }
        logQuery(ApiPath.COL_DESCRIBE, requestBuilder.build());
        Olama.DescribeCollectionResponse describeCollectionResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                .describeCollection(requestBuilder.build());
        logResponse(ApiPath.COL_DESCRIBE, describeCollectionResponse);
        if(describeCollectionResponse==null){
            throw new VectorDBException("VectorDBServer error: describeCollection not response");
        }
        if (describeCollectionResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: not Successful, body code=%s, message=%s",
                    describeCollectionResponse.getCode(), describeCollectionResponse.getMsg()));
        }
        return convertRpcToCollection(describeCollectionResponse.getCollection());
    }

    @Override
    public AffectRes truncateCollection(String databaseName, String collectionName, DataBaseTypeEnum dbType) {
       Olama.TruncateCollectionRequest.Builder requestBuilder = Olama.TruncateCollectionRequest.newBuilder();
        if (databaseName!=null){
            requestBuilder.setDatabase(databaseName);
        }
        if (collectionName!=null){
            requestBuilder.setCollection(collectionName);
        }
        logQuery(ApiPath.COL_FLUSH, requestBuilder.build());
        Olama.TruncateCollectionResponse truncateCollectionResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).
                truncateCollection(requestBuilder.build());
        logResponse(ApiPath.COL_FLUSH, truncateCollectionResponse);
        if (truncateCollectionResponse==null){
            throw new VectorDBException("VectorDBServer error: truncateCollectionResponse not response");
        }
        if (truncateCollectionResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: not Successful, body code=%s, message=%s",
                    truncateCollectionResponse.getCode(), truncateCollectionResponse.getMsg()));
        }
        return new AffectRes(truncateCollectionResponse.getCode(), truncateCollectionResponse.getMsg(),
                "", truncateCollectionResponse.getAffectedCount());
    }

    @Override
    public AffectRes truncateCollectionView(String databaseName, String collectionName, DataBaseTypeEnum dbType) {
        super.initHttpStub(this.connectParam);
        return super.truncateCollectionView(databaseName, collectionName, dbType);
    }

    @Override
    public void dropCollection(String databaseName, String collectionName) {
        Olama.DropCollectionRequest.Builder requestBuilder = Olama.DropCollectionRequest.newBuilder();
        if (databaseName!=null){
            requestBuilder.setDatabase(databaseName);
        }
        if (collectionName!=null){
            requestBuilder.setCollection(collectionName);
        }
        logQuery(ApiPath.COL_DROP, requestBuilder.build());
        Olama.DropCollectionResponse dropCollectionResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).
                dropCollection(requestBuilder.build());
        logResponse(ApiPath.COL_DROP, dropCollectionResponse);
        if(dropCollectionResponse==null){
            throw new VectorDBException("VectorDBServer error: dropCollection not response");
        }
        if (dropCollectionResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: dropCollection not Success, body code=%s, message=%s",
                    dropCollectionResponse.getCode(), dropCollectionResponse.getMsg()));
        }
    }

    @Override
    public AffectRes setAlias(String databaseName, String collectionName, String aliasName) {
        Olama.AddAliasRequest.Builder requestBuilder = Olama.AddAliasRequest.newBuilder();
        if (databaseName!=null){
            requestBuilder.setDatabase(databaseName);
        }
        if (collectionName!=null){
            requestBuilder.setCollection(collectionName);
        }
        if (aliasName!=null){
            requestBuilder.setAlias(aliasName);
        }
        logQuery(ApiPath.AI_ALIAS_SET, requestBuilder.build());
        Olama.UpdateAliasResponse setAliasResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).
                setAlias(requestBuilder.build());
        logResponse(ApiPath.AI_ALIAS_SET, setAliasResponse);
        if(setAliasResponse==null){
            throw new VectorDBException("VectorDBServer error: dropCollection not response");
        }
        if (setAliasResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: setAlias not Success, body code=%s, message=%s",
                    setAliasResponse.getCode(), setAliasResponse.getMsg()));
        }
        return new AffectRes(setAliasResponse.getCode(), setAliasResponse.getMsg(),
                "", setAliasResponse.getAffectedCount());
    }

    @Override
    public AffectRes deleteAlias(String databaseName, String aliasName) {

        Olama.RemoveAliasRequest.Builder requestBuilder = Olama.RemoveAliasRequest.newBuilder();
        if (databaseName!=null){
            requestBuilder.setDatabase(databaseName);
        }
        if (aliasName!=null){
            requestBuilder.setAlias(aliasName);
        }
        Olama.RemoveAliasRequest request = requestBuilder.build();
        logQuery(ApiPath.AI_ALIAS_DELETE, request);
        Olama.UpdateAliasResponse deleteResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                .deleteAlias(request);
        logQuery(ApiPath.AI_ALIAS_DELETE, request);
        if(deleteResponse==null){
            throw new VectorDBException("VectorDBServer error: dropCollection not response");
        }
        if (deleteResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: deleteAlias not Success, body code=%s, message=%s",
                    deleteResponse.getCode(), deleteResponse.getMsg()));
        }
        return new AffectRes(deleteResponse.getCode(), deleteResponse.getMsg(),
                "", deleteResponse.getAffectedCount());
    }

    @Override
    public AffectRes upsertDocument(InsertParamInner param, boolean ai) {
        Olama.UpsertRequest.Builder builder = Olama.UpsertRequest.newBuilder();
        if (param.getDatabase()!=null){
            builder.setDatabase(param.getDatabase());
        }
        if (param.getCollection()!=null){
            builder.setCollection(param.getCollection());
        }
        if (param.getBuildIndex()==null){
            builder.setBuildIndex(true);
        }else {
            builder.setBuildIndex(param.getBuildIndex());
        }

        if (param.getDocuments()!=null && !param.getDocuments().isEmpty()){
            for (Object document : param.getDocuments()) {
                if (document instanceof Document){
                    Olama.Document doc= convertDocument2OlamaDoc((Document) document);
                    builder.addDocuments(doc);
                }else if (document instanceof JSONObject){
                    Olama.Document doc= convertDocumentJSON2OlamaDoc((JSONObject) document);
                    builder.addDocuments(doc);

                }else {
                    throw new VectorDBException("upsert failed, because of incorrect documents type, " +
                            "which must be []Document or []JSONObject");
                }
            }
        }
        logQuery(ApiPath.DOC_UPSERT, builder);
        Olama.UpsertResponse response = this.blockingStub.withInterceptors(new BackendServiceInterceptor(ai))
                .withDeadlineAfter(this.timeout, TimeUnit.SECONDS).upsert(builder.build());
        logResponse(ApiPath.DOC_UPSERT, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer upsert data error: not Successful, code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return new AffectRes(response.getCode(), response.getMsg(), response.getWarning(), response.getAffectedCount());
    }

    @Override
    public List<Document> queryDocument(QueryParamInner param, boolean ai) {
        Olama.QueryRequest.Builder queryBuilder = Olama.QueryRequest.newBuilder()
                .setReadConsistency(param.getReadConsistency().getReadConsistency());
        if (param.getDatabase()!=null){
            queryBuilder.setDatabase(param.getDatabase());
        }
        if (param.getCollection()!=null){
            queryBuilder.setCollection(param.getCollection());
        }
        QueryParam queryParam = param.getQuery();
        if (queryParam==null){
            throw new VectorDBException("VectorDBServer error: query param is null");
        }
        Olama.QueryCond.Builder queryCondBuilder = Olama.QueryCond.newBuilder().setRetrieveVector(queryParam.isRetrieveVector())
                .setLimit(queryParam.getLimit()).setOffset(queryParam.getOffset());
        if(queryParam.getDocumentIds()!=null){
            queryCondBuilder.addAllDocumentIds(queryParam.getDocumentIds());
        }
        if(queryParam.getOutputFields()!=null){
            queryCondBuilder.addAllOutputFields(queryParam.getOutputFields());
        }
        if (queryParam.getFilter()!=null){
            queryCondBuilder.setFilter(queryParam.getFilter());
        }
        if (queryParam.getSort()!=null && queryParam.getSort().size()>0){
            queryCondBuilder.addAllSort(queryParam.getSort().stream().map(sort -> {
                return Olama.OrderRule.newBuilder().setFieldName(sort.getFieldName()).setDesc(sort.getDirection().equals("desc")).build();
            }).collect(Collectors.toList()));
        }
        queryBuilder.setQuery(queryCondBuilder.build());
        logQuery(ApiPath.DOC_QUERY, queryBuilder);
        Olama.QueryResponse queryResponse = this.blockingStub.withInterceptors(new BackendServiceInterceptor(ai)).withDeadlineAfter(this.timeout, TimeUnit.SECONDS).query(queryBuilder.build());
        logResponse(ApiPath.DOC_QUERY, queryResponse);
        if(queryResponse==null){
            throw new VectorDBException("VectorDBServer error: query not response");
        }
        if (queryResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: query not Success, body code=%s, message=%s",
                    queryResponse.getCode(), queryResponse.getMsg()));
        }

        List<Olama.Document> documentsList = queryResponse.getDocumentsList();

        return documentsList.stream().map(document -> {
            return convertDocument(document);
        }).collect(Collectors.toList());
    }

    @Override
    public SearchRes searchDocument(SearchParamInner param, DataBaseTypeEnum dbType) {
        if (dbType.equals(DataBaseTypeEnum.AI_DB)){
            super.initHttpStub(this.connectParam);
            return super.searchDocument(param, dbType);
        }
        Olama.SearchRequest.Builder builder = Olama.SearchRequest.newBuilder().
                setReadConsistency(param.getReadConsistency().getReadConsistency());
        if (param.getDatabase()!=null){
            builder.setDatabase(param.getDatabase());
        }
        if (param.getCollection()!=null){
            builder.setCollection(param.getCollection());
        }
        SearchParam searchParam = param.getSearch();
        Olama.SearchCond.Builder searchConBuilder = Olama.SearchCond.newBuilder()
                .setRetrieveVector(searchParam.isRetrieveVector()).setLimit(searchParam.getLimit());
        if (searchParam.getOutputFields()!=null){
            searchConBuilder.addAllOutputfields(searchParam.getOutputFields());
        }
        if (searchParam.getFilter()!=null){
            searchConBuilder.setFilter(searchParam.getFilter());
        }
        if (searchParam instanceof SearchByVectorParam){
            ((SearchByVectorParam)searchParam).getVectors().forEach(vector->{
                Olama.VectorArray.Builder vectorArrayBuilder =  Olama.VectorArray.newBuilder();
                vector.forEach(ele->vectorArrayBuilder.addVector(Float.valueOf(ele.toString())));
                searchConBuilder.addVectors(vectorArrayBuilder.build());
            });

        }
        if (searchParam instanceof SearchByIdParam){
            searchConBuilder.addAllDocumentIds(((SearchByIdParam)searchParam).getDocumentIds());
        }
        if (searchParam instanceof SearchByEmbeddingItemsParam){
            searchConBuilder.addAllEmbeddingItems(((SearchByEmbeddingItemsParam)searchParam).getEmbeddingItems());
        }

        if (searchParam.getParams()!=null){
            if (searchParam.getParams() instanceof GeneralParams){
                GeneralParams params = (GeneralParams) searchParam.getParams();
                searchConBuilder.setParams(Olama.SearchParams.newBuilder().setEf(params.getEf())
                        .setNprobe(params.getNProbe())
                        .setRadius((float) params.getRadius()).build());
            }else if (searchParam.getParams() instanceof HNSWSearchParams){
                HNSWSearchParams params = (HNSWSearchParams) searchParam.getParams();
                searchConBuilder.setParams(Olama.SearchParams.newBuilder().setEf(params.getEf()).build());
            }
        }

        if (searchParam.getRadius()!=null){
            searchConBuilder.setRange(true);
            searchConBuilder.getParamsBuilder().setRadius(searchParam.getRadius());
        }
        builder.setSearch(searchConBuilder.build());
        logQuery(ApiPath.DOC_SEARCH, builder);
        Olama.SearchResponse searchResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).search(builder.build());
        logResponse(ApiPath.DOC_SEARCH, searchResponse);
        if(searchResponse==null){
            throw new VectorDBException("VectorDBServer error: search not response");
        }
        if (searchResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: search not Success, body code=%s, message=%s",
                    searchResponse.getCode(), searchResponse.getMsg()));
        }
        List<List<Document>> documentsList = new ArrayList<>();
        for (Olama.SearchResult searchResult : searchResponse.getResultsList()) {
            documentsList.add(searchResult.getDocumentsList().stream().map(GrpcStub::convertDocument)
                    .collect(Collectors.toList()));
        }
        return new SearchRes(searchResponse.getCode(),searchResponse.getMsg(), searchResponse.getWarning(), documentsList);
    }

    @Override
    public HybridSearchRes hybridSearchDocument(HybridSearchParamInner param, boolean ai) {
        Olama.SearchRequest.Builder builder = Olama.SearchRequest.newBuilder().
                setReadConsistency(param.getReadConsistency().getReadConsistency());
        if (param.getDatabase()!=null){
            builder.setDatabase(param.getDatabase());
        }
        if (param.getCollection()!=null){
            builder.setCollection(param.getCollection());
        }
        HybridSearchParam searchParam = param.getSearch();
        Olama.SearchCond.Builder searchConBuilder = Olama.SearchCond.newBuilder()
                .setRetrieveVector(searchParam.isRetrieveVector()).setLimit(searchParam.getLimit());
        if (searchParam.getOutputFields()!=null){
            searchConBuilder.addAllOutputfields(searchParam.getOutputFields());
        }
        if (searchParam.getFilter()!=null){
            searchConBuilder.setFilter(searchParam.getFilter());
        }
        if (searchParam.getAnn()!=null && !searchParam.getAnn().isEmpty()){
            searchConBuilder.addAllAnn(searchParam.getAnn().stream()
                    .map(annOption -> {
                        Olama.AnnData.Builder annBuilder = Olama.AnnData.newBuilder()
                                .setFieldName(annOption.getFieldName());
                        if (annOption.getDocumentIds()!=null){
                            annBuilder.addAllDocumentIds(annOption.getDocumentIds());
                        }
                        if (annOption.getData()!=null){
                            if (annOption.getData().get(0) instanceof String){
                                annBuilder.addAllEmbeddingItems(annOption.getData().stream().map(item->(String)item).collect(Collectors.toList()));
                            }if (annOption.getData().get(0) instanceof List){
                                annBuilder.addAllData(annOption.getData().stream()
                                        .map(item-> Olama.VectorArray.newBuilder().addAllVector(((List<Object>)item).
                                                stream().map(ele->Float.parseFloat(ele.toString())).collect(Collectors.toList())).build())
                                        .collect(Collectors.toList()));
                            }
                        }
                        if (annOption.getLimit()!=null){
                            annBuilder.setLimit(annOption.getLimit());
                        }
                        if (annOption.getParams()!=null){
                            if (annOption.getParams() instanceof GeneralParams){
                                GeneralParams params = (GeneralParams) annOption.getParams();
                                annBuilder.setParams(Olama.SearchParams.newBuilder().setEf(params.getEf())
                                        .setNprobe(params.getNProbe())
                                        .setRadius((float) params.getRadius()).build());
                            }else if (annOption.getParams() instanceof HNSWSearchParams){
                                HNSWSearchParams params = (HNSWSearchParams) annOption.getParams();
                                annBuilder.setParams(Olama.SearchParams.newBuilder().setEf(params.getEf()).build());
                            }
                        }
                        return annBuilder.build();
                    }).collect(Collectors.toList()));
        }
        if (searchParam.getMatch()!=null && !searchParam.getMatch().isEmpty()){
            searchConBuilder.addAllSparse(searchParam.getMatch().stream().map(matchOption -> {
                Olama.SparseData.Builder sparseBuilder = Olama.SparseData.newBuilder().setFieldName(matchOption.getFieldName());
                matchOption.getData().forEach(sparseVectors->{
                    sparseBuilder.addData(Olama.SparseVectorArray.newBuilder().addAllSpVector(sparseVectors.stream()
                            .map(vectors-> Olama.SparseVecItem.newBuilder().setTermId((Long) vectors.get(0)).
                                    setScore((Float.parseFloat(vectors.get(1).toString()))).
                                    build()).collect(Collectors.toList())).build());
                });
                if(matchOption.getLimit()!=null){
                    sparseBuilder.setLimit(matchOption.getLimit());
                }

                if (matchOption.getCutoffFrequency()!=null || matchOption.getTerminateAfter()!=null){
                    Olama.SparseSearchParams.Builder sparseSearchParamsBuilder = Olama.SparseSearchParams.newBuilder();
                    if (matchOption.getCutoffFrequency()!=null){
                        sparseSearchParamsBuilder.setCutoffFrequency(matchOption.getCutoffFrequency());
                    }
                    if (matchOption.getTerminateAfter()!=null){
                        sparseSearchParamsBuilder.setTerminateAfter(matchOption.getTerminateAfter());
                    }
                    sparseBuilder.setParams(sparseSearchParamsBuilder.build()).build();
                }
                return sparseBuilder.build();
            }).collect(Collectors.toList()));
        }
        if (searchParam.getRerank()!=null){
            Olama.RerankParams.Builder rerankBuilder = Olama.RerankParams.newBuilder()
                    .setMethod(searchParam.getRerank().getMethod());
            if (searchParam.getRerank() instanceof WeightRerankParam){
                WeightRerankParam weightRerankParam = (WeightRerankParam)searchParam.getRerank();
                Map<String, Float> weightMap = new HashMap<>();
                for (int i = 0; i < weightRerankParam.getFieldList().size(); i++) {
                    weightMap.put(weightRerankParam.getFieldList().get(i), weightRerankParam.getWeight().get(i).floatValue());
                }
                rerankBuilder.putAllWeights(weightMap);
            }else if (searchParam.getRerank() instanceof RRFRerankParam){
                RRFRerankParam rrfRerankParam = (RRFRerankParam)searchParam.getRerank();
                rerankBuilder.setRrfK(rrfRerankParam.getRrfK());
            }
            searchConBuilder.setRerankParams(rerankBuilder.build());
        }
        builder.setSearch(searchConBuilder);
        logQuery(ApiPath.DOC_HYBRID_SEARCH, builder);
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(ai));
        Olama.SearchResponse searchResponse = searchEngineBlockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).hybridSearch(builder.build());
        logResponse(ApiPath.DOC_HYBRID_SEARCH, searchResponse);

        if(searchResponse==null){
            throw new VectorDBException("VectorDBServer error: search not response");
        }
        if (searchResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: search not Success, body code=%s, message=%s",
                    searchResponse.getCode(), searchResponse.getMsg()));
        }
        List<List<Document>> documentsList = new ArrayList<>();
        for (Olama.SearchResult searchResult : searchResponse.getResultsList()) {
            List<Document> documents = searchResult.getDocumentsList().stream().map(GrpcStub::convertDocument)
                    .collect(Collectors.toList());
            if (!searchParam.getIsArrayParam()){
                return new HybridSearchRes(searchResponse.getCode(),searchResponse.getMsg(), searchResponse.getWarning(), Collections.unmodifiableList(documents));
            }else {
                documentsList.add(documents);
            }
        }
        return new HybridSearchRes(searchResponse.getCode(),searchResponse.getMsg(), searchResponse.getWarning(), Collections.unmodifiableList(documentsList));
    }

    private static void logQuery(String url, MessageOrBuilder messageOrBuilder) {
        try {
            logger.debug("Query {}, request body={}", url, JsonFormat.printer().print(messageOrBuilder));
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    private static void logResponse(String url, MessageOrBuilder messageOrBuilder) {
        try {
            logger.debug("Query {}, response={}", url, JsonFormat.printer().print(messageOrBuilder));
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AffectRes deleteDocument(DeleteParamInner param) {
        Olama.QueryCond.Builder queryCondBuilder = Olama.QueryCond.newBuilder();
        DeleteParam paramQuery = param.getQuery();
        if(paramQuery.getDocumentIds()!=null && !paramQuery.getDocumentIds().isEmpty()){
            queryCondBuilder.addAllDocumentIds(paramQuery.getDocumentIds());
        }
        if (paramQuery.getFilter()!=null && !paramQuery.getFilter().isEmpty()){
            queryCondBuilder.setFilter(paramQuery.getFilter());
        }
        if(param.getQuery().getLimit()!=null){
            queryCondBuilder.setLimit(param.getQuery().getLimit());
        }
        Olama.DeleteRequest.Builder deleteRequestBuilder = Olama.DeleteRequest.newBuilder();
        if (param.getDatabase()!=null){
            deleteRequestBuilder.setDatabase(param.getDatabase());
        }
        if (param.getCollection()!=null){
            deleteRequestBuilder.setCollection(param.getCollection());
        }

        Olama.DeleteRequest deleteRequest = deleteRequestBuilder.setQuery(queryCondBuilder.build()).build();
        logQuery(ApiPath.DOC_DELETE, deleteRequest);
        Olama.DeleteResponse deleteResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).dele(deleteRequest);
        logResponse(ApiPath.DOC_DELETE, deleteResponse);
        if(deleteResponse==null){
            throw new VectorDBException("VectorDBServer error: deleteResponse not response");
        }
        if (deleteResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: delete not Success, body code=%s, message=%s",
                    deleteResponse.getCode(), deleteResponse.getMsg()));
        }
        return new AffectRes(deleteResponse.getCode(), deleteResponse.getMsg(),"",deleteResponse.getAffectedCount());
    }

    @Override
    public AffectRes updateDocument(UpdateParamInner param, boolean ai) {
        Olama.QueryCond.Builder queryCondBuilder = Olama.QueryCond.newBuilder();
        UpdateParam paramQuery = param.getQuery();
        if(paramQuery.getDocumentIds()!=null && !paramQuery.getDocumentIds().isEmpty()){
            queryCondBuilder.addAllDocumentIds(paramQuery.getDocumentIds());
        }
        if (paramQuery.getFilter()!=null && !paramQuery.getFilter().isEmpty()){
            queryCondBuilder.setFilter(paramQuery.getFilter());
        }
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(ai));
        Olama.UpdateResponse updateResponse=null;
        Olama.UpdateRequest.Builder updateRequestBuilder = Olama.UpdateRequest.newBuilder();
        if (param.getDatabase()!=null) {
            updateRequestBuilder.setDatabase(param.getDatabase());
        }
        if (param.getCollection()!=null) {
            updateRequestBuilder.setCollection(param.getCollection());
        }
        if (param.getUpdate()!=null){
            updateRequestBuilder.setQuery(queryCondBuilder.build())
                    .setUpdate(convertDocument2OlamaDoc(param.getUpdate()));

        } else if (param.getUpdateData()!=null) {
            updateRequestBuilder.setQuery(queryCondBuilder.build())
                    .setUpdate(convertDocumentJSON2OlamaDoc(param.getUpdateData()));
        }
        logQuery(ApiPath.DOC_UPDATE, updateRequestBuilder.build());
        updateResponse = searchEngineBlockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                .update(updateRequestBuilder.build());
        logResponse(ApiPath.DOC_UPDATE, updateResponse);
        if(updateResponse==null){
            throw new VectorDBException("VectorDBServer error: update not response");
        }
        if (updateResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: update not Success, body code=%s, message=%s",
                    updateResponse.getCode(), updateResponse.getMsg()));
        }
        return new AffectRes(updateResponse.getCode(), updateResponse.getMsg(),updateResponse.getWarning(),updateResponse.getAffectedCount());
    }

    @Override
    public BaseRes rebuildIndex(RebuildIndexParamInner param) {
        Olama.RebuildIndexRequest.Builder builder = Olama.RebuildIndexRequest.newBuilder();
        if (param.getDatabase()!=null){
            builder.setDatabase(param.getDatabase());
        }
        if (param.getCollection()!=null){
            builder.setCollection(param.getCollection());
        }
        Olama.RebuildIndexRequest rebuildIndexRequest = builder
                .setThrottle(param.getThrottle())
                .setDropBeforeRebuild(param.isDropBeforeRebuild())
                .build();
        logQuery(ApiPath.REBUILD_INDEX, rebuildIndexRequest);
        Olama.RebuildIndexResponse rebuildIndexResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).rebuildIndex(rebuildIndexRequest);
        logResponse(ApiPath.REBUILD_INDEX, rebuildIndexResponse);
        if(rebuildIndexResponse==null){
            throw new VectorDBException("VectorDBServer error: rebuildIndex not response");
        }
        if (rebuildIndexResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: search not Success, body code=%s, message=%s",
                    rebuildIndexResponse.getCode(), rebuildIndexResponse.getMsg()));
        }
        return new BaseRes(rebuildIndexResponse.getCode(), rebuildIndexResponse.getMsg(), "");
    }

    @Override
    public AffectRes setAIAlias(String databaseName, String collectionName, String aliasName) {
        super.initHttpStub(this.connectParam);
        return super.setAIAlias(databaseName, collectionName, aliasName);
    }

    @Override
    public AffectRes deleteAIAlias(String databaseName, String aliasName) {
        super.initHttpStub(this.connectParam);
        return super.deleteAIAlias(databaseName, aliasName);
    }

    @Override
    public List<CollectionView> listCollectionView(String databaseName) {
        super.initHttpStub(this.connectParam);
        return super.listCollectionView(databaseName);
    }

    @Override
    public CollectionView describeCollectionView(String databaseName, String collectionName) {
        super.initHttpStub(this.connectParam);
        return super.describeCollectionView(databaseName, collectionName);
    }

    @Override
    public AffectRes dropCollectionView(String databaseName, String collectionName) {
        super.initHttpStub(this.connectParam);
        return super.dropCollectionView(databaseName, collectionName);
    }

    @Override
    public List<DocumentSet> queryAIDocument(CollectionViewQueryParamInner queryParamInner) {
        super.initHttpStub(this.connectParam);
        return super.queryAIDocument(queryParamInner);
    }

    @Override
    public AffectRes deleteAIDocument(CollectionViewDeleteParamInner deleteParamInner) {
        super.initHttpStub(this.connectParam);
        return super.deleteAIDocument(deleteParamInner);
    }

    @Override
    public SearchContentRes searchAIDocument(SearchDocParamInner searchDocParamInner) {
        super.initHttpStub(this.connectParam);
        return super.searchAIDocument(searchDocParamInner);
    }

    @Override
    public AffectRes updateAIDocument(CollectionViewUpdateParamInner updateParamInner) {
        super.initHttpStub(this.connectParam);
        return super.updateAIDocument(updateParamInner);
    }

    @Override
    public void upload(String databaseName, String collectionName, LoadAndSplitTextParam loadAndSplitTextParam, Map<String, Object> metaDataMap) throws Exception {
        super.initHttpStub(this.connectParam);
        super.upload(databaseName, collectionName, loadAndSplitTextParam, metaDataMap);
    }

    @Override
    public void collectionUpload(String databaseName, String collectionName, UploadFileParam loadAndSplitTextParam, Map<String, Object> metaDataMap) throws Exception {
        super.initHttpStub(this.connectParam);
        super.collectionUpload(databaseName, collectionName, loadAndSplitTextParam, metaDataMap);
    }

    @Override
    public GetDocumentSetRes getFile(String databaseName, String collectionName, String fileName, String fileId) {
        super.initHttpStub(this.connectParam);
        return super.getFile(databaseName, collectionName, fileName, fileId);
    }

    @Override
    public GetChunksRes getChunks(String databaseName, String collectionName, String documentSetName, String documentSetId, Integer limit, Integer offset) {
        super.initHttpStub(this.connectParam);
        return super.getChunks(databaseName, collectionName, documentSetName, documentSetId, limit, offset);
    }

    @Override
    public GetImageUrlRes GetImageUrl(GetImageUrlParamInner param) {
        super.initHttpStub(this.connectParam);
        return super.GetImageUrl(param);
    }

    @Override
    public BaseRes rebuildAIIndex(RebuildIndexParamInner param) {
        super.initHttpStub(this.connectParam);
        return super.rebuildAIIndex(param);
    }

    public BaseRes countDocument(QueryCountParamInner param, boolean ai) {
        Olama.CountRequest.Builder countCondBuilder = Olama.CountRequest.newBuilder();
        if(param.getDatabase()!=null){
            countCondBuilder.setDatabase(param.getDatabase());
        }
        if(param.getCollection()!=null){
            countCondBuilder.setCollection(param.getCollection());
        }
        if (param.getQuery()!=null) {
            Olama.QueryCond.Builder queryBuilder =  Olama.QueryCond.newBuilder();
            if (param.getQuery().getFilter()!=null){
                queryBuilder.setFilter(param.getQuery().getFilter());
            }
            countCondBuilder.setQuery(queryBuilder.build());
        }
        Olama.CountRequest countRequest = countCondBuilder.build();
        logQuery(ApiPath.DOC_COUNT, countCondBuilder);
        Olama.CountResponse countResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).count(countRequest);
        logResponse(ApiPath.DOC_COUNT, countResponse);
        if(countResponse==null){
            throw new VectorDBException("VectorDBServer error: count not response");
        }
        if (countResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: count not Success, body code=%s, message=%s",
                    countResponse.getCode(), countResponse.getMsg()));
        }
        return new BaseRes(countResponse.getCode(), countResponse.getMsg(),"", countResponse.getCount());
    }

    @Override
    public BaseRes modifyVectorIndex(ModifyIndexParamInner param, boolean ai) {
        Olama.ModifyVectorIndexRequest.Builder builder = Olama.ModifyVectorIndexRequest.newBuilder();
        if(param.getDatabase()!=null){
            builder.setDatabase(param.getDatabase());
        }
        if(param.getCollection()!=null){
            builder.setCollection(param.getCollection());
        }
        if (param.getRebuildRules()!=null){
            builder.setRebuildRules(Olama.RebuildIndexRequest.newBuilder()
                    .setDropBeforeRebuild(param.getRebuildRules().getDropBeforeRebuild())
                    .setThrottle(param.getRebuildRules().getThrottle()).build());
        }
        if (param.getVectorIndexes() !=null && !param.getVectorIndexes().isEmpty()){
            builder.putAllVectorIndexes(param.getVectorIndexes().stream()
                    .map(indexField -> getRpcIndexBuilder(indexField).build()).collect(Collectors.toMap(index -> index.getFieldName(), index -> index)));
        }
        logQuery(ApiPath.MODIFY_VECTOR_INDEX, builder);
        Olama.ModifyVectorIndexResponse modifyVectorIndexResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                .modifyVectorIndex(builder.build());
        logResponse(ApiPath.MODIFY_VECTOR_INDEX, modifyVectorIndexResponse);
        if(modifyVectorIndexResponse==null){
            throw new VectorDBException("VectorDBServer error: modifyVectorIndex not response");
        }
        if (modifyVectorIndexResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: modifyVectorIndex not Success, body code=%s, message=%s",
                    modifyVectorIndexResponse.getCode(), modifyVectorIndexResponse.getMsg()));
        }
        return new BaseRes(modifyVectorIndexResponse.getCode(), modifyVectorIndexResponse.getMsg(), "");
    }


    @Override
    public BaseRes addIndex(AddIndexParamInner addIndexParamInner) {
        Olama.AddIndexRequest.Builder builder = Olama.AddIndexRequest.newBuilder();
        if(addIndexParamInner.getDatabase()!=null){
            builder.setDatabase(addIndexParamInner.getDatabase());
        }
        if(addIndexParamInner.getCollection()!=null){
            builder.setCollection(addIndexParamInner.getCollection());
        }
        Olama.AddIndexRequest addIndexRequest = builder.setBuildExistedData(addIndexParamInner.isBuildExistedData())
                .putAllIndexes(addIndexParamInner.getIndexes().stream()
                        .map(indexField -> getRpcIndexBuilder(indexField).build()).collect(Collectors.toMap(index -> index.getFieldName(), index -> index)))
                .build();

        logQuery(ApiPath.ADD_INDEX, addIndexRequest);
        Olama.AddIndexResponse addIndexResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).addIndex(addIndexRequest);
        logResponse(ApiPath.REBUILD_INDEX, addIndexResponse);
        if(addIndexResponse==null){
            throw new VectorDBException("VectorDBServer error: addIndex not response");
        }
        if (addIndexResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: search not Success, body code=%s, message=%s",
                    addIndexResponse.getCode(), addIndexResponse.getMsg()));
        }
        return new BaseRes(addIndexResponse.getCode(), addIndexResponse.getMsg(), "");
    }

    @Override
    public BaseRes createUser(UserCreateParam userCreateParam) {
        Olama.UserAccountRequest.Builder builder = Olama.UserAccountRequest
                .newBuilder();
        if (userCreateParam.getUser()!=null){
            builder.setUser(userCreateParam.getUser());
        }
        if (userCreateParam.getPassword()!=null){
            builder.setPassword(userCreateParam.getPassword()).build();
        }
        logQuery(ApiPath.USER_CREATE, builder.build());
        Olama.UserAccountResponse response = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).userCreate(builder.build());
        logResponse(ApiPath.USER_CREATE, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: create user account error, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return new BaseRes(response.getCode(), response.getMsg(), "");
    }


    @Override
    public BaseRes grantToUser(UserGrantParam param) {

        Olama.UserPrivilegesRequest.Builder builder = Olama.UserPrivilegesRequest.newBuilder();
        if (param.getUser()!=null){
            builder.setUser(param.getUser());
        }
        if (param.getPrivileges()!=null){
            builder.addAllPrivileges(param.getPrivileges().stream().map(privilege ->
                    Olama.Privilege.newBuilder()
                            .setResource(privilege.getResource())
                            .addAllActions(privilege.getActions()).build()).collect(Collectors.toList()));
        }
        logQuery(ApiPath.USER_GRANT, builder);
        Olama.UserPrivilegesResponse response = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).userGrant(builder.build());
        logResponse(ApiPath.USER_GRANT, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: grant user account error, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return new BaseRes(response.getCode(), response.getMsg(), "");

    }


    @Override
    public BaseRes revokeFromUser(UserRevokeParam param) {
        Olama.UserPrivilegesRequest.Builder builder = Olama.UserPrivilegesRequest.newBuilder();
        if (param.getUser()!=null){
            builder.setUser(param.getUser());
        }
        if (param.getPrivileges()!=null){
            builder.addAllPrivileges(param.getPrivileges().stream().map(privilege ->
                    Olama.Privilege.newBuilder()
                            .setResource(privilege.getResource())
                            .addAllActions(privilege.getActions()).build()).collect(Collectors.toList()));
        }
        logQuery(ApiPath.USER_REVOKE, builder);
        Olama.UserPrivilegesResponse response = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).userRevoke(builder.build());
        logResponse(ApiPath.USER_REVOKE, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: revoke user error, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return new BaseRes(response.getCode(), response.getMsg(), "");
    }

    @Override
    public UserDescribeRes describeUser(UserDescribeParam userDescribeParam) {

        Olama.UserDescribeRequest.Builder builder = Olama.UserDescribeRequest.newBuilder();
        if (userDescribeParam.getUser()!=null){
            builder.setUser(userDescribeParam.getUser());
        }
        logQuery(ApiPath.USER_DESCRIBE, builder.build());
        Olama.UserDescribeResponse response = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).userDescribe(builder.build());
        logResponse(ApiPath.USER_DESCRIBE, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: revoke user error, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        UserDescribeRes res = new UserDescribeRes(response.getCode(), response.getMsg(), "");
        if (response.hasUser()){
            res.setUser(response.getUser().getName());
            res.setPrivileges(response.getUser().getPrivilegesList().stream().map(privilege ->
                    PrivilegeParam.newBuilder().withResource(privilege.getResource()).withActions(privilege.getActionsList()).build())
                    .collect(Collectors.toList()));
        }
        return res;
    }

    @Override
    public UserListRes listUser() {
        Olama.UserListRequest request = Olama.UserListRequest.newBuilder().build();
        logQuery(ApiPath.USER_LIST, request);
        Olama.UserListResponse response = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).userList(request);
        logResponse(ApiPath.USER_LIST, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: list user error, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        UserListRes res = new UserListRes(response.getCode(), response.getMsg(), "");
        if (response.getUsersList()!=null && !response.getUsersList().isEmpty()){
            res.setUsers(response.getUsersList().stream().map(user -> {
                UserInfo userInfo = new UserInfo();
                userInfo.setUser(user.getName());
                userInfo.setPrivileges(user.getPrivilegesList().stream().map(privilege ->
                                PrivilegeParam.newBuilder().withResource(privilege.getResource()).withActions(privilege.getActionsList()).build())
                        .collect(Collectors.toList()));
                return userInfo;
            }).collect(Collectors.toList()));
        }
        return res;
    }

    @Override
    public BaseRes dropUser(UserDropParam userDropParam) {
        Olama.UserAccountRequest.Builder builder = Olama.UserAccountRequest.newBuilder();
        if (userDropParam.getUser() != null) {
            builder.setUser(userDropParam.getUser());
        }

        logQuery(ApiPath.USER_DROP, builder.build());
        Olama.UserAccountResponse response = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).userDrop(builder.build());
        logResponse(ApiPath.USER_DROP, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: drop user error, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return new BaseRes(response.getCode(), response.getMsg(), "");
    }

    @Override
    public BaseRes changeUserPassword(UserChangePasswordParam param) {
        Olama.UserAccountRequest.Builder builder = Olama.UserAccountRequest
                .newBuilder();
        if (param.getUser()!=null){
            builder.setUser(param.getUser());
        }
        if (param.getPassword()!=null){
            builder.setPassword(param.getPassword()).build();
        }
        logQuery(ApiPath.USER_CHANGE_PASSWORD, builder.build());
        Olama.UserAccountResponse response = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).userChangePassword(builder.build());
        logResponse(ApiPath.USER_CHANGE_PASSWORD, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: change user password error, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return new BaseRes(response.getCode(), response.getMsg(), "");
    }

    @Override
    public BaseRes dropIndex(DropIndexParamInner dropIndexParamInner) {
        Olama.DropIndexRequest.Builder builder = Olama.DropIndexRequest
                .newBuilder();
        if (dropIndexParamInner.getDatabase()!=null){
            builder.setDatabase(dropIndexParamInner.getDatabase());
        }
        if (dropIndexParamInner.getCollection()!=null){
            builder.setCollection(dropIndexParamInner.getCollection());
        }
        if (dropIndexParamInner.getFieldNames()!=null && !dropIndexParamInner.getFieldNames().isEmpty()){
            builder.addAllFieldNames(dropIndexParamInner.getFieldNames());
        }
        logQuery(ApiPath.DROP_INDEX, builder.build());
        Olama.DropIndexResponse response = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).dropIndex(builder.build());
        logResponse(ApiPath.USER_CHANGE_PASSWORD, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: drop user index error, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return new BaseRes(response.getCode(), response.getMsg(), "");
    }

    private static Collection convertRpcToCollection(Olama.CreateCollectionRequest collection) {
        Collection collectionInner = new Collection();
        collectionInner.setDatabase(collection.getDatabase());
        collectionInner.setCollection(collection.getCollection());
        if(!collection.getEmbeddingParams().getField().isEmpty()){
            collectionInner.setEmbedding(Embedding.newBuilder()
                            .withField(collection.getEmbeddingParams().getField())
                            .withModel(EmbeddingModelEnum.find(collection.getEmbeddingParams().getModelName()))
                            .withVectorField(collection.getEmbeddingParams().getVectorField())
                    .build());
        }
        collectionInner.setDescription(collection.getDescription());
        collectionInner.setAlias(collection.getAliasListList());
        collectionInner.setCreateTime(collection.getCreateTime());
        collectionInner.setShardNum(collection.getShardNum());
        collectionInner.setReplicaNum(collection.getReplicaNum());
        collectionInner.setIndexStatus(new Collection.IndexStatus(collection.getIndexStatus().getStatus(), collection.getIndexStatus().getStartTime()));
        if (collection.hasTtlConfig()){
            collectionInner.setTtlConfig(TTLConfig.newBuilder()
                            .WithEnable(collection.getTtlConfig().getEnable())
                            .WithTimeField(collection.getTtlConfig().getTimeField())
                    .build());
        }
        if (collection.hasFilterIndexConfig()){
            collectionInner.setFilterIndexConfig(FilterIndexConfig.newBuilder()
                            .withFieldWithoutFilterIndex(collection.getFilterIndexConfig().getFieldsWithoutIndexList())
                            .withFilterAll(collection.getFilterIndexConfig().getFilterAll())
                    .build());
        }
        collectionInner.setIndexes(collection.getIndexesMap().entrySet().stream().map(entry->{
            IndexField indexField = new IndexField();
            indexField.setFieldName(entry.getValue().getFieldName());
            indexField.setFieldType(FieldType.fromValue(entry.getValue().getFieldType()));
            indexField.setIndexType(IndexType.fromValue(entry.getValue().getIndexType()));
            if (indexField.isVectorField()){
                indexField.setMetricType(MetricType.fromValue(entry.getValue().getMetricType()));
                indexField.setDimension(entry.getValue().getDimension());
                if(entry.getValue().hasParams()){
                    switch (indexField.getIndexType()) {
                        case HNSW:
                            indexField.setParams(new HNSWParams(entry.getValue().getParams().getM(), entry.getValue().getParams().getEfConstruction()));
                            break;
                        case IVF_FLAT:
                            indexField.setParams(new IVFFLATParams(entry.getValue().getParams().getNlist()));
                            break;
                        case IVF_PQ:
                            indexField.setParams(new IVFPQParams(entry.getValue().getParams().getNlist(),entry.getValue().getParams().getM()));
                            break;
                        case IVF_SQ8:
                            indexField.setParams(new IVFSQ8Params(entry.getValue().getParams().getNlist()));
                            break;
                    }

                }
            }
            if (indexField.isSparseVectorField()){
                indexField.setMetricType(MetricType.fromValue(entry.getValue().getMetricType()));
            }
            if(indexField.getFieldType()== FieldType.Array){
                indexField.setFieldElementType(FieldElementType.fromValue(entry.getValue().getFieldElementType()));
            }
            if(entry.getValue().getAutoId()!=null && !entry.getValue().getAutoId().equals("")){
                indexField.setAutoId(entry.getValue().getAutoId());
            }
            return indexField;
        }).collect(Collectors.toList()));
        return collectionInner;
    }



    private static Olama.Document convertDocument2OlamaDoc(Document document) {
        Olama.Document.Builder docBuilder = Olama.Document.newBuilder();
        if (document.getId()!=null){
            docBuilder.setId(document.getId());
        }
        if (document.getVector()!=null){
            if (document.getVector() instanceof List){
                docBuilder.addAllVector(((List<Number>)document.getVector()).stream().map(ele->ele.floatValue()).collect(Collectors.toList()));
            }else if (document.getVector() instanceof String){
                docBuilder.setDataExpr((String)document.getVector());
            }
        }
        if (document.getSparseVector()!=null){
            docBuilder.addAllSparseVector(document.getSparseVector().stream()
                    .map(pair->Olama.SparseVecItem.newBuilder().setTermId(pair.getKey()).setScore(pair.getValue().floatValue()).build())
                    .collect(Collectors.toList()));
        }
        document.getDocFields().forEach(docField -> {
            Olama.Field.Builder fieldBuilder = Olama.Field.newBuilder();
            if (docField.getValue() instanceof Integer || docField.getValue() instanceof Long) {
                fieldBuilder.setValU64(Long.parseLong(docField.getValue().toString()));
            } else if (docField.getValue() instanceof Double || docField.getValue() instanceof Float) {
                fieldBuilder.setValDouble(Double.parseDouble(docField.getValue().toString()));
            }else if(docField.getValue() instanceof String){
                fieldBuilder.setValStr(ByteString.copyFromUtf8(docField.getValue().toString()));
            }else if (docField.getValue() instanceof List && ((List<?>) docField.getValue()).get(0) instanceof String ){
                fieldBuilder.setValStrArr(Olama.Field.StringArray.newBuilder().addAllStrArr(
                        ((List<?>) docField.getValue()).stream().map(ele-> ByteString.copyFromUtf8((String)ele)).collect(Collectors.toList())));
            }else if(docField.getValue() instanceof JSONObject){
                fieldBuilder.setValJson(ByteString.copyFromUtf8(docField.getValue().toString()));
            }else {
                throw new VectorDBException("Unsupported field type,  field key:" + docField.getName() + " type:"+ docField.getValue().getClass() +"\n" +
                        "supported field type is:  Integer,Long,Double,Float,String,List<String>,JSONObject");
            }
            docBuilder.putFields(docField.getName(), fieldBuilder.build());
        });
        return docBuilder.build();
    }

    private Olama.Document convertDocumentJSON2OlamaDoc(JSONObject document) {
        Olama.Document.Builder docBuilder = Olama.Document.newBuilder();
        document.keySet().forEach(key->{
            if (key.equals("id")){
                docBuilder.setId(document.get(key).toString());
            }else if (key.equals("vector")){
                docBuilder.addAllVector((((JSONArray)document.get(key)).toList()).stream().map(vecEle -> Float.parseFloat(vecEle.toString())).collect(Collectors.toList()));
            } else if (key.equals("sparse_vector")) {
                List<Object> sparseVectors = ((List)document.get("sparse_vector"));
                docBuilder.addAllSparseVector(sparseVectors.stream().map(pair->
                             Olama.SparseVecItem.newBuilder().setTermId((Long) ((List<Object>)pair).get(0))
                                    .setScore((Float) ((List<Object>)pair).get(1)).build()).collect(Collectors.toList()));
            } else {
                Olama.Field.Builder fieldBuilder = Olama.Field.newBuilder();
                if (document.get(key) instanceof Integer || document.get(key) instanceof Long) {
                    fieldBuilder.setValU64(Long.parseLong(document.get(key).toString()));
                } else if (document.get(key) instanceof Double || document.get(key) instanceof Float) {
                    fieldBuilder.setValDouble(Double.parseDouble(document.get(key).toString()));
                } else if (document.get(key) instanceof String) {
                    fieldBuilder.setValStr(ByteString.copyFromUtf8(document.get(key).toString()));
                }else if(document.get(key) instanceof JSONArray && ((JSONArray)document.get(key)).get(0) instanceof String){
                    fieldBuilder.setValStrArr(Olama.Field.StringArray.newBuilder().addAllStrArr(((JSONArray)document.get(key)).toList().stream().map(ele-> ByteString.copyFromUtf8((String)ele)).collect(Collectors.toList())));
                }else if(document.get(key) instanceof JSONObject){
                    fieldBuilder.setValJson(ByteString.copyFromUtf8(document.get(key).toString()));
                }
                else {
                    throw new VectorDBException("Unsupported field type, field:+"+ key +" type:"+ document.get(key).getClass()
                            + "\nsupported field type is:  Integer,Long,Double,Float,String,JSONArray<String>,JSONObject");
                }
                docBuilder.putFields(key, fieldBuilder.build());
            }
        });
        return docBuilder.build();
    }

    private static Document convertDocument(Olama.Document document) {
        Document.Builder builder =  Document.newBuilder().withId(document.getId());
        builder.withScore(Double.valueOf(document.getScore()));
        if (document.getVectorCount()>0){
            builder.withVector(document.getVectorList().stream().map(ele->ele.doubleValue()).collect(Collectors.toList()));
        }
        if (document.getSparseVectorCount()>0){
            builder.withSparseVector(document.getSparseVectorList().stream().map(sparseVecItem->
                    Pair.of(sparseVecItem.getTermId(),sparseVecItem.getScore())).collect(Collectors.toList()));
        }
        if(document.getFieldsMap()!=null && document.getFieldsMap().size()>0){
            for (Map.Entry<String, Olama.Field> stringFieldEntry : document.getFieldsMap().entrySet()) {
                if (stringFieldEntry.getValue().hasValDouble()){
                    builder.addDocField(new DocField(stringFieldEntry.getKey(), stringFieldEntry.getValue().getValDouble()));
                }
                if (stringFieldEntry.getValue().hasValU64()){
                    builder.addDocField(new DocField(stringFieldEntry.getKey(), stringFieldEntry.getValue().getValU64()));
                }
                if (stringFieldEntry.getValue().hasValStr()){
                    builder.addDocField(new DocField(stringFieldEntry.getKey(), stringFieldEntry.getValue().getValStr().toString(StandardCharsets.UTF_8)));
                }
                if (stringFieldEntry.getValue().hasValJson()){
                    builder.addDocField(new DocField(stringFieldEntry.getKey(), new JSONObject(stringFieldEntry.getValue().getValJson().toString(StandardCharsets.UTF_8))));
                }
                if (stringFieldEntry.getValue().hasValStrArr()){
                    builder.addDocField(new DocField(stringFieldEntry.getKey(),
                            stringFieldEntry.getValue().getValStrArr().getStrArrList().stream().map(ele->
                                    ele.toString(StandardCharsets.UTF_8)
                            ).collect(Collectors.toList())));
                }
            }
        }
        return builder.build();
    }
}

