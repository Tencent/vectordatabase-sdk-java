package com.tencent.tcvectordb.service;

import com.google.protobuf.ByteString;
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
import com.tencent.tcvectordb.rpc.Interceptor.AuthorityInterceptor;
import com.tencent.tcvectordb.rpc.proto.Olama;
import com.tencent.tcvectordb.rpc.proto.SearchEngineGrpc;
import com.tencent.tcvectordb.service.param.*;
import io.grpc.*;
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
    public GrpcStub(ConnectParam param){
        super(param);
        this.authorization = String.format("Bearer account=%s&api_key=%s",param.getUsername(), param.getKey());

        this.channel = ManagedChannelBuilder.forTarget(this.getAddress(param.getUrl())).
                intercept(new AuthorityInterceptor(this.authorization)).
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
    public void createDatabase(Database database) {
        Olama.DatabaseResponse response = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).
                createDatabase(Olama.DatabaseRequest.newBuilder().
                setDatabase(database.getDatabaseName()).build());
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer create Database error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
    }

    @Override
    public void dropDatabase(Database database) {

        Olama.DatabaseRequest request = Olama.DatabaseRequest.newBuilder().setDatabase(database.getDatabaseName()).build();
        Olama.DatabaseResponse response =  this.blockingStub.
                withDeadlineAfter(this.timeout, TimeUnit.SECONDS).dropDatabase(request);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer drop Database error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
    }

    @Override
    public AffectRes createAIDatabase(AIDatabase aiDatabase) {
        return super.createAIDatabase(aiDatabase);
    }

    @Override
    public DataBaseTypeRes describeDatabase(Database database) {
        Olama.DescribeDatabaseResponse response = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).describeDatabase(Olama.DescribeDatabaseRequest.newBuilder().setDatabase(database.getDatabaseName()).build());
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
        return super.dropAIDatabase(aiDatabase);
    }

    @Override
    public List<String> listDatabases() {
        Olama.DatabaseRequest request = Olama.DatabaseRequest.newBuilder().build();
        Olama.DatabaseResponse response = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).listDatabases(request);
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
        Olama.DatabaseResponse response =  this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).listDatabases(request);
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
        Olama.CreateCollectionRequest.Builder requestOrBuilder = Olama.CreateCollectionRequest.newBuilder()
                .setShardNum(params.getShardNum())
                .setReplicaNum(params.getReplicaNum())
                .setCollection(params.getCollection())
                .setDatabase(params.getDatabase())
                .setDescription(params.getDescription());
        if (params.getAlias()!=null){
            requestOrBuilder.addAllAliasList(params.getAlias());
        }
        if (params.getEmbedding()!=null){
            requestOrBuilder.setEmbeddingParams(Olama.EmbeddingParams.newBuilder()
                            .setField(params.getEmbedding().getField())
                            .setModelName(params.getEmbedding().getModel().getModelName())
                            .setVectorField(params.getEmbedding().getVectorField())
                    .build());
        }
        if (!params.getIndexes().isEmpty()){
            for (IndexField index : params.getIndexes()) {
                Olama.IndexColumn.Builder indexBuilder = Olama.IndexColumn.newBuilder()
                        .setFieldName(index.getFieldName())
                        .setFieldType(index.getFieldType().getValue())
                        .setIndexType(index.getIndexType().getValue());
                if(index.isVectorField()){
                    indexBuilder.setDimension(index.getDimension());
                    indexBuilder.setMetricType(index.getMetricType().getValue());
                    if(index.getParams()!=null){
                        switch (index.getIndexType()) {
                            case HNSW:
                                HNSWParams hnswParams = (HNSWParams) index.getParams();
                                indexBuilder.setParams(Olama.IndexParams.newBuilder()
                                        .setM(hnswParams.getM())
                                        .setEfConstruction(hnswParams.getEfConstruction()).build());
                                break;
                            case IVF_FLAT:
                                IVFFLATParams ivfflatParams = (IVFFLATParams) index.getParams();
                                indexBuilder.setParams(Olama.IndexParams.newBuilder()
                                        .setNlist(ivfflatParams.getNList()).build());
                                break;
                            case IVF_PQ:
                                IVFPQParams ivfpqParams = (IVFPQParams) index.getParams();
                                indexBuilder.setParams(Olama.IndexParams.newBuilder()
                                        .setNlist(ivfpqParams.getNList()).setM(ivfpqParams.getM()).build());
                                break;
                            case IVF_SQ8:
                                IVFSQ8Params ivfsq8Params = (IVFSQ8Params) index.getParams();
                                indexBuilder.setParams(Olama.IndexParams.newBuilder()
                                        .setNlist(ivfsq8Params.getNList()).build());
                                break;
                        }
                    }
                }
                if(index.getFieldType()==FieldType.Array){
                    indexBuilder.setFieldElementType(FieldElementType.String.getValue());
                }
                requestOrBuilder.putIndexes(index.getFieldName(), indexBuilder.build());
            }

        }

        Olama.CreateCollectionResponse response =  this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                .createCollection(requestOrBuilder.build());
        if(response==null){
            throw new VectorDBException("VectorDBServer error: CreateCollectionResponse not response");
        }
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
    }

    @Override
    public void createCollectionView(CreateCollectionViewParam params) {
        super.createCollectionView(params);
    }

    @Override
    public List<Collection> listCollections(String databaseName) {
        Olama.ListCollectionsRequest request = Olama.ListCollectionsRequest.newBuilder().
                setDatabase(databaseName).setTransfer(false).build();
        Olama.ListCollectionsResponse response = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                .listCollections(request);
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
        Olama.DescribeCollectionResponse describeCollectionResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                .describeCollection(Olama.DescribeCollectionRequest.newBuilder()
                .setDatabase(databaseName)
                .setCollection(collectionName)
                .build());
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
        Olama.TruncateCollectionResponse truncateCollectionResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).
                truncateCollection(Olama.TruncateCollectionRequest.newBuilder()
                .setDatabase(databaseName)
                .setCollection(collectionName)
                .build());
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
        return super.truncateCollectionView(databaseName, collectionName, dbType);
    }

    @Override
    public void dropCollection(String databaseName, String collectionName) {
        Olama.DropCollectionResponse dropCollectionResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).
                dropCollection(Olama.DropCollectionRequest.newBuilder().
                setDatabase(databaseName).
                setCollection(collectionName).build());
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
        Olama.UpdateAliasResponse setAliasResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).
                setAlias(Olama.AddAliasRequest.newBuilder().setDatabase(databaseName)
                .setCollection(collectionName)
                .setAlias(aliasName).build());
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

        Olama.UpdateAliasResponse deleteResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                .deleteAlias(Olama.RemoveAliasRequest.newBuilder().
                setDatabase(databaseName).setAlias(aliasName).build());
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
    public AffectRes upsertDocument(InsertParamInner param) {
        Olama.UpsertRequest.Builder builder = Olama.UpsertRequest.newBuilder()
                .setDatabase(param.getDatabase()).setCollection(param.getCollection());
        if (param.getBuildIndex()==null){
            builder.setBuildIndex(true);
        }else {
            builder.setBuildIndex(param.getBuildIndex());
        }

        if (!param.getDocuments().isEmpty()){
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
        Olama.UpsertResponse response = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).upsert(builder.build());
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer upsert data error: not Successful, code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return new AffectRes(response.getCode(), response.getMsg(), response.getWarning(), response.getAffectedCount());
    }

    @Override
    public List<Document> queryDocument(QueryParamInner param) {
        Olama.QueryRequest.Builder queryBuilder = Olama.QueryRequest.newBuilder().
                setDatabase(param.getDatabase()).setCollection(param.getCollection())
                .setReadConsistency(param.getReadConsistency().getReadConsistency());
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

        queryBuilder.setQuery(queryCondBuilder.build());
        Olama.QueryResponse queryResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).query(queryBuilder.build());
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
            return super.searchDocument(param, dbType);
        }
        Olama.SearchRequest.Builder builder = Olama.SearchRequest.newBuilder().setDatabase(param.getDatabase()).
                setCollection(param.getCollection()).
                setReadConsistency(param.getReadConsistency().getReadConsistency());
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
                vector.forEach(ele->vectorArrayBuilder.addVector(ele.floatValue()));
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
        builder.setSearch(searchConBuilder.build());
        Olama.SearchResponse searchResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).search(builder.build());
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
    public AffectRes deleteDocument(DeleteParamInner param) {
        Olama.QueryCond.Builder queryCondBuilder = Olama.QueryCond.newBuilder();
        DeleteParam paramQuery = param.getQuery();
        if(!paramQuery.getDocumentIds().isEmpty()){
            queryCondBuilder.addAllDocumentIds(paramQuery.getDocumentIds());
        }
        if (!paramQuery.getFilter().isEmpty()){
            queryCondBuilder.setFilter(paramQuery.getFilter());
        }
        Olama.DeleteResponse deleteResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).dele(Olama.DeleteRequest.newBuilder()
                .setDatabase(param.getDatabase())
                .setCollection(param.getCollection())
                .setQuery(queryCondBuilder.build())
                .build());
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
    public AffectRes updateDocument(UpdateParamInner param) {
        Olama.QueryCond.Builder queryCondBuilder = Olama.QueryCond.newBuilder();
        UpdateParam paramQuery = param.getQuery();
        if(!paramQuery.getDocumentIds().isEmpty()){
            queryCondBuilder.addAllDocumentIds(paramQuery.getDocumentIds());
        }
        if (!paramQuery.getFilter().isEmpty()){
            queryCondBuilder.setFilter(paramQuery.getFilter());
        }
        Olama.UpdateResponse updateResponse=null;
        if (param.getUpdate()!=null){
            updateResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).update(Olama.UpdateRequest.newBuilder()
                    .setDatabase(param.getDatabase())
                    .setCollection(param.getCollection())
                    .setQuery(queryCondBuilder.build())
                    .setUpdate(convertDocument2OlamaDoc(param.getUpdate()))
                    .build());
        } else if (param.getUpdateData()!=null) {
            updateResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).update(Olama.UpdateRequest.newBuilder()
                    .setDatabase(param.getDatabase())
                    .setCollection(param.getCollection())
                    .setQuery(queryCondBuilder.build())
                    .setUpdate(convertDocumentJSON2OlamaDoc(param.getUpdateData()))
                    .build());
        }

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
        Olama.RebuildIndexResponse rebuildIndexResponse = this.blockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).rebuildIndex(Olama.RebuildIndexRequest.newBuilder()
                .setDatabase(param.getDatabase())
                .setCollection(param.getCollection())
                .setThrottle(param.getThrottle())
                .setDropBeforeRebuild(param.isDropBeforeRebuild())
                .build());
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
        return super.setAIAlias(databaseName, collectionName, aliasName);
    }

    @Override
    public AffectRes deleteAIAlias(String databaseName, String aliasName) {
        return super.deleteAIAlias(databaseName, aliasName);
    }

    @Override
    public List<CollectionView> listCollectionView(String databaseName) {
        return super.listCollectionView(databaseName);
    }

    @Override
    public CollectionView describeCollectionView(String databaseName, String collectionName) {
        return super.describeCollectionView(databaseName, collectionName);
    }

    @Override
    public AffectRes dropCollectionView(String databaseName, String collectionName) {
        return super.dropCollectionView(databaseName, collectionName);
    }

    @Override
    public List<DocumentSet> queryAIDocument(CollectionViewQueryParamInner queryParamInner) {
        return super.queryAIDocument(queryParamInner);
    }

    @Override
    public AffectRes deleteAIDocument(CollectionViewDeleteParamInner deleteParamInner) {
        return super.deleteAIDocument(deleteParamInner);
    }

    @Override
    public SearchContentRes searchAIDocument(SearchDocParamInner searchDocParamInner) {
        return super.searchAIDocument(searchDocParamInner);
    }

    @Override
    public AffectRes updateAIDocument(CollectionViewUpdateParamInner updateParamInner) {
        return super.updateAIDocument(updateParamInner);
    }

    @Override
    public void upload(String databaseName, String collectionName, LoadAndSplitTextParam loadAndSplitTextParam, Map<String, Object> metaDataMap) throws Exception {
        super.upload(databaseName, collectionName, loadAndSplitTextParam, metaDataMap);
    }

    @Override
    public GetDocumentSetRes getFile(String databaseName, String collectionName, String fileName, String fileId) {
        return super.getFile(databaseName, collectionName, fileName, fileId);
    }

    @Override
    public GetChunksRes getChunks(String databaseName, String collectionName, String documentSetName, String documentSetId, Integer limit, Integer offset) {
        return super.getChunks(databaseName, collectionName, documentSetName, documentSetId, limit, offset);
    }

    @Override
    public BaseRes rebuildAIIndex(RebuildIndexParamInner param) {
        return super.rebuildAIIndex(param);
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
            if(indexField.getFieldType()== FieldType.Array){
                indexField.setFieldElementType(FieldElementType.fromValue(entry.getValue().getFieldElementType()));
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
            docBuilder.addAllVector((document.getVector()).stream().map(vecEle -> vecEle.floatValue()).collect(Collectors.toList()));
        }
        document.getDocFields().forEach(docField -> {
            Olama.Field.Builder fieldBuilder = Olama.Field.newBuilder();
            if (docField.getValue() instanceof Integer || docField.getValue() instanceof Long) {
                fieldBuilder.setValU64(Long.parseLong(docField.getValue().toString()));
            } else if (docField.getValue() instanceof Double || docField.getValue() instanceof Float) {
                fieldBuilder.setValDouble(Double.parseDouble(docField.getValue().toString()));
            }else if(docField.getValue() instanceof String){
                fieldBuilder.setValStr(ByteString.copyFromUtf8(docField.getValue().toString()));
            }
            if (docField.getValue() instanceof List) {
                fieldBuilder.setValStrArr(Olama.Field.StringArray.newBuilder().addAllStrArr(
                        ((List<?>) docField.getValue()).stream().map(ele-> ByteString.copyFromUtf8((String)ele)).collect(Collectors.toList())));
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
            }else {
                Olama.Field.Builder fieldBuilder = Olama.Field.newBuilder();
                if (document.get(key) instanceof Integer || document.get(key) instanceof Long) {
                    fieldBuilder.setValU64(Long.parseLong(document.get(key).toString()));
                } else if (document.get(key) instanceof Double || document.get(key) instanceof Float) {
                    fieldBuilder.setValDouble(Double.parseDouble(document.get(key).toString()));
                } else if (document.get(key) instanceof String) {
                    fieldBuilder.setValStr(ByteString.copyFromUtf8(document.get(key).toString()));
                }else if(document.get(key) instanceof JSONArray){
                    fieldBuilder.setValStrArr(Olama.Field.StringArray.newBuilder().addAllStrArr(((JSONArray)document.get(key)).toList().stream().map(ele-> ByteString.copyFromUtf8((String)ele)).collect(Collectors.toList())));
                }
                docBuilder.putFields(key, fieldBuilder.build());
            }
        });
        return docBuilder.build();
    }

    private static Document convertDocument(Olama.Document document) {
        Document.Builder builder =  Document.newBuilder().withId(document.getId());
        if (document.getScore()>0){
            builder.withScore(Double.valueOf(document.getScore()));
        }
        if (document.getVectorCount()>0){
            builder.withVector(document.getVectorList().stream().map(ele->ele.doubleValue()).collect(Collectors.toList()));
        }
        if(document.getFieldsMap()!=null){
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

