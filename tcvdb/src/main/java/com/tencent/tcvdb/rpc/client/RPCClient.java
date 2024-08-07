package com.tencent.tcvdb.rpc.client;

import com.google.protobuf.ByteString;
import com.tencent.tcvdb.exception.VectorDBException;
import com.tencent.tcvdb.model.Collection;
import com.tencent.tcvdb.model.DocField;
import com.tencent.tcvdb.model.Document;
import com.tencent.tcvdb.model.param.collection.*;
import com.tencent.tcvdb.model.param.dml.*;
import com.tencent.tcvdb.model.param.entity.AffectRes;
import com.tencent.tcvdb.model.param.entity.BaseRes;
import com.tencent.tcvdb.model.param.entity.SearchRes;
import com.tencent.tcvdb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvdb.rpc.model.CollectionRpc;
import com.tencent.tcvdb.rpc.model.DatabaseRpc;
import com.tencent.tcvdb.rpc.proto.Olama;
import com.tencent.tcvdb.rpc.proto.SearchEngineGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;

import java.util.*;
import java.util.stream.Collectors;

public class RPCClient {
    private  ManagedChannel channel;
    private SearchEngineGrpc.SearchEngineBlockingStub blockingStub;

    private Map<String, String> headers;
    private final int maxSendMessageSize = 100*1024*1024;
    private final int maxReceiveMessageSize = 100*1024*1024;
    private String authorization;
    private int timeout = 10;

    private final ReadConsistencyEnum readConsistency;
    public RPCClient(String url, String username, String apikey, ReadConsistencyEnum readConsistency, Integer timeout) {

        this.authorization = String.format("Bearer account=%s&api_key=%s", username, apikey);
        this.channel = ManagedChannelBuilder.forTarget(url).
                intercept(new AuthorityInterceptor(this.authorization)).
                usePlaintext().build();
        Metadata headers = new Metadata();
        headers.put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER), this.authorization);
        this.blockingStub = SearchEngineGrpc.newBlockingStub(this.channel);
        this.blockingStub.withMaxInboundMessageSize(maxReceiveMessageSize);
        this.blockingStub.withMaxOutboundMessageSize(maxSendMessageSize);
        this.timeout = timeout;
        this.readConsistency = readConsistency;
    }
    public void close() {
        channel.shutdown();
    }
    public AffectRes upsert(String database, String collection, InsertParam param) {
        Olama.UpsertRequest.Builder builder = Olama.UpsertRequest.newBuilder()
                .setDatabase(database).setCollection(collection);
        if (param.isBuildIndex()==null){
            builder.setBuildIndex(true);
        }else {
            builder.setBuildIndex(param.isBuildIndex());
        }
        for (Document document : param.getDocuments()) {
            Olama.Document doc= convertDocument2OlamaDoc(document);
            builder.addDocuments(doc);
        }
        Olama.UpsertResponse response = this.blockingStub.upsert(builder.build());
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer upsert data error: not Successful, code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return new AffectRes(response.getCode(), response.getMsg(), response.getWarning(), response.getAffectedCount());
    }

    private static Olama.Document convertDocument2OlamaDoc(Document document) {
        Olama.Document.Builder docBuilder = Olama.Document.newBuilder();
        if (!document.getId().isEmpty()){
            docBuilder.setId(document.getId());
        }
        if (!document.getVector().isEmpty()){
            if(document.getVector().get(0) instanceof Number){
                docBuilder.addAllVector(document.getVector().stream().map(vecEle -> (Float)vecEle).collect(Collectors.toList()));
            } else if (document.getVector().get(0) instanceof String) {
                docBuilder.setDataExpr((String) document.getVector().get(0));
            }
        }
        if(!document.getSparseVector().isEmpty()){
            docBuilder.addAllSparseVector(document.getSparseVector().stream()
                    .map(pair->Olama.SparseVecItem.newBuilder().setTermId(pair.getKey()).setScore(pair.getValue().floatValue()).build())
                    .collect(Collectors.toList()));
        }
        document.getDocFields().forEach(docField -> {
            Olama.Field.Builder fieldBuilder = Olama.Field.newBuilder();
            if (docField.getValue() instanceof Integer || docField.getValue() instanceof Long) {
                fieldBuilder.setValU64((Long) docField.getValue());
            } else if (docField.getValue() instanceof Double || docField.getValue() instanceof Float) {
                fieldBuilder.setValDouble((Double) docField.getValue());
            }
            if (docField.getValue() instanceof List) {
                fieldBuilder.setValStrArr(Olama.Field.StringArray.newBuilder().addAllStrArr(
                        ((List<?>) docField.getValue()).stream().map(ele-> ByteString.copyFromUtf8((String)ele)).collect(Collectors.toList())));
            }
            docBuilder.putFields(docField.getName(), fieldBuilder.build());
        });
        return docBuilder.build();
    }

    public DatabaseRpc createDatabase(String database) {
        Olama.DatabaseResponse response = this.blockingStub.createDatabase(Olama.DatabaseRequest.newBuilder().setDatabase(database).build());
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer create Database error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return new DatabaseRpc(this, database, readConsistency);
    }

    public List<String> listDatabases(String database, int timeout, boolean ai) {
        Olama.DatabaseRequest request = Olama.DatabaseRequest.newBuilder().setDatabase(database).build();
        Olama.DatabaseResponse response =  this.blockingStub.listDatabases(request);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer list Database error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return response.getDatabasesList();
    }

    public List<CollectionRpc> listCollections(String database, boolean ai) {
        Olama.ListCollectionsRequest request = Olama.ListCollectionsRequest.newBuilder().
                setDatabase(database).setTransfer(false).build();
        Olama.ListCollectionsResponse response =  this.blockingStub.listCollections(request);
        List<CollectionRpc> collections = new ArrayList<>();
        response.getCollectionsList().forEach(collection -> {
            CollectionRpc collectionRpc = convertRpcToCollection(collection);
            collectionRpc.setClient(this);
            collections.add(collectionRpc);
        });
        return collections;
    }

    private static CollectionRpc convertRpcToCollection(Olama.CreateCollectionRequest collection) {
        CollectionRpc collectionRpc = new CollectionRpc(collection.getDatabase(), collection.getCollection());
        if(collection.getWordsEmbedding()!=null && collectionRpc.getWordsEmbedding().getFieldName()!=null){
            collectionRpc.setWordsEmbedding(new WordsEmbeddingParam(collection.getWordsEmbedding().getAllowEmpty(), collection.getWordsEmbedding().getFieldName(),
                    collection.getWordsEmbedding().getEmptyInputRerank()));
        }
        collectionRpc.setDescription(collection.getDescription());
        collectionRpc.setAlias(collection.getAliasListList());
        collectionRpc.setCreateTime(collection.getCreateTime());
        collectionRpc.setShardNum(collection.getShardNum());
        collectionRpc.setReplicaNum(collection.getReplicaNum());
        collectionRpc.setIndexStatus(new Collection.IndexStatus(collection.getIndexStatus().getStatus(), collection.getIndexStatus().getStartTime()));
        collection.getIndexesMap().entrySet().forEach(entry->{
            IndexField indexField = new IndexField();
            indexField.setFieldName(entry.getValue().getFieldName());
            indexField.setFieldType(FieldType.valueOf(entry.getValue().getFieldType()));
            indexField.setIndexType(IndexType.valueOf(entry.getValue().getIndexType()));
            if (indexField.isVectorField()){
                indexField.setMetricType(MetricType.valueOf(entry.getValue().getMetricType()));
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
                indexField.setFieldElementType(FieldElementType.valueOf(entry.getValue().getFieldElementType()));
            }
        });
        return collectionRpc;
    }

    public CollectionRpc createCollection(CreateCollectionParam params, boolean ai) {
        Olama.CreateCollectionRequest.Builder requestOrBuilder = Olama.CreateCollectionRequest.newBuilder()
                .setShardNum(params.getShardNum())
                .setReplicaNum(params.getReplicaNum())
                .setCollection(params.getCollection())
                .setDatabase(params.getDatabase())
                .setDescription(params.getDescription())
                .addAllAliasList(params.getAlias());
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
                    indexBuilder.setMetricType(indexBuilder.getMetricType());
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
                if(index.getFieldElementType()!=null){
                    indexBuilder.setFieldElementType(index.getFieldElementType().getValue());
                }

                requestOrBuilder.putIndexes(index.getFieldName(), indexBuilder.build());
            }

        }

        Olama.CreateCollectionResponse response =  this.blockingStub.createCollection(requestOrBuilder.build());
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return convertRpcToCollection(requestOrBuilder.build());
    }

    public AffectRes truncateCollections(String databaseName, String collectionName) {
        Olama.TruncateCollectionResponse truncateCollectionResponse = blockingStub.truncateCollection(Olama.TruncateCollectionRequest.newBuilder()
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

    public CollectionRpc describeCollection(String databaseName, String collectionName) {
        Olama.DescribeCollectionResponse describeCollectionResponse = blockingStub.describeCollection(Olama.DescribeCollectionRequest.newBuilder()
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


    public void dropCollection(String databaseName, String collectionName) {
        Olama.DropCollectionResponse dropCollectionResponse = blockingStub.dropCollection(Olama.DropCollectionRequest.newBuilder().
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

    public AffectRes setAlias(String databaseName, String collectionName, String aliasName) {
        Olama.UpdateAliasResponse setAliasResponse = blockingStub.setAlias(Olama.AddAliasRequest.newBuilder().setDatabase(databaseName)
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

    public AffectRes deleteAlias(String databaseName, String aliasName) {
        Olama.UpdateAliasResponse deleteResponse = blockingStub.deleteAlias(Olama.RemoveAliasRequest.newBuilder().
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

    public List<Document> query(String database, String collection, QueryParam param) {
        Olama.QueryRequest.Builder queryBuilder = Olama.QueryRequest.newBuilder().
                setDatabase(database).setCollection(collection).setReadConsistency(ReadConsistencyEnum.EVENTUAL_CONSISTENCY.getReadConsistency());
        Olama.QueryCond.Builder queryCondBuilder = Olama.QueryCond.newBuilder().setRetrieveVector(param.isRetrieveVector())
                        .setLimit(param.getLimit()).setOffset(param.getOffset());
        if(!param.getDocumentIds().isEmpty()){
            queryCondBuilder.addAllDocumentIds(param.getDocumentIds());
        }
        if(!param.getOutputFields().isEmpty()){
            queryCondBuilder.addAllOutputFields(param.getOutputFields());
        }
        if (!param.getFilter().isEmpty()){
            queryCondBuilder.setFilter(param.getFilter());
        }

        queryBuilder.setQuery(queryCondBuilder.build());
        Olama.QueryResponse queryResponse = blockingStub.query(queryBuilder.build());
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
        if(!document.getFieldsMap().isEmpty()){
            for (Map.Entry<String, Olama.Field> stringFieldEntry : document.getFieldsMap().entrySet()) {
                if (stringFieldEntry.getValue().hasValDouble()){
                    builder.addDocField(new DocField(stringFieldEntry.getKey(), Double.parseDouble(stringFieldEntry.getValue().toString())));
                }
                if (stringFieldEntry.getValue().hasValU64()){
                    builder.addDocField(new DocField(stringFieldEntry.getKey(), Long.parseLong(stringFieldEntry.getValue().toString())));
                }
                if (stringFieldEntry.getValue().hasValStr()){
                    builder.addDocField(new DocField(stringFieldEntry.getKey(), stringFieldEntry.getValue().toString()));
                }
                if (stringFieldEntry.getValue().hasValStrArr()){
                    builder.addDocField(new DocField(stringFieldEntry.getKey(),
                            stringFieldEntry.getValue().getValStrArr().getStrArrList().stream().map(ByteString::toString).collect(Collectors.toList())));
                }
            }
        }
        return builder.build();
    }

    public SearchRes search(String database, String collection, SearchParam param) {
        Olama.SearchRequest.Builder builder = Olama.SearchRequest.newBuilder().setDatabase(database).
                setCollection(collection).
                setReadConsistency(ReadConsistencyEnum.EVENTUAL_CONSISTENCY.getReadConsistency());
        Olama.SearchCond.Builder searchConBuilder = Olama.SearchCond.newBuilder()
                .setRetrieveVector(param.isRetrieveVector()).setLimit(param.getLimit());
        if (!param.getOutputFields().isEmpty()){
            searchConBuilder.addAllOutputfields(param.getOutputFields());
        }
        if (!param.getFilter().isEmpty()){
            searchConBuilder.setFilter(param.getFilter());
        }
        if (param.getAnn()!=null && !param.getAnn().isEmpty()){
            searchConBuilder.addAllAnn(param.getAnn().stream()
                    .map(annOption -> {
                        Olama.AnnData.Builder annBuilder = Olama.AnnData.newBuilder()
                                .setFieldName(annOption.getFieldName());
                        if (!annOption.getDocumentIds().isEmpty()){
                            annBuilder.addAllDocumentIds(annOption.getDocumentIds());
                        }
                        if (!annOption.getData().isEmpty()){
                            if (annOption.getData().get(0) instanceof String){
                                annBuilder.addAllDataExpr(annOption.getData().stream().map(item->(String)item).collect(Collectors.toList()));
                            }if (annOption.getData().get(0) instanceof List){
                                annBuilder.addAllData(annOption.getData().stream()
                                        .map(item-> Olama.VectorArray.newBuilder().addAllVector((List<Float>)item).build())
                                        .collect(Collectors.toList()));
                            }
                        }
                        if (annOption.getParams()!=null){
                            GeneralParams generalParams = (GeneralParams) annOption.getParams();
                            annBuilder.setParams(Olama.SearchParams.newBuilder().setEf(generalParams.getEf())
                                    .setNprobe(generalParams.getNProbe())
                                    .setRadius((float) generalParams.getRadius()).build());
                        }
                        return annBuilder.build();
                    }).collect(Collectors.toList()));
        }
        builder.setSearch(searchConBuilder.build());
        Olama.SearchResponse searchResponse = blockingStub.search(builder.build());
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
            documentsList.add(searchResult.getDocumentsList().stream().map(RPCClient::convertDocument)
                    .collect(Collectors.toList()));
        }
        return new SearchRes(searchResponse.getCode(),searchResponse.getMsg(), searchResponse.getWarning(), documentsList);
    }

    public AffectRes delete(String database, String collection, DeleteParam param) {
        Olama.QueryCond.Builder queryCondBuilder = Olama.QueryCond.newBuilder();
        if(!param.getDocumentIds().isEmpty()){
            queryCondBuilder.addAllDocumentIds(param.getDocumentIds());
        }
        if (!param.getFilter().isEmpty()){
            queryCondBuilder.setFilter(param.getFilter());
        }
        Olama.DeleteResponse deleteResponse = blockingStub.dele(Olama.DeleteRequest.newBuilder()
                .setDatabase(database)
                .setCollection(collection)
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

    public AffectRes update(String database, String collection, UpdateParam param, Document document) {
        Olama.QueryCond.Builder queryCondBuilder = Olama.QueryCond.newBuilder();
        if(!param.getDocumentIds().isEmpty()){
            queryCondBuilder.addAllDocumentIds(param.getDocumentIds());
        }
        if (!param.getFilter().isEmpty()){
            queryCondBuilder.setFilter(param.getFilter());
        }
        Olama.UpdateResponse updateResponse = blockingStub.update(Olama.UpdateRequest.newBuilder()
                .setDatabase(database)
                .setCollection(collection)
                .setQuery(queryCondBuilder.build())
                .setUpdate(convertDocument2OlamaDoc(document))
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

    public BaseRes rebuildIndex(String database, String collection, RebuildIndexParam rebuildIndexParam) {
        Olama.RebuildIndexResponse rebuildIndexResponse = blockingStub.rebuildIndex(Olama.RebuildIndexRequest.newBuilder()
                .setDatabase(database)
                .setCollection(collection)
                .setThrottle(rebuildIndexParam.getThrottle())
                .setDropBeforeRebuild(rebuildIndexParam.dropBeforeRebuild())
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
}
