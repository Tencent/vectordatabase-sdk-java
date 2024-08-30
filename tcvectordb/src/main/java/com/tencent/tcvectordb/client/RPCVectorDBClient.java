package com.tencent.tcvectordb.client;

import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.AIDatabase;
import com.tencent.tcvectordb.model.Database;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.entity.SearchRes;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.service.GrpcStub;
import com.tencent.tcvectordb.service.param.*;
import org.json.JSONObject;

import java.util.List;

public class RPCVectorDBClient extends VectorDBClient {

    public RPCVectorDBClient(ConnectParam connectParam, ReadConsistencyEnum readConsistency) {
        super();
        this.stub = new GrpcStub(connectParam);
        this.readConsistency = readConsistency;
    }

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

    public AIDatabase createAIDatabase(String databaseName) throws VectorDBException {
        AIDatabase db = aiDatabase(databaseName);
        stub.createAIDatabase(db);
        return db;
    }

    public AffectRes dropAIDatabase(String databaseName) throws VectorDBException {
        AIDatabase db = aiDatabase(databaseName);
        return stub.dropAIDatabase(db);
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

    public AIDatabase aiDatabase(String databaseName){
        return new AIDatabase(this.stub, databaseName, this.readConsistency);
    }

    public AffectRes upsert(String database, String collection, InsertParam param) throws VectorDBException {
        boolean ai = false;
        if((param.getDocuments().get(0)!=null)){
            if (param.getDocuments().get(0) instanceof Document){
                if (((Document)param.getDocuments().get(0)).getVector() instanceof String){
                    ai = true;
                }
            }
            if (param.getDocuments().get(0) instanceof JSONObject){
                if (((JSONObject)param.getDocuments().get(0)).get("vector") instanceof String){
                    ai = true;
                }
            }
        }
        InsertParamInner insertParam = new InsertParamInner(
                database, collection, param);
        return this.stub.upsertDocument(insertParam, ai);
    }

    public List<Document> query(String database, String collection, QueryParam param) throws VectorDBException {
        return this.stub.queryDocument(
                new QueryParamInner(database, collection, param, this.readConsistency), false);
    }

    public List<List<Document>> search(String database, String collection, SearchByVectorParam param) throws VectorDBException {
        return this.stub.searchDocument(new SearchParamInner(
                database, collection, param, this.readConsistency), DataBaseTypeEnum.BASE).getDocuments();
    }

    public List<List<Document>> searchById(String database, String collection, SearchByIdParam param) throws VectorDBException {
        return this.stub.searchDocument(new SearchParamInner(
                database, collection, param, this.readConsistency), DataBaseTypeEnum.BASE).getDocuments();
    }

    public SearchRes searchByEmbeddingItems(String database, String collection, SearchByEmbeddingItemsParam param) throws VectorDBException {
        return this.stub.searchDocument(new SearchParamInner(
                database, collection, param, this.readConsistency), DataBaseTypeEnum.BASE);
    }

    public AffectRes delete(String database, String collection, DeleteParam param) throws VectorDBException {
        return this.stub.deleteDocument(
                new DeleteParamInner(database, collection, param));
    }

    public AffectRes update(String database, String collection, UpdateParam param, Document document) throws VectorDBException {
        boolean ai = false;
        if (document.getVector() instanceof String){
            ai = true;
        }
        return this.stub.updateDocument(
                new UpdateParamInner(database, collection, param, document), ai);
    }

    public AffectRes update(String database, String collection, UpdateParam param, JSONObject document) throws VectorDBException {
        boolean ai = false;
        if (document.get("vector") instanceof String){
            ai = true;
        }
        return this.stub.updateDocument(
                new UpdateParamInner(database, collection, param, document), ai);
    }
}
