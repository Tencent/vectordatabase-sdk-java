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

package com.tencent.tcvectordb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.tcvectordb.exception.ParamException;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.param.collection.Embedding;
import com.tencent.tcvectordb.model.param.collection.IndexField;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.entity.BaseRes;
import com.tencent.tcvectordb.model.param.entity.SearchRes;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.service.Stub;
import com.tencent.tcvectordb.service.param.*;

import java.util.Date;
import java.util.List;

/**
 * VectorDB Collection
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Collection{
    @JsonIgnore
    private Stub stub;
    private String database;
    protected String collection;
    protected int replicaNum = 2;
    protected int shardNum = 1;
    protected String description;
    protected List<IndexField> indexes;
    private String createTime;
    @JsonIgnore
    protected ReadConsistencyEnum readConsistency;
    private long documentCount;
    private Collection.IndexStatus indexStatus;
    private List<String> alias;
    protected Embedding embedding;

    public void setStub(Stub stub) {
        this.stub = stub;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getCollection() {
        return collection;
    }

    public int getReplicaNum() {
        return replicaNum;
    }

    public int getShardNum() {
        return shardNum;
    }

    public String getDescription() {
        return description;
    }

    public List<IndexField> getIndexes() {
        return indexes;
    }

    public String getCreateTime() {
        return createTime;
    }

    public long getDocumentCount() {
        return documentCount;
    }

    public Collection.IndexStatus getIndexStatus() {
        return indexStatus;
    }

    public List<String> getAlias() {
        return alias;
    }

    public ReadConsistencyEnum getReadConsistency() {
        return readConsistency;
    }

    public void setReadConsistency(ReadConsistencyEnum readConsistency) {
        this.readConsistency = readConsistency;
    }

    public Embedding getEmbedding() {
        return embedding;
    }

    public void setEmbedding(Embedding embedding) {
        this.embedding = embedding;
    }

    public AffectRes upsert(InsertParam param) throws VectorDBException {
        InsertParamInner insertParam = new InsertParamInner(
                database, collection, param);
        return this.stub.upsertDocument(insertParam);
    }

    public List<Document> query(QueryParam param) throws VectorDBException {
        return this.stub.queryDocument(
                new QueryParamInner(database, collection, param, this.readConsistency));
    }

    public List<List<Document>> search(SearchByVectorParam param) throws VectorDBException {
        return this.stub.searchDocument(new SearchParamInner(
                database, collection, param, this.readConsistency), DataBaseTypeEnum.BASE).getDocuments();
    }

    public List<List<Document>> searchById(SearchByIdParam param) throws VectorDBException {
        return this.stub.searchDocument(new SearchParamInner(
                database, collection, param, this.readConsistency), DataBaseTypeEnum.BASE).getDocuments();
    }

    public SearchRes searchByEmbeddingItems(SearchByEmbeddingItemsParam param) throws VectorDBException {
        return this.stub.searchDocument(new SearchParamInner(
                database, collection, param, this.readConsistency), DataBaseTypeEnum.BASE);
    }

    public AffectRes delete(DeleteParam param) throws VectorDBException {
        return this.stub.deleteDocument(
                new DeleteParamInner(database, collection, param));
    }

    public AffectRes update(UpdateParam param, Document document) throws VectorDBException {
        return this.stub.updateDocument(
                new UpdateParamInner(database, collection, param, document));
    }

    public BaseRes rebuildIndex(RebuildIndexParam rebuildIndexParam) {
        return this.stub.rebuildIndex(new RebuildIndexParamInner(database, collection, rebuildIndexParam));
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
