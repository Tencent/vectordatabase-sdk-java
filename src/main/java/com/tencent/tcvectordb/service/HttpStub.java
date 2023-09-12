package com.tencent.tcvectordb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.Collection;
import com.tencent.tcvectordb.model.Database;
import com.tencent.tcvectordb.model.DocField;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.collection.*;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.entity.BaseRes;
import com.tencent.tcvectordb.service.param.*;
import com.tencent.tcvectordb.utils.JsonUtils;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * HTTP Stub for DB service API
 */
public class HttpStub implements Stub {
    private final ConnectParam connectParam;
    private final OkHttpClient client;
    private final Headers headers;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final MediaType JSON =
            MediaType.parse("application/json; charset=utf-8");
    private static final Logger logger = LoggerFactory.getLogger(HttpStub.class.getName());


    public HttpStub(ConnectParam connectParam) {
        this.connectParam = connectParam;
        String authorization = String.format("Bearer account=%s&api_key=%s",
                connectParam.getUsername(), connectParam.getKey());
        this.headers = new Headers.Builder()
                .add("Authorization", authorization).build();
        logger.debug("header: {}", authorization);
        this.client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(connectParam.getTimeout(), TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(
                        10, 5, TimeUnit.MINUTES))
                .build();
    }

    @Override
    public void createDatabase(Database database) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DB_CREATE);
        this.post(url, database.toString());
    }

    @Override
    public void dropDatabase(Database database) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DB_DROP);
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
        try {
            return mapper.readValue(dbsJson.toString(), new TypeReference<List<String>>() {});
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format(
                    "VectorDBServer response error: can't parse databases=%s", dbsJson));
        }
    }

    @Override
    public void createCollection(CreateCollectionParam param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.COL_CREATE);
        this.post(url, param.toString());
    }

    @Override
    public List<Collection> listCollections(String databaseName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.COL_LIST);
        JsonNode jsonNode = this.post(url, String.format("{\"database\":\"%s\"}", databaseName));
        JsonNode closJson = jsonNode.get("collections");
        if (closJson == null) {
            return new ArrayList<>();
        }

        List<Collection> res = new ArrayList<>();
        for (JsonNode node : closJson) {
            Collection collection = parseToCollection(node);
            res.add(collection);
        }
        return res;
    }


    @Override
    public Collection describeCollection(String databaseName, String collectionName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.COL_DESCRIBE);
        String body = String.format("{\"database\":\"%s\",\"collection\":\"%s\"}",
                databaseName, collectionName);
        JsonNode jsonNode = this.post(url, body);
        JsonNode dbsJson = jsonNode.get("collection");
        if (dbsJson == null) {
            return null;
        }
        return parseToCollection(dbsJson);
    }

    @Override
    public AffectRes truncateCollection(String databaseName, String collectionName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.COL_FLUSH);
        String body = String.format("{\"database\":\"%s\",\"collection\":\"%s\"}",
                databaseName, collectionName);
        JsonNode jsonNode = this.post(url, body);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    @Override
    public void dropCollection(String databaseName, String collectionName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.COL_DROP);
        String body = String.format("{\"database\":\"%s\",\"collection\":\"%s\"}",
                databaseName, collectionName);
        this.post(url, body);
    }

    @Override
    public AffectRes setCollectionAlias(String databaseName, String collectionName, String aliasName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.SET_COL_ALIAS);
        String body = String.format("{\"database\":\"%s\",\"collection\":\"%s\",\"alias\":\"%s\"}",
                databaseName, collectionName, aliasName);
        JsonNode jsonNode = this.post(url, body);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    @Override
    public AffectRes deleteCollectionAlias(String databaseName, String aliasName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DELETE_COL_ALIAS);
        String body = String.format("{\"database\":\"%s\",\"alias\":\"%s\"}",
                databaseName, aliasName);
        JsonNode jsonNode = this.post(url, body);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    @Override
    public AffectRes upsertDocument(InsertParamInner param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_UPSERT);
        JsonNode jsonNode = this.post(url, param.toString());
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    @Override
    public List<Document> queryDocument(QueryParamInner param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_QUERY);
        JsonNode jsonNode = this.post(url, param.toString());
        JsonNode docsNode = jsonNode.get("documents");
        List<Document> dosc = new ArrayList<>();
        if (docsNode == null) {
            return dosc;
        }
        try {
            Iterator<JsonNode> iterator = docsNode.elements();
            while (iterator.hasNext()) {
                JsonNode node = iterator.next();
                Document doc = node2Doc(node);
                dosc.add(doc);
            }
            return dosc;
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format("VectorDBServer response " +
                    "from query error: can't parse documents=%s", docsNode));
        }
    }

    @Override
    public List<List<Document>> searchDocument(SearchParamInner param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_SEARCH);
        JsonNode jsonNode = this.post(url, param.toString());
        JsonNode multiDocsNode = jsonNode.get("documents");
        List<List<Document>> multiDosc = new ArrayList<>();
        if (multiDocsNode == null) {
            return multiDosc;
        }
        try {
            Iterator<JsonNode> multiIter = multiDocsNode.elements();
            while (multiIter.hasNext()) {
                JsonNode docNode = multiIter.next();
                Iterator<JsonNode> iter = docNode.elements();
                List<Document> docs = new ArrayList<>();
                while (iter.hasNext()) {
                    JsonNode node = iter.next();
                    Document doc = node2Doc(node);
                    docs.add(doc);
                }
                multiDosc.add(docs);
            }
            return multiDosc;
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format("VectorDBServer response " +
                    "from search error: can't parse documents=%s", multiDocsNode));
        }
    }

    @Override
    public AffectRes deleteDocument(DeleteParamInner param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_DELETE);
        JsonNode jsonNode = this.post(url, param.toString());
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    public AffectRes updateDocument(UpdateParamInner param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_UPDATE);
        JsonNode jsonNode = this.post(url, param.toString());
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    @Override
    public BaseRes rebuildIndex(RebuildIndexParamInner param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.REBUILD_INDEX);
        JsonNode jsonNode = this.post(url, param.toString());
        return JsonUtils.parseObject(jsonNode.toString(), BaseRes.class);
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
        logger.debug("Query {}, body={}", url, json);
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
        logger.debug("Query {}, code={}, msg={}, result={}",
                response.request().url(), response.code(), response.message(), resStr);
        if (StringUtils.isEmpty(resStr)) {
            throw new VectorDBException(String.format(
                    "VectorDBServer error: ResponseBody empty, http code=%s, message=%s",
                    response.code(), response.message()));
        }
        if (!response.isSuccessful()) {
            throw new VectorDBException(String.format(
                    "VectorDBServer error: not Successful, http code=%s, message=%s, result=%s",
                    response.code(), response.message(), resStr));
        }
        JsonNode jsonNode = mapper.readTree(resStr);
        int code = jsonNode.get("code").asInt();
        if (code != 0) {
            throw new VectorDBException(String.format(
                    "VectorDBServer error: not Successful, body code=%s, message=%s",
                    code, jsonNode.get("msg").asText()));
        }
        return jsonNode;
    }

    private Document node2Doc(JsonNode node) throws JsonProcessingException {
        Document.Builder builder = Document.newBuilder();
        Iterator<String> iterator = node.fieldNames();
        ObjectMapper mapper = new ObjectMapper();
        while (iterator.hasNext()) {
            String name = iterator.next();
            JsonNode ele = node.get(name);
            if (StringUtils.equals("id", name)) {
                builder.withId(ele.asText());
            } else if (StringUtils.equals("vector", name)) {
                List<Double> vector = mapper.readValue(
                        ele.toString(), new TypeReference<List<Double>>() {
                        });
                builder.withVector(vector);
            } else if (StringUtils.equals("doc", name)) {
                builder.withDoc(ele.asText());
            } else if (StringUtils.equals("score", name)) {
                builder.withScore(ele.asDouble());
            } else {
                builder.addFilterField(new DocField(name, ele.asText()));
            }
        }
        return builder.build();
    }

    /**
     * parse {@link  JsonNode} to {@link Collection}
     *
     * @param jsonNode {@link  JsonNode}
     * @return {@link Collection}
     */
    private Collection parseToCollection(JsonNode jsonNode) {
        Collection collection = JsonUtils.parseObject(jsonNode.toString(), Collection.class);
        for (IndexField index : collection.getIndexes()) {
            if (index.isVectorField()) {
                ParamsSerializer params = null;
                switch (index.getIndexType()) {
                    case HNSW:
                        params = parseParams(jsonNode, HNSWParams.class);
                        break;
                    case IVF_FLAT:
                        params = parseParams(jsonNode, IVFFLATParams.class);
                        break;
                    case IVF_PQ:
                        params = parseParams(jsonNode, IVFPQParams.class);
                        break;
                    case IVF_SQ8:
                        params = parseParams(jsonNode, IVFSQ8Params.class);
                        break;
                    default:
                        throw new VectorDBException(String.format(
                                "VectorDBServer response error: can't parse collection=%s", jsonNode));
                }
                if (params != null) {
                    index.setParams(params);
                }
            }
        }
        return collection;
    }

    /**
     * parse  {@link  JsonNode} to {@link  ParamsSerializer}
     *
     * @param jsonNode {@link  JsonNode}
     * @param clz      {@link Class}
     * @return {@link ParamsSerializer}
     */
    private ParamsSerializer parseParams(JsonNode jsonNode, Class<? extends ParamsSerializer> clz) {
        JsonNode indexesNode = jsonNode.get("indexes");
        if (indexesNode == null) {
            throw new VectorDBException(String.format(
                    "VectorDBServer response error: can't parse collection=%s", jsonNode));
        } else {
            for (JsonNode indexNode : indexesNode) {
                if (indexNode.get("fieldType") != null && indexNode.get("fieldType").asText().equals("vector")) {
                    JsonNode paramsNode = indexesNode.get("params");
                    if (paramsNode != null) {
                        return JsonUtils.parseObject(paramsNode.toString(), clz);
                    }
                }
            }
        }

        throw new VectorDBException(String.format(
                "VectorDBServer response error: can't parse collection=%s", jsonNode));
    }
}
