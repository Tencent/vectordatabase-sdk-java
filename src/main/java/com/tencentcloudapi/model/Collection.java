package com.tencentcloudapi.model;

import com.tencentcloudapi.model.param.collection.index.Index;
import com.tencentcloudapi.model.param.dml.*;
import com.tencentcloudapi.service.Stub;

import java.util.List;

/**
 * VectorDB Document
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class Collection {

    private final Stub stub;
    private final String databaseName;
    private final String collectionName;
    private int shard = 0;
    private int replicas = 0;
    private String description;
    private Index index;
    private String createTime;

    public Collection(Stub stub, String databaseName, String collectionName) {
        this.stub = stub;
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }

    public void setShard(int shard) {
        this.shard = shard;
    }

    public void setReplicas(int replicas) {
        this.replicas = replicas;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void upsert(List<Document> documents) {
        this.stub.upsertDocument(documents);
    }

    public List<Document> query(QueryParam param) {
        return this.stub.queryDocument(param);
    }

    public List<List<Document>> search(SearchParam param) {
        return this.stub.searchDocument(param);
    }

    public List<Document> searchById(SearchByIdParam param) {
        return this.stub.searchDocumentById(param);
    }

    public void delete(List<String> documentIds) {
        this.stub.deleteDocument(documentIds);
    }
}
