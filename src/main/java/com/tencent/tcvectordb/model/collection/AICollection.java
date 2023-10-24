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
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.*;
import com.tencent.tcvectordb.service.param.*;

import java.util.*;

/**
 * VectorDB Collection
 */
public class AICollection extends BaseCollection{
    private AIConfig aiConfig;
    private AIStatus aiStatus;

    public AIStatus getAiStatus() {
        return aiStatus;
    }

    public void setAiStatus(AIStatus aiStatus) {
        this.aiStatus = aiStatus;
    }

    public AICollection() {
    }

    public AIConfig getAiConfig() {
        return aiConfig;
    }

    public void setAiConfig(AIConfig aiConfig) {
        this.aiConfig = aiConfig;
    }

    public List<Document> query(QueryParam param) throws VectorDBException {
        return this.stub.queryAIDocument(
                new QueryParamInner(database, collection, param, this.readConsistency));
    }

    public List<Document> search(SearchParam param) throws VectorDBException {
        if(param.isRetrieveVector()){
           throw new VectorDBException("can not retrieve vector");
        }
        return this.stub.searchAIDocument(new SearchParamInner(
                database, collection, param, this.readConsistency)).getDocuments();
    }

    public List<List<Document>> searchById(SearchByIdParam param) throws VectorDBException {
        throw new VectorDBException("can not support search by id");
    }

    public AffectRes delete(DeleteParam param) throws VectorDBException {
        return this.stub.deleteAIDocument(
                new DeleteParamInner(database, collection, param));
    }

    public AffectRes update(UpdateParam param, Document document) throws VectorDBException {
        return this.stub.updateAIDocument(
                new UpdateParamInner(database, collection, param, document));
    }

    public void upload(String databaseName, String collectionName, String filePath) throws VectorDBException {
        this.stub.upload(databaseName, collectionName, filePath);
    }

    public BaseRes rebuildIndex(RebuildIndexParam rebuildIndexParam) throws VectorDBException{
        throw new VectorDBException("can not support rebuild");
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
