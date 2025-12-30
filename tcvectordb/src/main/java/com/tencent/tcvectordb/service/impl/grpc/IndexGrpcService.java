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
import com.tencent.tcvectordb.model.param.entity.BaseRes;
import com.tencent.tcvectordb.rpc.proto.Olama;
import com.tencent.tcvectordb.rpc.proto.SearchEngineGrpc;
import com.tencent.tcvectordb.service.ApiPath;
import com.tencent.tcvectordb.service.param.*;
import io.grpc.ManagedChannel;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * gRPC service implementation for index operations.
 */
public class IndexGrpcService extends BaseGrpcService {

    /**
     * Rebuild the index for a collection to optimize search performance.
     */
    public BaseRes rebuildIndex(RebuildIndexParamInner param) {
        Olama.RebuildIndexRequest.Builder builder = Olama.RebuildIndexRequest.newBuilder();
        if (param.getDatabase()!=null){
            builder.setDatabase(param.getDatabase());
        }
        if (param.getCollection()!=null){
            builder.setCollection(param.getCollection());
        }
        if (param.getFieldName()!=null){
            builder.setFieldName(param.getFieldName());
        }
        Olama.RebuildIndexRequest rebuildIndexRequest = builder
                .setThrottle(param.getThrottle())
                .setDropBeforeRebuild(param.isDropBeforeRebuild()).build();
        logQuery(ApiPath.REBUILD_INDEX, rebuildIndexRequest);
        ManagedChannel channel = channelPool.getChannel();
        Olama.RebuildIndexResponse rebuildIndexResponse = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS).rebuildIndex(rebuildIndexRequest);

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

    /**
     * Rebuild the AI index for collection views.
     * Note: AI index operations are not supported via gRPC and must use HTTP fallback.
     */
    public BaseRes rebuildAIIndex(RebuildIndexParamInner param) {
        throw new UnsupportedOperationException("AI index operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * Add a new index to improve query performance.
     */
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
        ManagedChannel channel = channelPool.getChannel();
        Olama.AddIndexResponse addIndexResponse = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS).addIndex(addIndexRequest);

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

    /**
     * Modify existing vector index configuration.
     */
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
        ManagedChannel channel = channelPool.getChannel();
        Olama.ModifyVectorIndexResponse modifyVectorIndexResponse = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS).modifyVectorIndex(builder.build());

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

    /**
     * Drop an existing index from a collection.
     */
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
        ManagedChannel channel = channelPool.getChannel();
        Olama.DropIndexResponse response = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS).dropIndex(builder.build());

        logResponse(ApiPath.USER_CHANGE_PASSWORD, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: drop user index error, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return new BaseRes(response.getCode(), response.getMsg(), "");
    }
}
