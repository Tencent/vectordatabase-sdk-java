package com.tencentcloudapi.model;

import com.tencentcloudapi.service.Stub;

import java.util.List;

/**
 * VectorDB Database
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class Database {
    private Stub stub;
    private String databaseName;

    public Database(Stub stub, String databaseName) {
        this.stub = stub;
        this.databaseName = databaseName;
    }

    public void createDatabase() {
    }

    public void dropDatabase() {
    }

    public List<String> listDatabase() {
        return null;
    }

    public Collection createCollection() {
        return null;
    }

    public List<Collection> listCollections() {
        return null;
    }

    public Collection describeCollection(String databaseName) {
        return null;
    }

    public void dropCollection(String databaseName) {
    }

    public Collection collection(String databaseName) {
        return null;
    }
}
