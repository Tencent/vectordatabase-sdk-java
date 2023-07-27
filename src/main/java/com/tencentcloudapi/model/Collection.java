package com.tencentcloudapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencentcloudapi.exception.ParamException;
import com.tencentcloudapi.exception.VectorDBException;
import com.tencentcloudapi.model.param.collection.IndexField;
import com.tencentcloudapi.model.param.dml.*;
import com.tencentcloudapi.model.param.dml.InsertParam;
import com.tencentcloudapi.service.Stub;
import com.tencentcloudapi.service.param.DeleteParamInner;
import com.tencentcloudapi.service.param.InsertParamInner;
import com.tencentcloudapi.service.param.QueryParamInner;
import com.tencentcloudapi.service.param.SearchParamInner;

import java.util.List;

/**
 * VectorDB Collection
 * User: wlleiiwang
 * Date: 2023/7/24
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Collection {

    @JsonIgnore
    private Stub stub;
    protected String database;
    protected String collection;
    protected int replicaNum = 0;
    protected int shardNum = 0;
    protected String description;
    protected List<IndexField> indexes;
    protected String createTime;

    protected Collection() {
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

    public void upsert(InsertParam param) throws VectorDBException {
        InsertParamInner insertParam = new InsertParamInner(
                database, collection, param.getDocuments());
        this.stub.upsertDocument(insertParam);
    }

    public List<Document> query(QueryParam param) throws VectorDBException {
        return this.stub.queryDocument(
                new QueryParamInner(database, collection, param));
    }

    public List<List<Document>> search(SearchByVectorParam param) throws VectorDBException {
        return this.stub.searchDocument(new SearchParamInner(
                database, collection, param));
    }

    public List<List<Document>> searchById(SearchByIdParam param) throws VectorDBException {
        return this.stub.searchDocument(new SearchParamInner(
                database, collection, param));
    }

    public void delete(DeleteParam param) throws VectorDBException {
        this.stub.deleteDocument(
                new DeleteParamInner(database, collection, param));
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
}
