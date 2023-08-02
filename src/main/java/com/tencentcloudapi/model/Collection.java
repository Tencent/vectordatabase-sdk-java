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
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Collection {

    @JsonIgnore
    private Stub stub;
    protected String database;
    protected String collection;
    protected int replicaNum = 2;
    protected int shardNum = 1;
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
