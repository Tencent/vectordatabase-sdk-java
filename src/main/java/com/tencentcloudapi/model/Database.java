package com.tencentcloudapi.model;

import com.tencentcloudapi.model.param.collection.index.Index;
import com.tencentcloudapi.service.Stub;

import java.util.List;

/**
 * VectorDB Database
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class Database {
    private Stub stub;
    private String dbName;

    public Database(Stub stub, String dbName) {
        this.stub = stub;
        this.dbName = dbName;
    }

    public void createDatabase(String databaseName) {
    }

    public void dropDatabase(String databaseName) {
    }

    public List<String> listDatabase(String databaseName) {
        return null;
    }

    public Collection createCollection(String name, int shard, int replicas,
                                       String description, Index index) {
        return null;
    }

    public List<Collection> listCollections() {
        return null;
    }

    public Collection describeCollection(String name) {
        return null;
    }

    public void dropCollection(String name) {
    }

    public Collection collection(String name) {
        return null;
    }
}
