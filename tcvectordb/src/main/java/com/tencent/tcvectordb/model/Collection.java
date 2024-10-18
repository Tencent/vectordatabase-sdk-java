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
import com.tencent.tcvectordb.model.param.collection.FilterIndexConfig;
import com.tencent.tcvectordb.model.param.collection.IndexField;
import com.tencent.tcvectordb.model.param.collection.TTLConfig;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.entity.BaseRes;
import com.tencent.tcvectordb.model.param.entity.HybridSearchRes;
import com.tencent.tcvectordb.model.param.entity.SearchRes;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.service.Stub;
import com.tencent.tcvectordb.service.param.*;
import org.json.JSONObject;
import org.json.Property;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * VectorDB Collection
 *
 * <ol>
 * <li> database: the database name of collection </li>
 * <li> collection: name of collection, this name must be unique</li>
 * <li> replicaNum：the replica num of the collection, The number of replicas is limited by the instance type.
 *  eg: replicaNum must be zero if instance is free; the other instance's replicaNum must larger than zero </li>
 * <li> shardNum: the shard num of the collection, the data of the collection will be split into shardNum parts</li>
 * <li> description: description of the collection,</li>
 * <li> indexes: index field of the collection;
 *      field type could be string，uint64, array, vector and sparse vector.
 *      index type could be primaryKey, filter if the field is scalar field, id field must be primaryKey;
 *      index type could be FLAT, HNSW ... if the filed type is vector;
 *      index type should be inverted if the field type is sparse vector;
 *      metric type must be set if the field type is vector or sparse vector;
 *      fieldElementType could be string if the field type array;
 *      dimension must be set if the filed type is vector;
 *      </li>,
 *
 * <li> alias: alias of the collection </li>
 * <li> embedding: embedding config should be set if collection use embedding function </li>
 * <li> TTLConfig: ttl config should be set if collection use ttl function </li>
 * </ol>
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Collection{
    @JsonIgnore
    private Stub stub;
    protected String database;
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

    protected TTLConfig ttlConfig;

    protected FilterIndexConfig filterIndexConfig;

    public TTLConfig getTtlConfig() {
        return ttlConfig;
    }

    public void setTtlConfig(TTLConfig ttlConfig) {
        this.ttlConfig = ttlConfig;
    }

    public FilterIndexConfig getFilterIndexConfig() {
        return filterIndexConfig;
    }

    public void setFilterIndexConfig(FilterIndexConfig filterIndexConfig) {
        this.filterIndexConfig = filterIndexConfig;
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

    public void setCollection(String collection) {
        this.collection = collection;
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

    public void setDocumentCount(long documentCount) {
        this.documentCount = documentCount;
    }

    public void setIndexStatus(IndexStatus indexStatus) {
        this.indexStatus = indexStatus;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
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
        boolean ai = false;
        if((param.getDocuments().get(0)!=null)){
            if (param.getDocuments().get(0) instanceof Document){
                if (((Document)param.getDocuments().get(0)).getVector() instanceof String){
                    ai = true;
                }
            }
            if (param.getDocuments().get(0) instanceof JSONObject){
                if (((JSONObject)param.getDocuments().get(0)).get("vector") instanceof String){
                    ai = true;
                }
            }
        }
        return this.stub.upsertDocument(insertParam, ai);
    }

    public List<Document> query(QueryParam param) throws VectorDBException {
        boolean ai = false;
        return this.stub.queryDocument(
                new QueryParamInner(database, collection, param, this.readConsistency), ai);
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

    public HybridSearchRes hybridSearch(HybridSearchParam param) throws VectorDBException {
        boolean ai = false;
        if(param.getAnn()!=null && !param.getAnn().isEmpty() && param.getAnn().get(0).getData()!=null
                && !param.getAnn().get(0).getData().isEmpty()
                && param.getAnn().get(0).getData().get(0) instanceof String){
            ai = true;
        }
        return this.stub.hybridSearchDocument(new HybridSearchParamInner(
                database, collection, param, this.readConsistency), ai);
    }

    public AffectRes delete(DeleteParam param) throws VectorDBException {
        return this.stub.deleteDocument(
                new DeleteParamInner(database, collection, param));
    }

    public AffectRes update(UpdateParam param, Document document) throws VectorDBException {
        boolean ai = false;
        if (document.getVector() instanceof String){
            ai = true;
        }
        return this.stub.updateDocument(
                new UpdateParamInner(database, collection, param, document), ai);
    }

    public AffectRes update(UpdateParam param, JSONObject document) throws VectorDBException {
        boolean ai = false;
        if (document.get("vector") instanceof String){
            ai = true;
        }
        return this.stub.updateDocument(
                new UpdateParamInner(database, collection, param, document), ai);
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

        public IndexStatus() {
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        public IndexStatus(String status, String startTime) {
            this.status = status;
            if (!startTime.isEmpty()){
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
