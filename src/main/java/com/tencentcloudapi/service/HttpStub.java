package com.tencentcloudapi.service;

import com.tencentcloudapi.model.Collection;
import com.tencentcloudapi.model.Document;
import com.tencentcloudapi.model.param.collection.CreateCollectionParam;
import com.tencentcloudapi.model.param.database.ConnectParam;
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
    private final ConnectParam connectParam;
    private final OkHttpClient client;

    private final Headers headers;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public HttpStub(ConnectParam connectParam) {
        this.connectParam = connectParam;
        this.headers = new Headers.Builder()
                .add("Authorization", String.format("Bearer account=%s&api_key=%s",
                        connectParam.getUsername(), connectParam.getKey())).build();
        this.client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(connectParam.getTimeout(), TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(
                        10, 5, TimeUnit.MINUTES))
                .build();
    }

    @Override
    public void createDatabase(String databaseName) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DB_CREATE);

    }

    @Override
    public void dropDatabase(String databaseName) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DB_DROP);
    }

    @Override
    public List<String> listDatabases() {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DB_LIST);
        return null;
    }

    @Override
    public Collection createCollection(CreateCollectionParam params) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.COL_CREAGE);
        return null;
    }

    @Override
    public List<Collection> listCollections() {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.COL_LIST);
        return null;
    }

    @Override
    public Collection describeCollection(String collectionName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.COL_DESCRIBE);
        return null;
    }

    @Override
    public void dropCollection(String collectionName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.COL_DROP);

    }

    @Override
    public void upsertDocument(List<Document> documents) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_UPSERT);
    }

    @Override
    public List<Document> queryDocument(QueryParam param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_QUERY);
        return null;
    }

    @Override
    public List<List<Document>> searchDocument(SearchParam param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_SEARCH);
        return null;
    }

    @Override
    public List<Document> searchDocumentById(SearchByIdParam param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_SEARCH);
        return null;
    }

    @Override
    public void deleteDocument(List<String> documentIds) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_DELETE);
    }

    private Response get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .headers(this.headers)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response;
        }
    }

    private Response post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .headers(this.headers)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response;
        }
    }
}
