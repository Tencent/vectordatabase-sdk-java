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

import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.Collection;
import com.tencent.tcvectordb.model.CollectionView;
import com.tencent.tcvectordb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvectordb.model.param.collectionView.CreateCollectionViewParam;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.rpc.proto.Olama;
import com.tencent.tcvectordb.rpc.proto.SearchEngineGrpc;
import com.tencent.tcvectordb.service.ApiPath;
import com.tencent.tcvectordb.service.Stub;
import io.grpc.ManagedChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * gRPC service implementation for collection operations.
 */
public class CollectionGrpcService extends BaseGrpcService {
    
    private Stub stub;  // Used for setting stub reference in collections

    public void setStub(Stub stub) {
        this.stub = stub;
    }

    /**
     * Create a new collection with specified parameters.
     */
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
            params.getIndexes().forEach(index -> {
                Olama.IndexColumn.Builder indexBuilder = getRpcIndexBuilder(index);
                requestOrBuilder.putIndexes(index.getFieldName(), indexBuilder.build());
            });
        }
        logQuery(ApiPath.COL_CREATE, requestOrBuilder);
        ManagedChannel channel = channelPool.getChannel();
        Olama.CreateCollectionResponse response;
        response = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
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

    /**
     * Create a new AI collection view.
     * Note: Collection view operations are not supported via gRPC and must use HTTP fallback.
     */
    public void createCollectionView(CreateCollectionViewParam params) {
        throw new UnsupportedOperationException("Collection view operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * List all collections in the specified database.
     */
    public List<Collection> listCollections(String databaseName) {
        Olama.ListCollectionsRequest.Builder requestBuilder = Olama.ListCollectionsRequest.newBuilder().setTransfer(false);
        if (databaseName!=null){
            requestBuilder.setDatabase(databaseName);
        }
        logQuery(ApiPath.COL_LIST, requestBuilder.build());
        ManagedChannel channel = channelPool.getChannel();
        Olama.ListCollectionsResponse response;
        response = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                .listCollections(requestBuilder.build());

        logResponse(ApiPath.COL_LIST, response);
        List<Collection> collections = new ArrayList<>();
        response.getCollectionsList().forEach(collection -> {
            Collection collectionRpc = convertRpcToCollection(collection);
            collectionRpc.setStub(this.stub);
            collections.add(collectionRpc);
        });
        return collections;
    }

    /**
     * Get detailed information about a specific collection.
     */
    public Collection describeCollection(String databaseName, String collectionName) {
        Olama.DescribeCollectionRequest.Builder requestBuilder = Olama.DescribeCollectionRequest.newBuilder();
        if (databaseName!=null){
            requestBuilder.setDatabase(databaseName);
        }
        if (collectionName!=null){
            requestBuilder.setCollection(collectionName);
        }
        logQuery(ApiPath.COL_DESCRIBE, requestBuilder.build());
        Olama.DescribeCollectionResponse describeCollectionResponse;
        ManagedChannel channel = channelPool.getChannel();
        describeCollectionResponse = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
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

    /**
     * Remove all documents from a collection without dropping the collection.
     */
    public AffectRes truncateCollection(String databaseName, String collectionName, DataBaseTypeEnum dbType) {
       Olama.TruncateCollectionRequest.Builder requestBuilder = Olama.TruncateCollectionRequest.newBuilder();
        if (databaseName!=null){
            requestBuilder.setDatabase(databaseName);
        }
        if (collectionName!=null){
            requestBuilder.setCollection(collectionName);
        }
        logQuery(ApiPath.COL_FLUSH, requestBuilder.build());
        ManagedChannel channel = channelPool.getChannel();
        Olama.TruncateCollectionResponse truncateCollectionResponse;
        truncateCollectionResponse = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                .truncateCollection(requestBuilder.build());

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

    /**
     * Remove all documents from a collection view without dropping it.
     * Note: Collection view operations are not supported via gRPC and must use HTTP fallback.
     */
    public AffectRes truncateCollectionView(String databaseName, String collectionName, DataBaseTypeEnum dbType) {
        throw new UnsupportedOperationException("Collection view operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * Drop an existing collection permanently.
     */
    public void dropCollection(String databaseName, String collectionName) {
        Olama.DropCollectionRequest.Builder requestBuilder = Olama.DropCollectionRequest.newBuilder();
        if (databaseName!=null){
            requestBuilder.setDatabase(databaseName);
        }
        if (collectionName!=null){
            requestBuilder.setCollection(collectionName);
        }
        logQuery(ApiPath.COL_DROP, requestBuilder.build());
        ManagedChannel channel = channelPool.getChannel();
        Olama.DropCollectionResponse dropCollectionResponse;
        dropCollectionResponse = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                .dropCollection(requestBuilder.build());

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

    /**
     * Set an alias name for a collection.
     */
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
        ManagedChannel channel = channelPool.getChannel();
        Olama.UpdateAliasResponse setAliasResponse;
        setAliasResponse = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                    .setAlias(requestBuilder.build());

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

    /**
     * Delete an existing collection alias.
     */
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
        ManagedChannel channel = channelPool.getChannel();
        Olama.UpdateAliasResponse deleteAliasResponse;
        deleteAliasResponse = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                .deleteAlias(request);
        logResponse(ApiPath.AI_ALIAS_DELETE, deleteAliasResponse);
        if(deleteAliasResponse==null){
            throw new VectorDBException("VectorDBServer error: dropCollection not response");
        }
        if (deleteAliasResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: deleteAlias not Success, body code=%s, message=%s",
                    deleteAliasResponse.getCode(), deleteAliasResponse.getMsg()));
        }
        return new AffectRes(deleteAliasResponse.getCode(), deleteAliasResponse.getMsg(),
                "", deleteAliasResponse.getAffectedCount());
    }

    /**
     * Set an alias name for an AI collection.
     * Note: AI collection operations are not supported via gRPC and must use HTTP fallback.
     */
    public AffectRes setAIAlias(String databaseName, String collectionName, String aliasName) {
        throw new UnsupportedOperationException("AI collection operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * Delete an existing AI collection alias.
     * Note: AI collection operations are not supported via gRPC and must use HTTP fallback.
     */
    public AffectRes deleteAIAlias(String databaseName, String aliasName) {
        throw new UnsupportedOperationException("AI collection operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * List all collection views in the specified database.
     * Note: Collection view operations are not supported via gRPC and must use HTTP fallback.
     */
    public List<CollectionView> listCollectionView(String databaseName) {
        throw new UnsupportedOperationException("Collection view operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * Get detailed information about a specific collection view.
     * Note: Collection view operations are not supported via gRPC and must use HTTP fallback.
     */
    public CollectionView describeCollectionView(String databaseName, String collectionName) {
        throw new UnsupportedOperationException("Collection view operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * Drop an existing collection view permanently.
     * Note: Collection view operations are not supported via gRPC and must use HTTP fallback.
     */
    public AffectRes dropCollectionView(String databaseName, String collectionName) {
        throw new UnsupportedOperationException("Collection view operations not supported via gRPC, use HTTP fallback");
    }
}
