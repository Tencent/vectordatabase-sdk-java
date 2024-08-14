package com.tencent.tcvdb.service;

import com.google.protobuf.ByteString;
import com.tencent.tcvdb.exception.VectorDBException;
import com.tencent.tcvdb.model.Collection;
import com.tencent.tcvdb.model.Database;
import com.tencent.tcvdb.model.DocField;
import com.tencent.tcvdb.model.Document;
import com.tencent.tcvdb.model.param.collection.*;
import com.tencent.tcvdb.model.param.database.ConnectParam;
import com.tencent.tcvdb.model.param.dml.*;
import com.tencent.tcvdb.model.param.entity.*;
import com.tencent.tcvdb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvdb.rpc.client.AuthorityInterceptor;
import com.tencent.tcvdb.rpc.client.BackendServiceInterceptor;
import com.tencent.tcvdb.rpc.proto.Olama;
import com.tencent.tcvdb.rpc.proto.SearchEngineGrpc;
import com.tencent.tcvdb.service.param.*;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class GrpcStub implements Stub{
    private ManagedChannel channel;
    private SearchEngineGrpc.SearchEngineBlockingStub blockingStub;

    private Map<String, String> headers;
    private final int maxSendMessageSize = 100*1024*1024;
    private final int maxReceiveMessageSize = 100*1024*1024;
    private String authorization;
    private int timeout = 10;
    private static final Logger logger = LoggerFactory.getLogger(GrpcStub.class.getName());
    public GrpcStub(ConnectParam param) {
        this.authorization = String.format("Bearer account=%s&api_key=%s",param.getUsername(), param.getKey());

        this.channel = ManagedChannelBuilder.forTarget(param.getUrl()).
                intercept(new AuthorityInterceptor(this.authorization)).
                usePlaintext().build();
        Metadata headers = new Metadata();
        headers.put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER), this.authorization);
        this.blockingStub = SearchEngineGrpc.newBlockingStub(this.channel);
        this.blockingStub.withMaxInboundMessageSize(maxReceiveMessageSize);
        this.blockingStub.withMaxOutboundMessageSize(maxSendMessageSize);
        this.timeout = timeout;
    }

    @Override
    public void createDatabase(Database database) {
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(false));
        Olama.DatabaseResponse response = searchEngineBlockingStub.createDatabase(Olama.DatabaseRequest.newBuilder().
                setDatabase(database.getDatabaseName()).build());
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer create Database error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
    }

    @Override
    public void dropDatabase(Database database) {
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(false));
        Olama.DatabaseRequest request = Olama.DatabaseRequest.newBuilder().setDatabase(database.getDatabaseName()).build();
        Olama.DatabaseResponse response =  searchEngineBlockingStub.dropDatabase(request);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer drop Database error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
    }

    @Override
    public List<String> listDatabases() {
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(false));

        Olama.DatabaseRequest request = Olama.DatabaseRequest.newBuilder().build();
        Olama.DatabaseResponse response = searchEngineBlockingStub.listDatabases(request);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer list Database error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return response.getDatabasesList();

    }

    @Override
    public DataBaseInfoRes listDatabaseInfos() {
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(false));

        Olama.DatabaseRequest request = Olama.DatabaseRequest.newBuilder().build();
        Olama.DatabaseResponse response =  searchEngineBlockingStub.listDatabases(request);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer list Database error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        DataBaseInfoRes dataBaseInfoRes = new DataBaseInfoRes(response.getCode(), response.getMsg(), "");
        dataBaseInfoRes.setDatabases(response.getDatabasesList());
        dataBaseInfoRes.setInfo(response.getInfoMap().entrySet().stream().map(entry->
                        {
                            DataBaseInfo dataBaseInfo = new DataBaseInfo();
                            dataBaseInfo.setDatabaseName(entry.getKey());
                            Date date = new Date(entry.getValue().getCreateTime());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            dataBaseInfo.setCreateTime(sdf.format(date));
                            return dataBaseInfo;
                        }).collect(Collectors.toList()));
        return dataBaseInfoRes;
    }

    @Override
    public void createCollection(CreateCollectionParam params, boolean ai) {
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(false));
        Olama.CreateCollectionRequest.Builder requestOrBuilder = Olama.CreateCollectionRequest.newBuilder()
                .setShardNum(params.getShardNum())
                .setReplicaNum(params.getReplicaNum())
                .setCollection(params.getCollection())
                .setDatabase(params.getDatabase())
                .setDescription(params.getDescription());
        if (params.getAlias()!=null){
            requestOrBuilder.addAllAliasList(params.getAlias());
        }
        if (params.getWordsEmbedding()!=null){
            requestOrBuilder.setWordsEmbedding(Olama.CreateCollectionRequest.WordsEmbedding.newBuilder()
                    .setAllowEmpty(params.getWordsEmbedding().getAllowEmpty())
                    .setEmptyInputRerank(params.getWordsEmbedding().getEmptyInputRerank())
                    .setFieldName(params.getWordsEmbedding().getFieldName())
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

        Olama.CreateCollectionResponse response =  searchEngineBlockingStub.createCollection(requestOrBuilder.build());
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
    public List<Collection> listCollections(String databaseName) {
        Olama.ListCollectionsRequest request = Olama.ListCollectionsRequest.newBuilder().
                setDatabase(databaseName).setTransfer(false).build();
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(false));
        Olama.ListCollectionsResponse response = searchEngineBlockingStub.listCollections(request);
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
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(false));
        Olama.DescribeCollectionResponse describeCollectionResponse = searchEngineBlockingStub.describeCollection(Olama.DescribeCollectionRequest.newBuilder()
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
    public AffectRes truncateCollection(String databaseName, String collectionName, boolean ai) {
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(ai));
        Olama.TruncateCollectionResponse truncateCollectionResponse = searchEngineBlockingStub.truncateCollection(Olama.TruncateCollectionRequest.newBuilder()
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
                "", 0);
    }

    @Override
    public void dropCollection(String databaseName, String collectionName, boolean ai) {
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(ai));
        Olama.DropCollectionResponse dropCollectionResponse = searchEngineBlockingStub.dropCollection(Olama.DropCollectionRequest.newBuilder().
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
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(false));
        Olama.UpdateAliasResponse setAliasResponse = searchEngineBlockingStub.setAlias(Olama.AddAliasRequest.newBuilder().setDatabase(databaseName)
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
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(false));
        Olama.UpdateAliasResponse deleteResponse = searchEngineBlockingStub.deleteAlias(Olama.RemoveAliasRequest.newBuilder().
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
    public AffectRes upsertDocument(InsertParamInner param, boolean ai) {
        Olama.UpsertRequest.Builder builder = Olama.UpsertRequest.newBuilder()
                .setDatabase(param.getDatabase()).setCollection(param.getCollection());
        if (param.getBuildIndex()==null){
            builder.setBuildIndex(true);
        }else {
            builder.setBuildIndex(param.getBuildIndex());
        }
        for (Document document : param.getDocuments()) {
            Olama.Document doc= convertDocument2OlamaDoc(document);
            builder.addDocuments(doc);
        }
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(ai));
        Olama.UpsertResponse response = searchEngineBlockingStub.upsert(builder.build());
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer upsert data error: not Successful, code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return new AffectRes(response.getCode(), response.getMsg(), response.getWarning(), response.getAffectedCount());
    }

    @Override
    public List<Document> queryDocument(QueryParamInner param, boolean ai) {
        Olama.QueryRequest.Builder queryBuilder = Olama.QueryRequest.newBuilder().
                setDatabase(param.getDatabase()).setCollection(param.getCollection()).setReadConsistency(ReadConsistencyEnum.EVENTUAL_CONSISTENCY.getReadConsistency());
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
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(ai));
        Olama.QueryResponse queryResponse = searchEngineBlockingStub.query(queryBuilder.build());
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
    public SearchRes searchDocument(SearchParamInner param, boolean ai) {
        Olama.SearchRequest.Builder builder = Olama.SearchRequest.newBuilder().setDatabase(param.getDatabase()).
                setCollection(param.getCollection()).
                setReadConsistency(ReadConsistencyEnum.EVENTUAL_CONSISTENCY.getReadConsistency());
        SearchParam searchParam = param.getSearch();
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
                                annBuilder.addAllDataExpr(annOption.getData().stream().map(item->(String)item).collect(Collectors.toList()));
                            }if (annOption.getData().get(0) instanceof List){
                                annBuilder.addAllData(annOption.getData().stream()
                                        .map(item-> Olama.VectorArray.newBuilder().addAllVector(((List<Object>)item).
                                                stream().map(ele->Float.parseFloat(ele.toString())).collect(Collectors.toList())).build())
                                        .collect(Collectors.toList()));
                            }
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
                                    setScore((float)vectors.get(1)).build()).collect(Collectors.toList())).build());
                });
                sparseBuilder.setLimit(matchOption.getLimit());
                return sparseBuilder.build();
            }).collect(Collectors.toList()));
        }

        builder.setSearch(searchConBuilder.build());
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(ai));
        Olama.SearchResponse searchResponse = searchEngineBlockingStub.search(builder.build());
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
    public AffectRes deleteDocument(DeleteParamInner param, boolean ai) {
        Olama.QueryCond.Builder queryCondBuilder = Olama.QueryCond.newBuilder();
        DeleteParam paramQuery = param.getQuery();
        if(!paramQuery.getDocumentIds().isEmpty()){
            queryCondBuilder.addAllDocumentIds(paramQuery.getDocumentIds());
        }
        if (!paramQuery.getFilter().isEmpty()){
            queryCondBuilder.setFilter(paramQuery.getFilter());
        }
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(ai));
        Olama.DeleteResponse deleteResponse = searchEngineBlockingStub.dele(Olama.DeleteRequest.newBuilder()
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
    public AffectRes updateDocument(UpdateParamInner param, boolean ai) {
        Olama.QueryCond.Builder queryCondBuilder = Olama.QueryCond.newBuilder();
        UpdateParam paramQuery = param.getQuery();
        if(!paramQuery.getDocumentIds().isEmpty()){
            queryCondBuilder.addAllDocumentIds(paramQuery.getDocumentIds());
        }
        if (!paramQuery.getFilter().isEmpty()){
            queryCondBuilder.setFilter(paramQuery.getFilter());
        }
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(ai));
        Olama.UpdateResponse updateResponse = searchEngineBlockingStub.update(Olama.UpdateRequest.newBuilder()
                .setDatabase(param.getDatabase())
                .setCollection(param.getCollection())
                .setQuery(queryCondBuilder.build())
                .setUpdate(convertDocument2OlamaDoc(param.getUpdate()))
                .build());
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
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = this.blockingStub.withInterceptors(new BackendServiceInterceptor(false));
        Olama.RebuildIndexResponse rebuildIndexResponse = searchEngineBlockingStub.rebuildIndex(Olama.RebuildIndexRequest.newBuilder()
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


    private static Collection convertRpcToCollection(Olama.CreateCollectionRequest collection) {
        Collection collectionInner = new Collection();
        collectionInner.setDatabase(collection.getDatabase());
        collectionInner.setCollection(collection.getCollection());
        if(collection.getWordsEmbedding()!=null && !collection.getWordsEmbedding().getFieldName().isEmpty()){
            collectionInner.setWordsEmbedding(new WordsEmbeddingParam(collection.getWordsEmbedding().getAllowEmpty(), collection.getWordsEmbedding().getFieldName(),
                    collection.getWordsEmbedding().getEmptyInputRerank()));
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
            if(document.getVector() instanceof List){
                docBuilder.addAllVector(((List<Double>)document.getVector()).stream().map(vecEle -> Float.parseFloat(vecEle.toString())).collect(Collectors.toList()));
            } else if (document.getVector() instanceof String) {
                docBuilder.setDataExpr((String) document.getVector());
            }
        }
        if(document.getSparseVector()!=null && document.getSparseVector().size()>0){
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
            }
            if (docField.getValue() instanceof List) {
                fieldBuilder.setValStrArr(Olama.Field.StringArray.newBuilder().addAllStrArr(
                        ((List<?>) docField.getValue()).stream().map(ele-> ByteString.copyFromUtf8((String)ele)).collect(Collectors.toList())));
            }
            docBuilder.putFields(docField.getName(), fieldBuilder.build());
        });
        return docBuilder.build();
    }

    private static Document convertDocument(Olama.Document document) {
        Document.Builder builder =  Document.newBuilder().withId(document.getId()).
                withScore((double) document.getScore());
        if (document.getVectorCount()>0){
            builder.withVectorByList(Collections.singletonList(document.getVectorList()));
        }
        if(document.getSparseVectorCount()>0){
            builder.withSparseVectorList(document.getSparseVectorList().stream().
                    map(sparseVecItem -> Arrays.asList(sparseVecItem.getTermId(), sparseVecItem.getScore())).collect(Collectors.toList()));
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
                    builder.addDocField(new DocField(stringFieldEntry.getKey(), new String(stringFieldEntry.getValue().toByteArray(), StandardCharsets.UTF_8)));
                }
                if (stringFieldEntry.getValue().hasValStrArr()){
                    builder.addDocField(new DocField(stringFieldEntry.getKey(),
                            stringFieldEntry.getValue().getValStrArr().getStrArrList().stream().map(ele->
                                new String(stringFieldEntry.getValue().toByteArray(), StandardCharsets.UTF_8)
                            ).collect(Collectors.toList())));
                }
            }
        }
        if (document.hasContextResults()){
            builder.withContextResult(ContextResult.newBuilder()
                    .withPre(document.getContextResults().getPreList())
                    .withNext(document.getContextResults().getNextList())
                    .withSourceInfo(SourceInfo.newBuilder().with_vdc_source_name(document.getContextResults().getSourceInfo().getVdcSourceName())
                            .with_vdc_source_path(document.getContextResults().getSourceInfo().getVdcSourcePath())
                            .with_vdc_source_type(document.getContextResults().getSourceInfo().getVdcSourceType()).build())
                    .build());
        }
        return builder.build();
    }
}
