/*
 * Copyright (C) 2023 Tencent Cloud.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the vectordb-sdk-java), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
import com.tencent.tcvectordb.service.HttpStub;
import com.tencent.tcvectordb.service.Stub;
import com.tencent.tcvectordb.service.param.*;

import java.util.List;

/**
 * VectorDB Client
 */
public class VectorDBClient {

    protected  Stub stub;
    protected  ReadConsistencyEnum readConsistency;

    public VectorDBClient(ConnectParam connectParam, ReadConsistencyEnum readConsistency) {
        this.stub = new HttpStub(connectParam);
        this.readConsistency = readConsistency;
    }

    protected VectorDBClient() {
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
        InsertParamInner insertParam = new InsertParamInner(
                database, collection, param);
        return this.stub.upsertDocument(insertParam);
    }

    public List<Document> query(String database, String collection, QueryParam param) throws VectorDBException {
        return this.stub.queryDocument(
                new QueryParamInner(database, collection, param, this.readConsistency));
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
        return this.stub.updateDocument(
                new UpdateParamInner(database, collection, param, document));
    }
}
