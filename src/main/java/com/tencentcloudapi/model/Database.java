package com.tencentcloudapi.model;

import com.tencentcloudapi.model.param.collection.CreateCollectionParam;
import com.tencentcloudapi.service.Stub;

import java.util.List;

/**
 * VectorDB Database
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class Database {
    private final Stub stub;
    private final String databaseName;

    public Database(Stub stub, String databaseName) {
        this.stub = stub;
        this.databaseName = databaseName;
    }

    public void createDatabase() {
        stub.createDatabase(this);
    }

    public void dropDatabase() {
        stub.dropDatabase(this);
    }

    public List<String> listDatabase() {
        return stub.listDatabases();
    }

    public Collection createCollection(CreateCollectionParam param) {
        return stub.createCollection(param);
    }

    public List<Collection> listCollections() {
        return stub.listCollections(this.databaseName);
    }

    public Collection describeCollection(String collectionName) {
        return stub.describeCollection(this.databaseName, collectionName);
    }

    public void dropCollection(String collectionName) {
        stub.dropCollection(this.databaseName, collectionName);
    }

    public Collection collection(String collectionName) {
        return describeCollection(collectionName);
    }

    @Override
    public String toString() {
        return String.format("{\"databases\":\"%s\"}", this.databaseName);
    }
}
