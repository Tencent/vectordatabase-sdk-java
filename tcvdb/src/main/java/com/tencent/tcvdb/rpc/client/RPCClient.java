package com.tencent.tcvdb.rpc.client;

import com.tencent.tcvdb.client.VectorDBClient;
import com.tencent.tcvdb.exception.VectorDBException;
import com.tencent.tcvdb.model.Database;
import com.tencent.tcvdb.model.param.database.ConnectParam;
import com.tencent.tcvdb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvdb.service.GrpcStub;

import java.util.List;

public class RPCClient extends VectorDBClient {
    public RPCClient(ConnectParam connectParam, ReadConsistencyEnum readConsistency) {
        super();
        this.stub = new GrpcStub(connectParam);
        this.readConsistency = readConsistency;
    }
//    public void close() {
//        channel.shutdown();
//    }


    public Database createDatabase(String databaseName) throws VectorDBException {
        Database db = database(databaseName, readConsistency);
        stub.createDatabase(db);
        return db;
    }

    public Database dropDatabase(String databaseName) throws VectorDBException {
        Database db = database(databaseName, readConsistency);
        stub.dropDatabase(db);
        return db;
    }

    public List<String> listDatabase() throws VectorDBException {
        return stub.listDatabases();
    }

    /**
     * this method is deprecated, recommend use {@link VectorDBClient#database(String)}
     *
     * @param databaseName
     * @param readConsistency
     * @return
     */
    @Deprecated
    public Database database(String databaseName, ReadConsistencyEnum readConsistency) {
        return new Database(this.stub, databaseName, readConsistency);
    }

    public Database database(String databaseName) {
        return new Database(this.stub, databaseName, this.readConsistency);
    }




























}
