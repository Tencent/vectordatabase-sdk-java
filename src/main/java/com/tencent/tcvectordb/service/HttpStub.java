package com.tencent.tcvectordb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.*;
import com.tencent.tcvectordb.model.Collection;
import com.tencent.tcvectordb.model.param.collection.*;
import com.tencent.tcvectordb.model.param.collectionView.CreateCollectionViewParam;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.model.param.entity.*;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.model.param.enums.FileTypeEnum;
import com.tencent.tcvectordb.service.param.*;
import com.tencent.tcvectordb.utils.FileUtils;
import com.tencent.tcvectordb.utils.JsonUtils;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
    public void createAIDatabase(AIDatabase aiDatabase) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.AI_DB_CREATE);
        this.post(url, aiDatabase.toString());
    }

    @Override
    public DataBaseTypeRes describeDatabase(Database database) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DB_DESCRIBE);
        String body = String.format("{\"database\":\"%s\"}",
                database.getDatabaseName());
        JsonNode jsonNode = this.post(url, body);
        return JsonUtils.parseObject(jsonNode.toString(), DataBaseTypeRes.class);
    }

    @Override
    public void dropAIDatabase(AIDatabase aiDatabase) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.AI_DB_DROP);
        this.post(url, aiDatabase.toString());
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
    public Map<String, DataBaseType> listDatabaseInfos() {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DB_LIST);
        JsonNode jsonNode = this.get(url);
        JsonNode dbsJson = jsonNode.get("info");
        if (dbsJson == null) {
            return new HashMap<>();
        }
        try {
            return mapper.readValue(dbsJson.toString(), new TypeReference<Map<String, DataBaseType>>() {});
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
    public void createCollectionView(CreateCollectionViewParam params) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.AI_COL_CREATE);
        this.post(url, params.toString());
    }

    @Override
    public List<Collection> listCollections(String databaseName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.COL_LIST);
        JsonNode jsonNode = this.post(url, String.format("{\"database\":\"%s\"}", databaseName));
        JsonNode closJson = jsonNode.get("collections");
        if (closJson == null) {
            return new ArrayList<>();
        }
        return JsonUtils.collectionDeserializer(closJson.toString(), new TypeReference<List<Collection>>() {});
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
        return JsonUtils.collectionDeserializer(dbsJson.toString(), new TypeReference<Collection>() {});
    }

    @Override
    public AffectRes truncateCollection(String databaseName, String collectionName, DataBaseTypeEnum dbType) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.COL_FLUSH);
        String body = String.format("{\"database\":\"%s\",\"collection\":\"%s\"}",
                databaseName, collectionName);
        JsonNode jsonNode = this.post(url, body);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    @Override
    public AffectRes truncateCollectionView(String databaseName, String collectionName, DataBaseTypeEnum dbType) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.AI_COL_FLUSH);
        String body = String.format("{\"database\":\"%s\",\"collectionView\":\"%s\"}",
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
    public AffectRes setAlias(String databaseName, String collectionName, String aliasName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.SET_COL_ALIAS);
        String body = String.format("{\"database\":\"%s\",\"collection\":\"%s\",\"alias\":\"%s\"}",
                databaseName, collectionName, aliasName);
        JsonNode jsonNode = this.post(url, body);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    @Override
    public AffectRes deleteAlias(String databaseName, String aliasName) {
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
    public SearchRes searchDocument(SearchParamInner param, DataBaseTypeEnum dbType) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.DOC_SEARCH);
        if (DataBaseTypeEnum.isAIDataBase(dbType)){
            url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.AI_DOCUMENT_SEARCH);
        }
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
        try {
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

    @Override
    public BaseRes rebuildAIIndex(RebuildIndexParamInner param) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.AI_REBUILD_INDEX);
        JsonNode jsonNode = this.post(url, param.toString());
        return JsonUtils.parseObject(jsonNode.toString(), BaseRes.class);
    }

    @Override
    public AffectRes setAIAlias(String databaseName, String collectionName, String aliasName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.AI_ALIAS_SET);
        String body = String.format("{\"database\":\"%s\",\"collectionView\":\"%s\",\"alias\":\"%s\"}",
                databaseName, collectionName, aliasName);
        JsonNode jsonNode = this.post(url, body);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    @Override
    public AffectRes deleteAIAlias(String databaseName, String aliasName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.AI_ALIAS_DELETE);
        String body = String.format("{\"database\":\"%s\",\"alias\":\"%s\"}",
                databaseName, aliasName);
        JsonNode jsonNode = this.post(url, body);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    @Override
    public List<CollectionView> listCollectionView(String databaseName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.AI_COL_LIST);
        JsonNode jsonNode = this.post(url, String.format("{\"database\":\"%s\"}", databaseName));
        JsonNode closJson = jsonNode.get("collectionViews");
        if (closJson == null) {
            return new ArrayList<>();
        }
        return JsonUtils.collectionDeserializer(closJson.toString(), new TypeReference<List<CollectionView>>() {});
    }

    @Override
    public CollectionView describeCollectionView(String databaseName, String collectionName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.AI_COL_DESCRIBE);
        String body = String.format("{\"database\":\"%s\",\"collectionView\":\"%s\"}",
                databaseName, collectionName);
        JsonNode jsonNode = this.post(url, body);
        JsonNode dbsJson = jsonNode.get("collectionView");
        if (dbsJson == null) {
            return null;
        }
        return JsonUtils.collectionDeserializer(dbsJson.toString(), new TypeReference<CollectionView>() {});
    }

    @Override
    public void dropCollectionView(String databaseName, String collectionName) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.AI_COL_DROP);
        String body = String.format("{\"database\":\"%s\",\"collection\":\"%s\"}",
                databaseName, collectionName);
        this.post(url, body);
    }

    @Override
    public List<DocumentSet> queryAIDocument(CollectionViewQueryParamInner queryParamInner) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.AI_DOCUMENT_QUERY);
        JsonNode jsonNode = this.post(url, queryParamInner.toString());
        JsonNode docsNode = jsonNode.get("documentSets");
        List<DocumentSet> dosc = new ArrayList<>();
        if (docsNode == null) {
            return dosc;
        }
        try {
            Iterator<JsonNode> iterator = docsNode.elements();
            while (iterator.hasNext()) {
                JsonNode node = iterator.next();
                DocumentSet doc = node2DocmentSet(node);
                dosc.add(doc);
            }
            return dosc;
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format("VectorDBServer response " +
                    "from query error: can't parse documents=%s", docsNode));
        }
    }

    @Override
    public AffectRes deleteAIDocument(CollectionViewDeleteParamInner deleteParamInner) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.AI_DOCUMENT_DELETE);
        JsonNode jsonNode = this.post(url, deleteParamInner.toString());
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    @Override
    public SearchContentRes searchAIDocument(SearchDocParamInner searchDocParamInner) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.AI_DOCUMENT_SEARCH);
        JsonNode jsonNode = this.post(url, searchDocParamInner.toString());
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
            return new SearchContentRes(code, msg, warning, Collections.emptyList());
        }
        try {
            List<Document> multiDosc = new ArrayList<>();
            Iterator<JsonNode> multiIter = multiDocsNode.elements();
            while (multiIter.hasNext()) {
                JsonNode docNode = multiIter.next();
                Document doc = node2Doc(docNode);
                multiDosc.add(doc);
            }
            return new SearchContentRes(code, msg, warning, multiDosc);
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format("VectorDBServer response " +
                    "from search error: can't parse documents=%s", multiDocsNode));
        }
    }

    @Override
    public AffectRes updateAIDocument(CollectionViewUpdateParamInner updateParamInner) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.AI_DOCUMENT_UPDATE);
        JsonNode jsonNode = this.post(url, updateParamInner.toString());
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    public UploadUrlRes getUploadUrl(String databaseName, String collectionViewName, String documentSetName, String fileName, String fileType) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.AI_DOCUMENT_UPLOADER_URL);
        String body = String.format("{\"database\":\"%s\",\"collectionView\":\"%s\", \"documentSetName\":\"%s\", \"fileName\":\"%s\"," +
                        "\"fileType\":\"%s\"}",
                databaseName, collectionViewName, documentSetName, fileName, fileType);
        JsonNode jsonNode = this.post(url, body);
        return JsonUtils.collectionDeserializer(jsonNode.toString(), new TypeReference<UploadUrlRes>() {});
    }

    @Override
    public void upload(String databaseName, String collectionViewName, String documentSetName, String filePath, Map<String, Object> metaDataMap) throws Exception{
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()){
            throw new VectorDBException("file is not existed");
        }

        if (file.length()<=0){
            throw new VectorDBException("file is empty");
        }

        FileTypeEnum fileType = FileUtils.getFileType(file);
        if(fileType == FileTypeEnum.UNSUPPORT){
            throw new VectorDBException("only markdown file can upload");
        }
        UploadUrlRes uploadUrlRes = getUploadUrl(databaseName, collectionViewName, documentSetName, file.getName(), fileType.getDataFileType());
        if(uploadUrlRes.getCredentials()==null || uploadUrlRes.getCredentials().getTmpSecretId().equals("") || uploadUrlRes.getUploadCondition()==null
                || uploadUrlRes.getUploadCondition().getMaxSupportContentLength()==0){
            throw new VectorDBException("get file upload url failed");
        }

        if (file.length()> uploadUrlRes.getUploadCondition().getMaxSupportContentLength()){
            throw new VectorDBException(String.format("%s fileSize is invalid, support max content length is %d bytes",
                    filePath, uploadUrlRes.getUploadCondition().getMaxSupportContentLength()));
        }
        String uploadPath = uploadUrlRes.getUploadPath();
        String cosEndpoint = uploadUrlRes.getCosEndpoint();
        String bucket = uploadUrlRes.getCosBucket();
        String region = cosEndpoint.split("\\.")[2];
        BasicSessionCredentials cred = new BasicSessionCredentials(uploadUrlRes.getCredentials().getTmpSecretId(),
                uploadUrlRes.getCredentials().getTmpSecretKey(), uploadUrlRes.getCredentials().getToken());
        ClientConfig cosClientConfig = new ClientConfig(new Region(region));
        COSClient cosClient = new COSClient(cred, cosClientConfig);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, uploadPath, file);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.addUserMetadata("fileType", fileType.getDataFileType());
        metadata.addUserMetadata("id", uploadUrlRes.getDocumentSetId());
        if (metaDataMap!=null && !metaDataMap.isEmpty()){
            String metaJson = URLEncoder.encode(Base64.getEncoder().encodeToString(JsonUtils.toJsonString(metaDataMap).getBytes(StandardCharsets.UTF_8)),
                    String.valueOf(StandardCharsets.UTF_8));
            metadata.addUserMetadata("data", metaJson);
        }
        if (JsonUtils.toJsonString(metadata).length()>2048){
            throw new VectorDBException("cos header for param MetaData is too large, it can not be more than 2k");
        }
        putObjectRequest.withMetadata(metadata);
        putObjectRequest.withKey(uploadPath);

        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

        logger.debug("upload file, response:%s", JsonUtils.toJsonString(putObjectResult));
        cosClient.shutdown();
    }

    @Override
    public GetDocumentSetRes getFile(String databaseName, String collectionName, String documentSetName, String documentSetId) {
        String url = String.format("%s%s", this.connectParam.getUrl(), ApiPath.AI_GET_FILE);
        String body = String.format("{\"database\":\"%s\",\"collectionView\":\"%s\",\"documentSetName\":\"%s\"," +
                        "\"documentSetId\":\"%s\"}",
                databaseName, collectionName, documentSetName, documentSetId);
        JsonNode jsonNode = this.post(url, body);
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
        int count = 0;
        if (jsonNode.get("count") != null) {
            count = jsonNode.get("count").asInt();
        }

        GetDocumentSetRes res = new GetDocumentSetRes(code, msg, warning, count);
        JsonNode multiDocsNode = jsonNode.get("documentSet");
        if (multiDocsNode == null) {
            return res;
        }
        try {
            res.setDocumentSet(node2DocumentFileContent(multiDocsNode));
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format("VectorDBServer response " +
                    "from search error: can't parse documents=%s", multiDocsNode));
        }
        return res;
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
            } else if (StringUtils.equals("data", name)) {
                builder.addFilterField(new DocField(name, mapper.readValue(
                        ele.toString(), new TypeReference<ChunkInfo>() {
                        })));
            } else if (StringUtils.equals("documentSet", name)) {
                builder.addFilterField(new DocField(name, node2Doc(ele)));
            } else {
                if (ele.isInt()) {
                    builder.addFilterField(new DocField(name, ele.asInt()));
                } else if (ele.isLong()) {
                    builder.addFilterField(new DocField(name, ele.asLong()));
                } else {
                    builder.addFilterField(new DocField(name, ele.asText()));
                }
            }
        }
        return builder.build();
    }

    private DocumentSet node2DocmentSet(JsonNode node) throws JsonProcessingException {
        DocumentSet.Builder builder = DocumentSet.newBuilder();
        Iterator<String> iterator = node.fieldNames();
        ObjectMapper mapper = new ObjectMapper();
        while (iterator.hasNext()) {
            String name = iterator.next();
            JsonNode ele = node.get(name);
            if (StringUtils.equals("documentSetId", name)) {
                builder.withDocumentSetId(ele.asText());
            } else if (StringUtils.equals("documentSetInfo", name)) {
                DocumentSetInfo documentSetInfo = mapper.readValue(
                        ele.toString(), new TypeReference<DocumentSetInfo>() {
                        });
                builder.withDocumentSetInfo(documentSetInfo);
            } else if (StringUtils.equals("documentSetName", name)) {
                builder.withDocumnetSetName(ele.asText());
            }else if (StringUtils.equals("textPrefix", name)) {
                builder.withTextPrefix(ele.asText());
            }else {
                if (ele.isInt()) {
                    builder.addFilterField(new DocField(name, ele.asInt()));
                } else if (ele.isLong()) {
                    builder.addFilterField(new DocField(name, ele.asLong()));
                    builder.addFilterField(new DocField(name, ele.isLong()));
                } else if (ele.isArray()) {
                    List values = mapper.readValue(
                            ele.toString(), new TypeReference<List>() {
                            });
                    builder.addFilterField(new DocField(name, values));
                } else {
                    builder.addFilterField(new DocField(name, ele.asText()));
                }
            }
        }
        return builder.build();
    }

    private DocumentFileContent node2DocumentFileContent(JsonNode node) throws JsonProcessingException{
        DocumentFileContent documentFileContent = new DocumentFileContent();
        documentFileContent.setDocFields(new ArrayList<>());
        Iterator<String> iterator = node.fieldNames();
        ObjectMapper mapper = new ObjectMapper();
        while (iterator.hasNext()) {
            String name = iterator.next();
            JsonNode ele = node.get(name);
            if (StringUtils.equals("documentSetId", name)) {
                documentFileContent.setDocumentSetId(ele.asText());
            } else if (StringUtils.equals("documentSetInfo", name)) {
                DocumentSetInfo documentSetInfo = mapper.readValue(
                        ele.toString(), new TypeReference<DocumentSetInfo>() {
                        });
                documentFileContent.setDocumentSetInfo(documentSetInfo);
            } else if (StringUtils.equals("documentSetName", name)) {
                documentFileContent.setDocumentSetId(ele.asText());
            }else if (StringUtils.equals("text", name)) {
                documentFileContent.setText(ele.asText());
            }else {
                if (ele.isInt()) {
                    documentFileContent.addFilterField(new DocField(name, ele.asInt()));
                } else if (ele.isLong()) {
                    documentFileContent.addFilterField(new DocField(name, ele.asLong()));
                } else {
                    documentFileContent.addFilterField(new DocField(name, ele.asText()));
                }
            }
        }
        return documentFileContent;
    }
}
