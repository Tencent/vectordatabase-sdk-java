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
package com.tencent.tcvectordb.service.impl.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.*;
import com.tencent.tcvectordb.model.param.collection.UploadFileParam;
import com.tencent.tcvectordb.model.param.collectionView.LoadAndSplitTextParam;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.model.param.entity.*;
import com.tencent.tcvectordb.model.param.collectionView.SplitterPreprocessParams;
import com.tencent.tcvectordb.model.param.collectionView.ParsingProcessParam;
import com.tencent.tcvectordb.service.ApiPath;
import com.tencent.tcvectordb.utils.JsonUtils;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Base HTTP service containing shared fields and methods for all module services.
 * All resources (client, headers, connectParam) are injected from HttpStub.
 */
public abstract class BaseHttpService {
    protected ConnectParam connectParam;
    protected OkHttpClient client;
    protected Headers.Builder headersBuilder;
    protected static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    protected static final Logger logger = LoggerFactory.getLogger(BaseHttpService.class.getName());

    /**
     * Initialize the HTTP service with shared resources from HttpStub.
     * 
     * @param connectParam Connection parameters for building URLs
     * @param sharedClient Shared OkHttpClient instance
     * @param sharedHeadersBuilder Shared headers builder with authorization
     */
    public void init(ConnectParam connectParam, OkHttpClient sharedClient, Headers.Builder sharedHeadersBuilder) {
        this.connectParam = connectParam;
        this.client = sharedClient;
        this.headersBuilder = sharedHeadersBuilder;
    }

    /**
     * Perform HTTP GET request.
     */
    protected JsonNode get(String url, boolean ai) {
        Request request = new Request.Builder()
                .url(url)
                .headers(getHeaders(ai))
                .build();
        try (Response response = client.newCall(request).execute()) {
            return parseResponse(response);
        } catch (IOException ex) {
            throw new VectorDBException(ex.getMessage());
        }
    }

    /**
     * Perform HTTP POST request.
     */
    protected JsonNode post(String url, String json, boolean ai) {
        logger.debug("Query {}, body={}", url, json);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .headers(getHeaders(ai))
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

    /**
     * Build headers for HTTP request.
     */
    protected Headers getHeaders(boolean ai) {
        Headers.Builder headersTmp = new Headers.Builder();
        String backend = "vdb";
        if (ai){
            backend = "ai";
        }
        headersTmp.add("backend-service", backend);
        headersTmp.add("Authorization", this.headersBuilder.get("Authorization"));
        logger.debug("Backend: {}", backend);
        return headersBuilder.build();
    }

    /**
     * Parse HTTP response and handle errors.
     */
    protected JsonNode parseResponse(Response response) throws IOException {
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
        int code = jsonNode.get("code").asInt();
        if (code != 0) {
            throw new VectorDBException(String.format(
                    "VectorDBServer error: not Successful, body code=%s, message=%s",
                    code, jsonNode.get("msg").asText()));
        }
        return jsonNode;
    }

    /**
     * Convert JsonNode to SearchContentInfo.
     */
    protected SearchContentInfo nodeToSearchDoc(JsonNode node) throws JsonProcessingException {
        SearchContentInfo.Builder builder = SearchContentInfo.newBuilder();
        Iterator<String> iterator = node.fieldNames();
        while (iterator.hasNext()) {
            String name = iterator.next();
            JsonNode ele = node.get(name);
            if (StringUtils.equals("score", name)) {
                builder.withScore(ele.asDouble());
            } else if (StringUtils.equals("data", name)) {
                builder.withSearchContentInfo(JsonUtils.parseObject(ele.toString(), ContentInfo.class));
            } else if (StringUtils.equals("documentSet", name)) {
                builder.withSearchDocumentSetInfo(nodeToSearchDocumentSet(ele));
            }
        }
        return builder.build();
    }

    /**
     * Convert JsonNode to SearchDocumentSetInfo.
     */
    protected SearchDocumentSetInfo nodeToSearchDocumentSet(JsonNode node) throws JsonProcessingException {
        SearchDocumentSetInfo.Builder builder = SearchDocumentSetInfo.newBuilder();
        Iterator<String> iterator = node.fieldNames();
        while (iterator.hasNext()) {
            String name = iterator.next();
            JsonNode ele = node.get(name);
            if (StringUtils.equals("documentSetName", name)) {
                builder.withDocumentSetName(ele.toString());
            } else if (StringUtils.equals("documentSetId", name)) {
                builder.withDocumentSetId(ele.toString());
            } else {
                if (ele.isInt()) {
                    builder.addDocField(new DocField(name, ele.asInt()));
                } else if (ele.isLong()) {
                    builder.addDocField(new DocField(name, ele.asLong()));
                } else if (ele.isArray()) {
                    List values = JsonUtils.parseObject(ele.toString(), List.class);
                    builder.addDocField(new DocField(name, values));
                } else {
                    builder.addDocField(new DocField(name, ele.asText()));
                }
            }
        }
        return builder.build();
    }

    /**
     * Convert JsonNode to Document.
     */
    protected Document nodeToDoc(JsonNode node) throws JsonProcessingException {
        Document.Builder builder = Document.newBuilder();
        Iterator<String> iterator = node.fieldNames();
        while (iterator.hasNext()) {
            String name = iterator.next();
            JsonNode ele = node.get(name);
            if (StringUtils.equals("id", name)) {
                builder.withId(ele.asText());
            } else if (StringUtils.equals("vector", name)) {
                List<Double> vector = JsonUtils.parseObject(ele.toString(), List.class);
                builder.withVector(vector);
            } else if (StringUtils.equals("sparse_vector", name)) {
                builder.withSparseVectorList(JsonUtils.parseObject(ele.toString(), List.class));
            } else if (StringUtils.equals("doc", name)) {
                builder.withDoc(ele.asText());
            } else if (StringUtils.equals("score", name)) {
                builder.withScore(ele.asDouble());
            } else if (StringUtils.equals("documentSet", name)) {
                builder.addFilterField(new DocField(name, nodeToDoc(ele)));
            } else {
                if (ele.isInt()) {
                    builder.addFilterField(new DocField(name, ele.asInt()));
                } else if (ele.isLong()) {
                    builder.addFilterField(new DocField(name, ele.asLong()));
                } else if (ele.isArray()) {
                    List values = JsonUtils.parseObject(ele.toString(), List.class);
                    builder.addFilterField(new DocField(name, values));
                } else if(ele.isObject()){
                    builder.addFilterField(new DocField(name, new JSONObject(ele.toString())));
                } else if (ele.isDouble() || ele.isFloat()) {
                    builder.addFilterField(new DocField(name, ele.asDouble()));
                } else {
                    builder.addFilterField(new DocField(name, ele.asText()));
                }
            }
        }
        return builder.build();
    }

    /**
     * Convert JsonNode to DocumentSet.
     */
    protected DocumentSet nodeToDocumentSet(JsonNode node) throws JsonProcessingException {
        DocumentSet.Builder builder = DocumentSet.newBuilder();
        Iterator<String> iterator = node.fieldNames();
        while (iterator.hasNext()) {
            String name = iterator.next();
            JsonNode ele = node.get(name);
            if (StringUtils.equals("documentSetId", name)) {
                builder.withDocumentSetId(ele.asText());
            } else if (StringUtils.equals("documentSetInfo", name)) {
                DocumentSetInfo documentSetInfo = JsonUtils.parseObject(ele.toString(), DocumentSetInfo.class);
                builder.withDocumentSetInfo(documentSetInfo);
            } else if (StringUtils.equals("documentSetName", name)) {
                builder.withDocumnetSetName(ele.asText());
            } else if (StringUtils.equals("textPrefix", name)) {
                builder.withTextPrefix(ele.asText());
            } else if (StringUtils.equals("splitterPreprocess", name)) {
                SplitterPreprocessParams splitterPreprocess = JsonUtils.parseObject(ele.toString(), SplitterPreprocessParams.class);
                builder.withSplitProcess(splitterPreprocess);
            } else if (StringUtils.equals("parsingProcess", name)) {
                ParsingProcessParam parsingProcessParam = JsonUtils.parseObject(ele.toString(), ParsingProcessParam.class);
                builder.withParsingProcess(parsingProcessParam);
            } else {
                if (ele.isInt()) {
                    builder.addFilterField(new DocField(name, ele.asInt()));
                } else if (ele.isLong()) {
                    builder.addFilterField(new DocField(name, ele.asLong()));
                } else if (ele.isArray()) {
                    List values = JsonUtils.parseObject(ele.toString(), List.class);
                    builder.addFilterField(new DocField(name, values));
                } else if (ele.isDouble() || ele.isFloat()) {
                    builder.addFilterField(new DocField(name, ele.asDouble()));
                } else if (ele.isObject()) {
                    builder.addFilterField(new DocField(name, new JSONObject(ele.toString())));
                } else {
                    builder.addFilterField(new DocField(name, ele.asText()));
                }
            }
        }
        return builder.build();
    }

    /**
     * Convert JsonNode to DocumentFileContent.
     */
    protected DocumentFileContent nodeToDocumentFileContent(JsonNode node) throws JsonProcessingException {
        DocumentFileContent documentFileContent = new DocumentFileContent();
        documentFileContent.setDocFields(new ArrayList<>());
        Iterator<String> iterator = node.fieldNames();
        while (iterator.hasNext()) {
            String name = iterator.next();
            JsonNode ele = node.get(name);
            if (StringUtils.equals("documentSetId", name)) {
                documentFileContent.setDocumentSetId(ele.asText());
            } else if (StringUtils.equals("documentSetInfo", name)) {
                DocumentSetInfo documentSetInfo = JsonUtils.parseObject(ele.toString(), DocumentSetInfo.class);
                documentFileContent.setDocumentSetInfo(documentSetInfo);
            } else if (StringUtils.equals("documentSetName", name)) {
                documentFileContent.setDocumentSetName(ele.asText());
            } else if (StringUtils.equals("text", name)) {
                documentFileContent.setText(ele.asText());
            } else if (StringUtils.equals("splitterPreprocess", name)) {
                SplitterPreprocessParams splitterPreprocess = JsonUtils.parseObject(ele.toString(), SplitterPreprocessParams.class);
                documentFileContent.setSplitterPreprocess(splitterPreprocess);
            } else {
                if (ele.isInt()) {
                    documentFileContent.addFilterField(new DocField(name, ele.asInt()));
                } else if (ele.isLong()) {
                    documentFileContent.addFilterField(new DocField(name, ele.asLong()));
                } else if (ele.isArray()) {
                    List values = JsonUtils.parseObject(ele.toString(), List.class);
                    documentFileContent.addFilterField(new DocField(name, values));
                } else {
                    documentFileContent.addFilterField(new DocField(name, ele.asText()));
                }
            }
        }
        return documentFileContent;
    }

    /**
     * Get upload URL for AI document processing.
     */
    protected UploadUrlRes getUploadUrl(String databaseName, String collectionViewName,
                                        LoadAndSplitTextParam loadAndSplitTextParam, String fileName) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_DOCUMENT_SET_UPLOADER_URL);
        Map<String, Object> params = new HashMap<>();
        params.put("database", databaseName);
        params.put("collectionView", collectionViewName);
        if (loadAndSplitTextParam.getDocumentSetName() != null) {
            params.put("documentSetName", loadAndSplitTextParam.getDocumentSetName());
        } else if (fileName != null) {
            params.put("documentSetName", fileName);
        }
        if (loadAndSplitTextParam.getParsingProcess() != null){
            params.put("parsingProcess", loadAndSplitTextParam.getParsingProcess());
        }
        if (loadAndSplitTextParam.getByteLength() != null){
             params.put("byteLength", loadAndSplitTextParam.getByteLength());
        }
        String body = JsonUtils.toJsonString(params);
        JsonNode jsonNode = this.post(url, body, true);
        return JsonUtils.parseObject(jsonNode.toString(), UploadUrlRes.class);
    }

    /**
     * Get upload URL for collection file upload.
     */
    protected CollectionUploadUrlRes getCollectionUploadUrl(String databaseName, String collection,
                                                            UploadFileParam loadAndSplitTextParam) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_DOCUMENT_UPLOADER_URL);
        Map<String, Object> params = new HashMap<>();
        params.put("database", databaseName);
        params.put("collection", collection);
        if (loadAndSplitTextParam.getFileName() != null) {
            params.put("fileName", loadAndSplitTextParam.getFileName());
        }
        if (loadAndSplitTextParam.getParsingProcess() != null){
            params.put("parsingProcess", loadAndSplitTextParam.getParsingProcess());
        }
        if (loadAndSplitTextParam.getSplitterProcess() != null){
            params.put("splitterPreprocess", loadAndSplitTextParam.getSplitterProcess());
        }
        if (loadAndSplitTextParam.getEmbeddingModel() != null){
            params.put("embeddingModel", loadAndSplitTextParam.getEmbeddingModel());
        }
        if (loadAndSplitTextParam.getFieldMappings() != null){
            params.put("fieldMappings", loadAndSplitTextParam.getFieldMappings());
        }
        if (loadAndSplitTextParam.getByteLength() != null){
            params.put("byteLength", loadAndSplitTextParam.getByteLength());
        }
        String body = JsonUtils.toJsonString(params);
        JsonNode jsonNode = this.post(url, body, true);
        return JsonUtils.parseObject(jsonNode.toString(), CollectionUploadUrlRes.class);
    }
}