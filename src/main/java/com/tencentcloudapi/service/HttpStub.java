package com.tencentcloudapi.service;

import com.tencentcloudapi.model.Collection;
import com.tencentcloudapi.model.Document;
import com.tencentcloudapi.model.param.collection.CreateCollectionParam;
import com.tencentcloudapi.model.param.dml.QueryParam;
import com.tencentcloudapi.model.param.dml.SearchByIdParam;
import com.tencentcloudapi.model.param.dml.SearchParam;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * HTTP Stub for DB service API
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class HttpStub implements Stub {

    private final OkHttpClient client;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public HttpStub() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
                .build();
    }

    @Override
    public void createDatabase(String databaseName) {

    }

    @Override
    public void dropDatabase(String databaseName) {

    }

    @Override
    public List<String> listDatabases() {
        return null;
    }

    @Override
    public Collection createCollection(CreateCollectionParam params) {
        return null;
    }

    @Override
    public List<Collection> listCollections() {
        return null;
    }

    @Override
    public Collection describeCollection(String collectionName) {
        return null;
    }

    @Override
    public void dropCollection(String collectionName) {

    }

    @Override
    public void upsertDocument(List<Document> documents) {

    }

    @Override
    public List<Document> queryDocument(QueryParam param) {
        return null;
    }

    @Override
    public List<List<Document>> searchDocument(SearchParam param) {
        return null;
    }

    @Override
    public List<Document> searchDocumentById(SearchByIdParam param) {
        return null;
    }

    @Override
    public void deleteDocument(List<String> documentIds) {

    }

    private Response get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response;
        }
    }

    private Response post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response;
        }
    }
}
