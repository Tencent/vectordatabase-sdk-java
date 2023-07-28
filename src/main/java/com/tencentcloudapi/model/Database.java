package com.tencentcloudapi.model;

import com.tencentcloudapi.exception.VectorDBException;
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

    public String getDatabaseName() {
        return databaseName;
    }

    public Collection createCollection(CreateCollectionParam param) throws VectorDBException {
        param.setDatabase(databaseName);
        stub.createCollection(param);
        param.setStub(this.stub);
        return (Collection)param;
    }

    public List<Collection> listCollections() throws VectorDBException {
        List<Collection> collections = stub.listCollections(this.databaseName);
        collections.forEach(c -> c.setStub(stub));
        return collections;
    }

    public Collection describeCollection(String collectionName) throws VectorDBException {
        Collection collection = stub.describeCollection(this.databaseName, collectionName);
        collection.setStub(stub);
        return collection;
    }

    public void dropCollection(String collectionName) throws VectorDBException {
        stub.dropCollection(this.databaseName, collectionName);
    }

    public Collection collection(String collectionName) throws VectorDBException {
        return describeCollection(collectionName);
    }

    @Override
    public String toString() {
        return String.format("{\"database\":\"%s\"}", this.databaseName);
    }
}
