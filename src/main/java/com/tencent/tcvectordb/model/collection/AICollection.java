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

package com.tencent.tcvectordb.model.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.tcvectordb.exception.ParamException;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.collection.DocumentPreprocessParams;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.*;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.service.param.*;

import java.util.*;

/**
 * VectorDB Collection
 */
public class AICollection extends BaseCollection{
    private AIStatus aiStatus;
    private int expectedFileNum;
    // 文件的平均大小
    private int averageFileSize;
    private String language;
    private DocumentPreprocessParams documentPreprocess;

    public int getExpectedFileNum() {
        return expectedFileNum;
    }

    public void setExpectedFileNum(int expectedFileNum) {
        this.expectedFileNum = expectedFileNum;
    }

    public int getAverageFileSize() {
        return averageFileSize;
    }

    public void setAverageFileSize(int averageFileSize) {
        this.averageFileSize = averageFileSize;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public DocumentPreprocessParams getDocumentPreprocess() {
        return documentPreprocess;
    }

    public void setDocumentPreprocess(DocumentPreprocessParams documentPreprocess) {
        this.documentPreprocess = documentPreprocess;
    }

    public AIStatus getAiStatus() {
        return aiStatus;
    }

    public void setAiStatus(AIStatus aiStatus) {
        this.aiStatus = aiStatus;
    }

    public AICollection() {
    }

    public List<Document> query(QueryParam param) throws VectorDBException {
        return this.stub.queryAIDocument(
                new QueryParamInner(database, collection, param, this.readConsistency));
    }

    public List<Document> search(SearchByContentsParam param) throws VectorDBException {
        return this.stub.searchAIDocument(new SearchParamInner(
                database, collection, param, this.readConsistency)).getDocuments();
    }

    public AffectRes delete(DeleteParam param) throws VectorDBException {
        return this.stub.deleteAIDocument(
                new DeleteParamInner(database, collection, param));
    }

    public AffectRes update(UpdateParam param, Document document) throws VectorDBException {
        return this.stub.updateAIDocument(
                new UpdateParamInner(database, collection, param, document));
    }

    public void upload(String databaseName, String collectionName, String filePath, Map<String, Object> metaDataMap) throws Exception {
        this.stub.upload(databaseName, collectionName, filePath, metaDataMap);
    }

    public GetFileRes getFile(String databaseName, String collectionName, String fileName, String fileId){
        return this.stub.getFile(databaseName, collectionName, fileName, fileId);
    }

    public BaseRes rebuildIndex(RebuildIndexParam rebuildIndexParam) throws VectorDBException{
        return this.stub.rebuildAIIndex(new RebuildIndexParamInner(this.database, this.collection, rebuildIndexParam));
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new ParamException(String.format(
                    "Create collection param error: %s", e));
        }
    }


    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class IndexStatus {
        private String status;
        private Date startTime;

        public String getStatus() {
            return status;
        }

        public Date getStartTime() {
            return startTime;
        }

        @Override
        public String toString() {
            return "IndexStatus{" +
                    "status='" + status + '\'' +
                    ", startTime=" + startTime +
                    '}';
        }
    }
}
