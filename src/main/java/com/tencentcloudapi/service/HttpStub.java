package com.tencentcloudapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencentcloudapi.exception.VectorDBException;
import com.tencentcloudapi.model.Collection;
import com.tencentcloudapi.model.Database;
import com.tencentcloudapi.model.Document;
import com.tencentcloudapi.model.param.collection.CreateCollectionParam;
import com.tencentcloudapi.model.param.database.ConnectParam;
import com.tencentcloudapi.service.param.DeleteParamInner;
import com.tencentcloudapi.service.param.InsertParamInner;
import com.tencentcloudapi.service.param.QueryParamInner;
import com.tencentcloudapi.service.param.SearchParamInner;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
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
    private static final MediaType JSON =
            MediaType.parse("application/json; charset=utf-8");

    private static final Logger logger = LoggerFactory.getLogger(HttpStub.class.getName());


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
    public void createDatabase(Database database) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DB_CREATE);
        this.post(url, database.toString());
    }

    @Override
    public void dropDatabase(Database database) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DB_DROP);
        this.post(url, database.toString());
    }

    @Override
    public List<String> listDatabases() {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DB_LIST);
        JsonNode jsonNode = this.get(url);
        JsonNode dbsJson = jsonNode.get("databases");
        if (dbsJson == null) {
            return new ArrayList<>();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(dbsJson.asText(), new TypeReference<List<String>>() {});
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format(
                    "VectorDBServer response error: can't parse databases=%s", dbsJson.asText()));
        }
    }

    @Override
    public void createCollection(CreateCollectionParam param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.COL_CREAGE);
        this.post(url, param.toString());
    }

    @Override
    public List<Collection> listCollections(String databaseName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.COL_LIST);
        JsonNode jsonNode = this.post(url, String.format("{\"databases\":\"%s\"}", databaseName));
        JsonNode dbsJson = jsonNode.get("collections");
        if (dbsJson == null) {
            return new ArrayList<>();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(dbsJson.asText(), new TypeReference<List<Collection>>() {});
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format(
                    "VectorDBServer response error: can't parse collections=%s", dbsJson.asText()));
        }
    }

    @Override
    public Collection describeCollection(String databaseName, String collectionName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.COL_DESCRIBE);
        String body = String.format("{\"databases\":\"%s\",\"collection\":\"%s\"}",
                databaseName, collectionName);
        JsonNode jsonNode = this.post(url, body);
        JsonNode dbsJson = jsonNode.get("collection");
        if (dbsJson == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(dbsJson.asText(), Collection.class);
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format(
                    "VectorDBServer response error: can't parse collection=%s", dbsJson.asText()));
        }
    }

    @Override
    public void dropCollection(String databaseName, String collectionName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.COL_DROP);
        String body = String.format("{\"databases\":\"%s\",\"collection\":\"%s\"}",
                databaseName, collectionName);
        this.post(url, body);
    }

    @Override
    public void upsertDocument(InsertParamInner param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_UPSERT);
        this.post(url, param.toString());
    }

    @Override
    public List<Document> queryDocument(QueryParamInner param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_QUERY);
        JsonNode jsonNode = this.post(url, param.toString());
        JsonNode docsNode = jsonNode.get("documents");
        if (docsNode == null) {
            return new ArrayList<>();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(docsNode.asText(), new TypeReference<List<Document>>() {});
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format(
                    "VectorDBServer response from query error: can't parse documents=%s", docsNode.asText()));
        }
    }

    @Override
    public List<List<Document>> searchDocument(SearchParamInner param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_SEARCH);
        JsonNode jsonNode = this.post(url, param.toString());
        JsonNode docsNode = jsonNode.get("documents");
        if (docsNode == null) {
            return new ArrayList<>();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(docsNode.asText(), new TypeReference<List<List<Document>>>() {});
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format(
                    "VectorDBServer response from search error: can't parse documents=%s", docsNode.asText()));
        }
    }

    @Override
    public void deleteDocument(DeleteParamInner param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_DELETE);
        this.post(url, param.toString());
    }

    private JsonNode get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .headers(this.headers)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return parseResponse(response);
        } catch (IOException ex) {
            throw new VectorDBException(ex.getMessage());
        }
    }

    private JsonNode post(String url, String json) {
        logger.debug(String.format("Query %s, body=%s", url, json));
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .headers(this.headers)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return parseResponse(response);
        } catch (IOException ex) {
            throw new VectorDBException(String.format(
                    "VectorDBServer IOException: %s", ex.getMessage()));
        }
    }

    private JsonNode parseResponse(Response response) throws IOException {
        ResponseBody resBody = response.body();
        if (resBody == null) {
            throw new VectorDBException(String.format(
                    "VectorDBServer error: ResponseBody null, http code=%s, message=%s",
                    response.code(), response.message()));
        }
        String resStr = resBody.string();
        logger.debug(String.format("Query %s, code=%s, msg=%s, result=%s",
                response.request().url(), response.code(), response.message(), resStr));
        if (StringUtils.isEmpty(resStr)) {
            throw new VectorDBException(String.format(
                    "VectorDBServer error: ResponseBody empty, http code=%s, message=%s",
                    response.code(), response.message()));
        }
        if (!response.isSuccessful()) {
            throw new VectorDBException(String.format(
                    "VectorDBServer error: not Successful, http code=%s, message=%s",
                    response.code(), response.message()));
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(resStr);
        int code = jsonNode.get("code").asInt();
        if (code != 0) {
            throw new VectorDBException(String.format(
                    "VectorDBServer error: not Successful, body code=%s, message=%s",
                    code, jsonNode.get("msg").asText()));
        }
        return jsonNode;
    }
}
