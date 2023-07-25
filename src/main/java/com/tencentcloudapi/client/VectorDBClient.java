package com.tencentcloudapi.client;

import com.tencentcloudapi.model.Database;
import com.tencentcloudapi.model.param.ConnectParam;

import java.util.List;

/**
 * VectorDB Client
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class VectorDBClient {

    private HttpClient httpClient;

    public VectorDBClient(ConnectParam connectParam) {
        httpClient = new HttpClient();
    }

    public Database createDatabase(String databaseName) {
        Database db = new Database(httpClient, databaseName);
        db.createDatabase(databaseName);
        return db;
    }

    public Database dropDatabase(String databaseName) {
        Database db = new Database(httpClient, databaseName);
        db.dropDatabase(databaseName);
        return db;
    }

    public List<String> listDatabase(String databaseName, int timeout) {
        Database db = new Database(httpClient, databaseName);
        return db.listDatabase(databaseName);
    }

    public Database database(String databaseName) {
        return new Database(httpClient, databaseName);
    }
}
