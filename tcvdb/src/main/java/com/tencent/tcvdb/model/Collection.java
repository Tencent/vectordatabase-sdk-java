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

package com.tencent.tcvdb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvdb.exception.VectorDBException;
import com.tencent.tcvdb.model.param.collection.IndexField;
import com.tencent.tcvdb.model.param.collection.WordsEmbeddingParam;
import com.tencent.tcvdb.model.param.dml.*;
import com.tencent.tcvdb.model.param.entity.AffectRes;
import com.tencent.tcvdb.model.param.entity.BaseRes;
import com.tencent.tcvdb.model.param.entity.SearchRes;
import com.tencent.tcvdb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvdb.service.Stub;
import com.tencent.tcvdb.service.param.*;
import com.tencent.tcvdb.utils.JsonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * VectorDB Collection
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Collection {
    @JsonIgnore
    protected Stub stub;
    protected String database;
    protected String collection;
    protected int replicaNum = 2;
    protected int shardNum = 1;
    protected String description;
    protected List<IndexField> indexes;
    private String createTime;
    @JsonIgnore
    protected ReadConsistencyEnum readConsistency;
    protected Long documentCount;
    protected IndexStatus indexStatus;
    protected List<String> alias;

    protected WordsEmbeddingParam wordsEmbedding;

    public WordsEmbeddingParam getWordsEmbedding() {
        return wordsEmbedding;
    }

    public void setWordsEmbedding(WordsEmbeddingParam wordsEmbedding) {
        this.wordsEmbedding = wordsEmbedding;
    }

    public void setStub(Stub stub) {
        this.stub = stub;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setCollection(String collection) {
        this.collection = collection;
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

    public Long getDocumentCount() {
        return documentCount;
    }

    public IndexStatus getIndexStatus() {
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

    public void setReplicaNum(int replicaNum) {
        this.replicaNum = replicaNum;
    }

    public void setShardNum(int shardNum) {
        this.shardNum = shardNum;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIndexes(List<IndexField> indexes) {
        this.indexes = indexes;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setIndexStatus(IndexStatus indexStatus) {
        this.indexStatus = indexStatus;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    public void setDocumentCount(Long documentCount) {
        this.documentCount = documentCount;
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

    public SearchRes search(SearchParam param) throws VectorDBException {
        return this.stub.searchDocument(new SearchParamInner(
                database, collection, param, this.readConsistency));
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
        return JsonUtils.toJsonString(this);
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

        public IndexStatus() {
        }

        public IndexStatus(String status, String startTime) {
            this.status = status;
            String formatPattern = "yyyy-MM-dd HH:mm:ss";

            SimpleDateFormat dateFormat = new SimpleDateFormat(formatPattern);
            try {
                Date date = dateFormat.parse(startTime);
                this.startTime = date;
                System.out.println("Parsed Date: " + date);
            } catch (ParseException e) {
                System.err.println("Failed to parse date: " + e.getMessage());
            }

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
