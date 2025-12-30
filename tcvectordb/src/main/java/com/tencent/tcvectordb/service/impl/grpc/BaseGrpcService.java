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
package com.tencent.tcvectordb.service.impl.grpc;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.*;
import com.tencent.tcvectordb.model.param.collection.*;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.model.param.enums.EmbeddingModelEnum;
import com.tencent.tcvectordb.rpc.pool.ChannelPool;
import com.tencent.tcvectordb.rpc.proto.Olama;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Base gRPC service containing shared fields and methods for all module services.
 * All resources (channelPool, authorization, timeout) are injected from GrpcStub.
 */
public abstract class BaseGrpcService {
    protected ConnectParam connectParam;
    protected ChannelPool channelPool;
    protected String authorization;
    protected int timeout;
    protected int maxReceiveMessageSize;
    protected static final Logger logger = LoggerFactory.getLogger(BaseGrpcService.class.getName());

    /**
     * Initialize the gRPC service with shared resources from GrpcStub.
     *
     * @param connectParam Connection parameters
     * @param channelPool Shared channel pool for gRPC connections
     * @param authorization Authorization header string
     * @param timeout Request timeout in seconds
     * @param maxReceiveMessageSize Maximum message size
     */
    public void init(ConnectParam connectParam, ChannelPool channelPool, String authorization, 
                     int timeout, int maxReceiveMessageSize) {
        this.connectParam = connectParam;
        this.channelPool = channelPool;
        this.authorization = authorization;
        this.timeout = timeout;
        this.maxReceiveMessageSize = maxReceiveMessageSize;
    }

    /**
     * Log gRPC query request.
     */
    protected void logQuery(String url, MessageOrBuilder messageOrBuilder) {
        try {
            logger.debug("Query {}, request body={}", url, JsonFormat.printer().print(messageOrBuilder));
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Log gRPC response.
     */
    protected void logResponse(String url, MessageOrBuilder messageOrBuilder) {
        try {
            logger.debug("Query {}, response={}", url, JsonFormat.printer().print(messageOrBuilder));
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert gRPC Collection message to SDK Collection object.
     */
    protected Collection convertRpcToCollection(Olama.CreateCollectionRequest collection) {
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
                        case IVF_RABITQ:
                            indexField.setParams(new IVFRABITQParams(entry.getValue().getParams().getNlist(), entry.getValue().getParams().getBits()));
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
            // 设置 diskSwapEnabled 参数
            indexField.setDiskSwapEnabled(entry.getValue().getDiskSwapEnabled());
            return indexField;
        }).collect(Collectors.toList()));
        return collectionInner;
    }

    /**
     * Build gRPC IndexColumn from SDK IndexField.
     */
    protected Olama.IndexColumn.Builder getRpcIndexBuilder(IndexField index) {
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
                } else if (index.getParams() instanceof  IVFRABITQParams) {
                    IVFRABITQParams ivfrabitqParams = (IVFRABITQParams) index.getParams();
                    indexBuilder.setParams(Olama.IndexParams.newBuilder()
                            .setNlist(ivfrabitqParams.getNList()).setBits(ivfrabitqParams.getBits()).build());
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
        // 设置 diskSwapEnabled 参数
        indexBuilder.setDiskSwapEnabled(index.isDiskSwapEnabled());
        return indexBuilder;
    }

    /**
     * Convert SDK Document to gRPC Document.
     */
    protected Olama.Document convertDocument2OlamaDoc(Document document) {
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
                Long value = Long.parseLong(docField.getValue().toString());
                if (value < 0) {
                    fieldBuilder.setValInt64(value);
                } else {
                    fieldBuilder.setValU64(value);
                }
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

    /**
     * Convert JSONObject to gRPC Document.
     */
    protected Olama.Document convertDocumentJSON2OlamaDoc(JSONObject document) {
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
                    Long value = Long.parseLong(document.get(key).toString());
                    if (value < 0) {
                        fieldBuilder.setValInt64(value);
                    } else {
                        fieldBuilder.setValU64(value);
                    }
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
                    throw new VectorDBException("Unsupported field type, field: "+ key +" type:"+ document.get(key).getClass()
                            + "\nsupported field type is:  Integer,Long,Double,Float,String,JSONArray<String>,JSONObject");
                }
                docBuilder.putFields(key, fieldBuilder.build());
            }
        });
        return docBuilder.build();
    }

    /**
     * Convert gRPC Document to SDK Document.
     */
    protected Document convertDocument(Olama.Document document) {
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
                if (stringFieldEntry.getValue().hasValInt64()){
                    builder.addDocField(new DocField(stringFieldEntry.getKey(), stringFieldEntry.getValue().getValInt64()));
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
