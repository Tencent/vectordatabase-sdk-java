package com.tencentcloudapi.model;

import com.tencentcloudapi.client.HttpClient;
import com.tencentcloudapi.model.param.Index;

import java.util.List;

/**
 * VectorDB Database
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class Database {
    private HttpClient httpClient;
    private String dbName;

    public void createDatabase(String databaseName, int timeout) {
    }

    public void dropDatabase(String databaseName, int timeout) {
    }

    public void listDatabase(String databaseName, int timeout) {
    }

    public Collection createCollection(String name, int shard, int replicas,
                                       String description, Index index, int timeout) {
        return null;
    }

    public List<Collection> listCollections(int timeout) {
        return null;
    }

    public Collection describeCollection(String name, int timeout) {
        return null;
    }

    public void dropCollection(String name, int timeout) {
    }

    public Collection collection(String name) {
        return null;
    }
}
