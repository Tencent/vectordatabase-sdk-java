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
import com.tencent.tcvectordb.model.AIDatabase;
import com.tencent.tcvectordb.model.Database;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.entity.DataBaseType;
import com.tencent.tcvectordb.model.param.entity.DataBaseTypeRes;
import com.tencent.tcvectordb.rpc.Interceptor.BackendServiceInterceptor;
import com.tencent.tcvectordb.rpc.proto.Olama;
import com.tencent.tcvectordb.rpc.proto.SearchEngineGrpc;
import com.tencent.tcvectordb.service.ApiPath;
import io.grpc.ManagedChannel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * gRPC service implementation for database operations.
 */
public class DatabaseGrpcService extends BaseGrpcService {

    /**
     * Create a new database instance.
     */
    public void createDatabase(Database database) {
        ManagedChannel channel = channelPool.getChannel();
        Olama.DatabaseRequest.Builder databaseRequest = Olama.DatabaseRequest.newBuilder();
        if (database.getDatabaseName()!=null){
            databaseRequest.setDatabase(database.getDatabaseName());
        }
        logQuery(ApiPath.DB_CREATE, databaseRequest);
        Olama.DatabaseResponse response = SearchEngineGrpc.newBlockingStub(channel).withInterceptors(new BackendServiceInterceptor(false))
                .withDeadlineAfter(this.timeout, TimeUnit.SECONDS).
                createDatabase(databaseRequest.build());
        logResponse(ApiPath.DB_CREATE, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer create Database error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
    }

    /**
     * Drop an existing database.
     */
    public void dropDatabase(Database database) {
        ManagedChannel channel = channelPool.getChannel();
        Olama.DatabaseRequest.Builder databaseRequest = Olama.DatabaseRequest.newBuilder();
        if (database.getDatabaseName()!=null){
            databaseRequest.setDatabase(database.getDatabaseName());
        }
        logQuery(ApiPath.DB_DROP, databaseRequest.build());
        Olama.DatabaseResponse response =  SearchEngineGrpc.newBlockingStub(channel).withInterceptors(new BackendServiceInterceptor(false)).
                withDeadlineAfter(this.timeout, TimeUnit.SECONDS).dropDatabase(databaseRequest.build());
        logResponse(ApiPath.DB_DROP, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer drop Database error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
    }

    /**
     * Create a new AI database instance.
     * Note: AI database operations are not supported via gRPC and must use HTTP fallback.
     */
    public AffectRes createAIDatabase(AIDatabase aiDatabase) {
        throw new UnsupportedOperationException("AI database operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * Describe database information and type.
     */
    public DataBaseTypeRes describeDatabase(Database database) {
        ManagedChannel channel = channelPool.getChannel();
        
        Olama.DescribeDatabaseRequest.Builder databaseRequest = Olama.DescribeDatabaseRequest.newBuilder();
        if (database.getDatabaseName()!=null){
            databaseRequest.setDatabase(database.getDatabaseName());
        }
        logQuery(ApiPath.DB_DESCRIBE, databaseRequest.build());
        Olama.DescribeDatabaseResponse response = SearchEngineGrpc.newBlockingStub(channel).withInterceptors(new BackendServiceInterceptor(false))
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

    /**
     * Drop an existing AI database.
     * Note: AI database operations are not supported via gRPC and must use HTTP fallback.
     */
    public AffectRes dropAIDatabase(AIDatabase aiDatabase) {
        throw new UnsupportedOperationException("AI database operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * List all available database names.
     */
    public List<String> listDatabases() {
        ManagedChannel channel = channelPool.getChannel();
        
        Olama.DatabaseRequest request = Olama.DatabaseRequest.newBuilder().build();
        logQuery(ApiPath.DB_LIST, request);
        Olama.DatabaseResponse response = SearchEngineGrpc.newBlockingStub(channel).withInterceptors(new BackendServiceInterceptor(false))
                .withDeadlineAfter(this.timeout, TimeUnit.SECONDS).listDatabases(request);
        logResponse(ApiPath.DB_LIST, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer list Database error: not Successful, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return response.getDatabasesList();
    }

    /**
     * List all databases with their type information.
     */
    public Map<String, DataBaseType> listDatabaseInfos() {
        ManagedChannel channel = channelPool.getChannel();
        Olama.DatabaseRequest request = Olama.DatabaseRequest.newBuilder().build();
        logQuery(ApiPath.DB_LIST, request);
        Olama.DatabaseResponse response = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS).listDatabases(request);
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
}
