package com.tencentcloudapi.client;

import com.tencentcloudapi.model.Database;
import com.tencentcloudapi.model.param.database.ConnectParam;
import com.tencentcloudapi.service.HttpStub;
import com.tencentcloudapi.service.Stub;

import java.util.List;

/**
 * VectorDB Client
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class VectorDBClient {

    private Stub stub;

    public VectorDBClient(ConnectParam connectParam) {
        stub = new HttpStub();
    }

    public Database createDatabase(String databaseName) {
        Database db = new Database(stub, databaseName);
        db.createDatabase(databaseName);
        return db;
    }

    public Database dropDatabase(String databaseName) {
        Database db = new Database(stub, databaseName);
        db.dropDatabase(databaseName);
        return db;
    }

    public List<String> listDatabase(String databaseName, int timeout) {
        Database db = new Database(stub, databaseName);
        return db.listDatabase(databaseName);
    }

    public Database database(String databaseName) {
        return new Database(stub, databaseName);
    }
}
