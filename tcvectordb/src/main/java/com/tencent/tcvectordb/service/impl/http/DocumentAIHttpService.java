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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.tencent.tcvectordb.enums.Code;
import com.tencent.tcvectordb.exception.ParamException;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.param.collection.UploadFileParam;
import com.tencent.tcvectordb.model.param.collectionView.LoadAndSplitTextParam;
import com.tencent.tcvectordb.model.param.entity.*;
import com.tencent.tcvectordb.model.param.collectionView.FileType;
import com.tencent.tcvectordb.service.ApiPath;
import com.tencent.tcvectordb.service.param.*;
import com.tencent.tcvectordb.utils.FileUtils;
import com.tencent.tcvectordb.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * HTTP service implementation for AI document processing operations.
 * Handles file upload, document management, and AI document processing.
 */
public class DocumentAIHttpService extends BaseHttpService {

    /**
     * Upload and process text files for AI document processing.
     */
    public void upload(String databaseName, String collectionViewName, LoadAndSplitTextParam loadAndSplitTextParam,
                       Map<String, Object> metaDataMap) throws Exception {
        File file = null;
        String fileName = "";
        String fileType = "";
        if (loadAndSplitTextParam.getLocalFilePath() != null){
            file = new File(loadAndSplitTextParam.getLocalFilePath());
            if (!file.exists() || !file.isFile()) {
                throw new VectorDBException("file is not existed");
            }

            if (file.length() <= 0) {
                throw new VectorDBException("file is empty");
            }
            fileName = file.getName();
            fileType = FileUtils.getFileType(file);
            loadAndSplitTextParam.setByteLength(file.length());
        } else if(loadAndSplitTextParam.getFileInputStream() != null){
            if (loadAndSplitTextParam.getDocumentSetName() == null || loadAndSplitTextParam.getFileType() == null
                    || loadAndSplitTextParam.getInputStreamSize() == null){
                throw new VectorDBException("use input stream, documentSetName、inputStreamSize and file type can not be null");
            }
            fileType = loadAndSplitTextParam.getFileType();
            loadAndSplitTextParam.setByteLength(loadAndSplitTextParam.getInputStreamSize());
        }

        UploadUrlRes uploadUrlRes = getUploadUrl(databaseName, collectionViewName, loadAndSplitTextParam, fileName);

        if (Code.isFailed(uploadUrlRes.getCode()) ||
                uploadUrlRes.getCredentials() == null ||
                uploadUrlRes.getCredentials().getTmpSecretId().equals("") ||
                uploadUrlRes.getUploadCondition() == null ||
                uploadUrlRes.getUploadCondition().getMaxSupportContentLength() == 0) {
            String msg = StringUtils.isNotBlank(uploadUrlRes.getMsg()) ? ", " + uploadUrlRes.getMsg() : "";
            throw new VectorDBException("get file upload url failed" + msg);
        }

        String filePath = loadAndSplitTextParam.getLocalFilePath();
        int maxLength = uploadUrlRes.getUploadCondition().getMaxSupportContentLength();

        if (file != null && file.length() > maxLength) {
            throw new ParamException(String.format("%s file is too large, max size is %d bytes", filePath, maxLength));
        }

        String uploadPath = uploadUrlRes.getUploadPath();
        String bucket = uploadUrlRes.getCosBucket();
        String region = uploadUrlRes.getCosRegion();
        BasicSessionCredentials cred = new BasicSessionCredentials(uploadUrlRes.getCredentials().getTmpSecretId(),
                uploadUrlRes.getCredentials().getTmpSecretKey(), uploadUrlRes.getCredentials().getToken());
        ClientConfig cosClientConfig = new ClientConfig(new Region(region));
        String cosEndPoint = uploadUrlRes.getCosEndpoint().split("\\.",2)[1];
        cosClientConfig.setEndpointBuilder(new CosEndpointBuilder(cosEndPoint));
        COSClient cosClient = new COSClient(cred, cosClientConfig);
        PutObjectRequest putObjectRequest = null;
        ObjectMetadata metadata = new ObjectMetadata();

        if (file != null && file.exists()){
            putObjectRequest = new PutObjectRequest(bucket, uploadPath, file);
        } else if (loadAndSplitTextParam.getFileInputStream() != null){
            metadata.setContentLength(loadAndSplitTextParam.getInputStreamSize());
            putObjectRequest = new PutObjectRequest(bucket, uploadPath, loadAndSplitTextParam.getFileInputStream(), null);
        } else {
            throw new VectorDBException("file or inputStream not exist ");
        }


        if (!Arrays.asList(FileType.MD, FileType.WORD).contains(fileType) &&
                Objects.nonNull(loadAndSplitTextParam.getSplitterProcess()) &&
                StringUtils.isNotEmpty(loadAndSplitTextParam.getSplitterProcess().getChunkSplitter())) {
            logger.warn("only markdown files are allowed to use chunkSplitter");
        }
        metadata.addUserMetadata("fileType", fileType);
        metadata.addUserMetadata("id", uploadUrlRes.getDocumentSetId());
        if (metaDataMap == null || metaDataMap.isEmpty()) {
            metaDataMap = new HashMap<>();
        }
        String metaJson = URLEncoder.encode(Base64.getEncoder().encodeToString(JsonUtils.toJsonString(metaDataMap).getBytes(StandardCharsets.UTF_8)),
                String.valueOf(StandardCharsets.UTF_8));
        metadata.addUserMetadata("data", metaJson);

        Map<String, Object> config = new HashMap<>();
        if (loadAndSplitTextParam.getSplitterProcess() != null) {

            config.put("appendTitleToChunk", loadAndSplitTextParam.getSplitterProcess().isAppendTitleToChunk());
            config.put("appendKeywordsToChunk", loadAndSplitTextParam.getSplitterProcess().isAppendKeywordsToChunk());
            if (loadAndSplitTextParam.getSplitterProcess().getChunkSplitter() != null) {
                config.put("chunkSplitter", loadAndSplitTextParam.getSplitterProcess().getChunkSplitter());
            }
        }
        if (loadAndSplitTextParam.getParsingProcess() != null){
            config.put("parsingProcess", loadAndSplitTextParam.getParsingProcess());
        }

        if(config.size() > 0){
            metadata.addUserMetadata("config", URLEncoder.encode(Base64.getEncoder().encodeToString(JsonUtils.toJsonString(config).getBytes(StandardCharsets.UTF_8)),
                    String.valueOf(StandardCharsets.UTF_8)));
        }


        if (JsonUtils.toJsonString(metadata).length() > 2048) {
            throw new VectorDBException("cos header for param MetaData is too large, it can not be more than 2k");
        }
        putObjectRequest.withMetadata(metadata);

        putObjectRequest.withKey(uploadPath);

        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

        logger.debug("upload file, response:%s", JsonUtils.toJsonString(putObjectResult));
        cosClient.shutdown();
    }

    /**
     * Upload files to a collection for processing.
     */
    public void collectionUpload(String databaseName, String collectionName, UploadFileParam loadAndSplitTextParam,
                                 Map<String, Object> metaDataMap) throws Exception {
        File file = null;
        if (loadAndSplitTextParam.getLocalFilePath() != null){
            file = new File(loadAndSplitTextParam.getLocalFilePath());
            if (!file.exists() || !file.isFile()) {
                throw new VectorDBException("file is not existed");
            }

            if (file.length() <= 0) {
                throw new VectorDBException("file is empty");
            }
            loadAndSplitTextParam.setByteLength(file.length());
        } else if(loadAndSplitTextParam.getFileInputStream() != null){
            if (loadAndSplitTextParam.getFileName() == null || loadAndSplitTextParam.getInputStreamSize() == null){
                throw new VectorDBException("use input stream, fileName and inputStreamSize  can not be null");
            }
            loadAndSplitTextParam.setByteLength(loadAndSplitTextParam.getInputStreamSize());
        }

        CollectionUploadUrlRes uploadUrlRes = getCollectionUploadUrl(databaseName, collectionName, loadAndSplitTextParam);

        if (Code.isFailed(uploadUrlRes.getCode()) ||
                uploadUrlRes.getCredentials() == null ||
                uploadUrlRes.getCredentials().getTmpSecretId().equals("") ||
                uploadUrlRes.getUploadCondition() == null ||
                uploadUrlRes.getUploadCondition().getMaxSupportContentLength() == 0) {
            String msg = StringUtils.isNotBlank(uploadUrlRes.getMsg()) ? ", " + uploadUrlRes.getMsg() : "";
            throw new VectorDBException("get file upload url failed" + msg);
        }

        String filePath = loadAndSplitTextParam.getLocalFilePath();
        int maxLength = uploadUrlRes.getUploadCondition().getMaxSupportContentLength();

        if (file != null && file.length() > maxLength) {
            throw new ParamException(String.format("%s file is too large, max size is %d bytes", filePath, maxLength));
        }

        String uploadPath = uploadUrlRes.getUploadPath();
        String bucket = uploadUrlRes.getCosBucket();
        String region = uploadUrlRes.getCosRegion();
        BasicSessionCredentials cred = new BasicSessionCredentials(uploadUrlRes.getCredentials().getTmpSecretId(),
                uploadUrlRes.getCredentials().getTmpSecretKey(), uploadUrlRes.getCredentials().getToken());
        ClientConfig cosClientConfig = new ClientConfig(new Region(region));
        String cosEndPoint = uploadUrlRes.getCosEndpoint().split("\\.",2)[1];
        cosClientConfig.setEndpointBuilder(new CosEndpointBuilder(cosEndPoint));
        COSClient cosClient = new COSClient(cred, cosClientConfig);
        PutObjectRequest putObjectRequest = null;
        ObjectMetadata metadata = new ObjectMetadata();

        if (file != null && file.exists()){
            putObjectRequest = new PutObjectRequest(bucket, uploadPath, file);
        } else if (loadAndSplitTextParam.getFileInputStream() != null){
            metadata.setContentLength(loadAndSplitTextParam.getInputStreamSize());
            putObjectRequest = new PutObjectRequest(bucket, uploadPath, loadAndSplitTextParam.getFileInputStream(), null);
        } else {
            throw new VectorDBException("file or inputStream not exist ");
        }

        if (metaDataMap == null || metaDataMap.isEmpty()) {
            metaDataMap = new HashMap<>();
        }
        String metaJson = URLEncoder.encode(Base64.getEncoder().encodeToString(JsonUtils.toJsonString(metaDataMap).getBytes(StandardCharsets.UTF_8)),
                String.valueOf(StandardCharsets.UTF_8));
        metadata.addUserMetadata("data", metaJson);

        Map<String, Object> config = new HashMap<>();
        if (loadAndSplitTextParam.getSplitterProcess() != null) {

            config.put("appendTitleToChunk", loadAndSplitTextParam.getSplitterProcess().isAppendTitleToChunk());
            config.put("appendKeywordsToChunk", loadAndSplitTextParam.getSplitterProcess().isAppendKeywordsToChunk());
            if (loadAndSplitTextParam.getSplitterProcess().getChunkSplitter() != null) {
                config.put("chunkSplitter", loadAndSplitTextParam.getSplitterProcess().getChunkSplitter());
            }
        }
        if (loadAndSplitTextParam.getParsingProcess() != null){
            config.put("parsingProcess", loadAndSplitTextParam.getParsingProcess());
        }

        if(config.size() > 0){
            metadata.addUserMetadata("config", URLEncoder.encode(Base64.getEncoder().encodeToString(JsonUtils.toJsonString(config).getBytes(StandardCharsets.UTF_8)),
                    String.valueOf(StandardCharsets.UTF_8)));
        }


        if (JsonUtils.toJsonString(metadata).length() > 2048) {
            throw new VectorDBException("cos header for param MetaData is too large, it can not be more than 2k");
        }
        putObjectRequest.withMetadata(metadata);

        putObjectRequest.withKey(uploadPath);

        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

        logger.debug("upload file, response:%s", JsonUtils.toJsonString(putObjectResult));
        cosClient.shutdown();
    }

    /**
     * Retrieve file information from a collection.
     */
    public GetDocumentSetRes getFile(String databaseName, String collectionName, String documentSetName, String documentSetId) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_GET_FILE);
        String body = String.format("{\"database\":\"%s\",\"collectionView\":\"%s\",\"documentSetName\":\"%s\"," +
                        "\"documentSetId\":\"%s\"}",
                databaseName, collectionName, documentSetName, documentSetId);
        JsonNode jsonNode = this.post(url, body, true);
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
            res.setDocumentSet(nodeToDocumentFileContent(multiDocsNode));
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format("VectorDBServer response " +
                    "from search error: can't parse documents=%s", multiDocsNode));
        }
        return res;
    }

    /**
     * Get document chunks with pagination support.
     */
    public GetChunksRes getChunks(String databaseName, String collectionName, String documentSetName, String documentSetId,
                                  Integer limit, Integer offset) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_DOCUMENT_GET_CHUNKS);
        Map<String, Object> params = new HashMap<>();
        params.put("database", databaseName);
        params.put("collectionView", collectionName);
        if (documentSetName != null) {
            params.put("documentSetName", documentSetName);
        }
        if (documentSetId != null) {
            params.put("documentSetId", documentSetId);
        }
        if (limit != null) {
            params.put("limit", limit);
        }
        if (offset != null) {
            params.put("offset", offset);
        }
        String body = JsonUtils.toJsonString(params);
        JsonNode jsonNode = this.post(url, body, true);
        return JsonUtils.collectionDeserializer(jsonNode.toString(), new TypeReference<GetChunksRes>() {
        });
    }

    /**
     * Get image URL for document processing.
     */
    public GetImageUrlRes getImageUrl(GetImageUrlParamInner param) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_DOCUMENT_IMAGE_URL);
        JsonNode jsonNode = this.post(url, JsonUtils.toJsonString(param), false);
        return JsonUtils.parseObject(jsonNode.toString(), GetImageUrlRes.class);
    }

    /**
     * Query detailed file information.
     */
    public QueryFileDetailRes queryFileDetails(QueryFileDetailsParamInner param) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_DOCUMENT_QUERY_FILE_DETAILS);
        JsonNode jsonNode = this.post(url, JsonUtils.toJsonString(param), false);
        return JsonUtils.parseObject(jsonNode.toString(), QueryFileDetailRes.class);
    }
}