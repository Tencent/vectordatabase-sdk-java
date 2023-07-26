package com.tencentcloudapi.client;

import com.tencentcloudapi.exception.VectorDBException;
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

    public Database createDatabase(String databaseName) throws VectorDBException {
        Database db = database(databaseName);
        stub.createDatabase(db);
        return db;
    }

    public Database dropDatabase(String databaseName) throws VectorDBException {
        Database db = database(databaseName);
        stub.dropDatabase(db);
        return db;
    }

    public List<String> listDatabase() throws VectorDBException {
        return stub.listDatabases();
    }

    public Database database(String databaseName) {
        return new Database(this.stub, databaseName);
    }
}
