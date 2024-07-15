package tcvdb.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tcvdb.exception.VectorDBException;
import tcvdb.model.Database;
import tcvdb.model.DocField;
import tcvdb.model.Document;
import tcvdb.model.Collection;
import tcvdb.model.param.collection.CreateCollectionParam;
import tcvdb.model.param.database.ConnectParam;
import tcvdb.model.param.entity.AffectRes;
import tcvdb.model.param.entity.BaseRes;
import tcvdb.model.param.entity.DataBaseType;
import tcvdb.model.param.entity.SearchRes;
import tcvdb.service.param.*;
import tcvdb.utils.JsonUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * HTTP Stub for DB service API
 */
public class HttpStub implements Stub {
    private final ConnectParam connectParam;
    private final OkHttpClient client;
    private final Headers headers;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
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
        return JsonUtils.parseObject(dbsJson.toString(), new TypeReference<List<String>>() {
        });
    }

    @Override
    public Map<String, DataBaseType> listDatabaseInfos() {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DB_LIST);
        JsonNode jsonNode = this.get(url);
        JsonNode dbsJson = jsonNode.get("info");
        if (dbsJson == null) {
            return new HashMap<>();
        }
        return JsonUtils.parseObject(dbsJson.toString(), new TypeReference<Map<String, DataBaseType>>() {
        });
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
        return JsonUtils.parseObject(closJson.toString(), new TypeReference<List<Collection>>() {
        });
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
        return JsonUtils.jsonNodeToObject(dbsJson, Collection.class);
    }

    @Override
    public AffectRes truncateCollection(String databaseName, String collectionName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.COL_FLUSH);
        String body = String.format("{\"database\":\"%s\",\"collection\":\"%s\"}",
                databaseName, collectionName);
        JsonNode jsonNode = this.post(url, body);
        return JsonUtils.jsonNodeToObject(jsonNode, AffectRes.class);
    }

    @Override
    public void dropCollection(String databaseName, String collectionName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.COL_DROP);
        String body = String.format("{\"database\":\"%s\",\"collection\":\"%s\"}",
                databaseName, collectionName);
        this.post(url, body);
    }

    @Override
    public AffectRes setAlias(String databaseName, String collectionName, String aliasName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.SET_COL_ALIAS);
        String body = String.format("{\"database\":\"%s\",\"collection\":\"%s\",\"alias\":\"%s\"}",
                databaseName, collectionName, aliasName);
        JsonNode jsonNode = this.post(url, body);
        return JsonUtils.jsonNodeToObject(jsonNode, AffectRes.class);
    }

    @Override
    public AffectRes deleteAlias(String databaseName, String aliasName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DELETE_COL_ALIAS);
        String body = String.format("{\"database\":\"%s\",\"alias\":\"%s\"}",
                databaseName, aliasName);
        JsonNode jsonNode = this.post(url, body);
        return JsonUtils.jsonNodeToObject(jsonNode, AffectRes.class);
    }

    @Override
    public AffectRes upsertDocument(InsertParamInner param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_UPSERT);
        JsonNode jsonNode = this.post(url, param.toString());
        return JsonUtils.jsonNodeToObject(jsonNode, AffectRes.class);
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

        Iterator<JsonNode> iterator = docsNode.elements();
        while (iterator.hasNext()) {
            JsonNode node = iterator.next();
            Document doc = node2Doc(node);
            dosc.add(doc);
        }
        return dosc;
    }

    @Override
    public SearchRes searchDocument(SearchParamInner param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_SEARCH);
        JsonNode jsonNode = this.post(url, param.toString());
        JsonNode multiDocsNode = jsonNode.get("documents");
        int code = 0;
        if (jsonNode.get("code") != null) {
            code = jsonNode.get("code").asInt();
        }
        String msg = "";
        if (jsonNode.get("msg") != null) {
            msg = jsonNode.get("msg").asText();
        }
        String warning = "";
        if (jsonNode.get("warning") != null) {
            warning = jsonNode.get("warning").asText();
        }
        if (multiDocsNode == null) {
            return new SearchRes(code, msg, warning, Collections.emptyList());
        }

        List<List<Document>> multiDosc = new ArrayList<>();
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
        return new SearchRes(code, msg, warning, multiDosc);

    }

    @Override
    public AffectRes deleteDocument(DeleteParamInner param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_DELETE);
        JsonNode jsonNode = this.post(url, param.toString());
        return JsonUtils.jsonNodeToObject(jsonNode, AffectRes.class);
    }

    public AffectRes updateDocument(UpdateParamInner param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_UPDATE);
        JsonNode jsonNode = this.post(url, param.toString());
        return JsonUtils.jsonNodeToObject(jsonNode, AffectRes.class);
    }

    @Override
    public BaseRes rebuildIndex(RebuildIndexParamInner param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.REBUILD_INDEX);
        JsonNode jsonNode = this.post(url, param.toString());
        return JsonUtils.jsonNodeToObject(jsonNode, BaseRes.class);
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
            logger.error("VectorDBServer IOException", ex);
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

        JsonNode jsonNode = JsonUtils.parseToJsonNode(resStr);

        if (!jsonNode.has("code")) {
            throw new VectorDBException(String.format(
                    "VectorDBServer error: body content error, body=%s", resStr));
        }
        int code = jsonNode.get("code").asInt();
        if (code != 0) {
            throw new VectorDBException(String.format(
                    "VectorDBServer error: not Successful, body code=%s, message=%s",
                    code, jsonNode.get("msg").asText()));
        }

        return jsonNode;
    }

    private Document node2Doc(JsonNode node) {
        Document.Builder builder = Document.newBuilder();
        Iterator<String> iterator = node.fieldNames();
        while (iterator.hasNext()) {
            String name = iterator.next();
            JsonNode ele = node.get(name);
            if (StringUtils.equals("id", name)) {
                builder.withId(ele.asText());
            } else if (StringUtils.equals("vector", name)) {
                List<Double> vector = JsonUtils.parseObject(ele.toString(), new TypeReference<List<Double>>() {
                });
                builder.withVector(Collections.singletonList(vector));
            } else if (StringUtils.equals("score", name)) {
                builder.withScore(ele.asDouble());
            }else {
                if (ele.isInt()) {
                    builder.addFilterField(new DocField(name, ele.asInt()));
                } else if (ele.isLong()) {
                    builder.addFilterField(new DocField(name, ele.asLong()));
                } else if (ele.isArray()) {
                    List values = JsonUtils.parseObject(ele.toString(), new TypeReference<List<Object>>() {
                    });
                    builder.addFilterField(new DocField(name, values));
                } else {
                    builder.addFilterField(new DocField(name, ele.asText()));
                }
            }
        }
        return builder.build();
    }
}
