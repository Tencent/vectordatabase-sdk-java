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

    private final Stub stub;

    public VectorDBClient(ConnectParam connectParam) {
        this.stub = new HttpStub(connectParam);
    }

    public Database createDatabase(String databaseName) {
        Database db = database(databaseName);
        db.createDatabase();
        return db;
    }

    public Database dropDatabase(String databaseName) {
        Database db = database(databaseName);
        db.dropDatabase();
        return db;
    }

    public List<String> listDatabase(String databaseName) {
        Database db = database(databaseName);
        return db.listDatabase();
    }

    public Database database(String databaseName) {
        return new Database(this.stub, databaseName);
    }
}
