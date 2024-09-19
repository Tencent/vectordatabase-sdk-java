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

    /**
     * create database
     * @param databaseName database's name to create. The name of the database. A database name can only include
     *         numbers, letters, and underscores, and must not begin with a letter, and length
     *         must between 1 and 128.
     * @return Database object
     * @throws VectorDBException
     */
    public Database createDatabase(String databaseName) throws VectorDBException {
        Database db = database(databaseName, readConsistency);
        stub.createDatabase(db);
        return db;
    }

    /**
     * drop database
     * @param databaseName: database's name to drop
     * @return
     * @throws VectorDBException
     */
    public Database dropDatabase(String databaseName) throws VectorDBException {
        Database db = database(databaseName, readConsistency);
        stub.dropDatabase(db);
        return db;
    }

    /**
     * create ai database
     * @param databaseName ai database's name to create, The name of the database. A database name can only include
     *         numbers, letters, and underscores, and must not begin with a letter, and length
     *         must between 1 and 128
     * @return
     * @throws VectorDBException
     */
    public AIDatabase createAIDatabase(String databaseName) throws VectorDBException {
        AIDatabase db = aiDatabase(databaseName);
        stub.createAIDatabase(db);
        return db;
    }

    /**
     * drop ai database
     * @param databaseName: ai database's name to drop
     * @return
     * @throws VectorDBException
     */
    public AffectRes dropAIDatabase(String databaseName) throws VectorDBException {
        AIDatabase db = aiDatabase(databaseName);
        return stub.dropAIDatabase(db);
    }

    /**
     * get database list
     * @return the list of database name
     * @throws VectorDBException
     */
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

    /**
     * upsert document
     * @param database: database name
     * @param collection: collection name
     * @param param: insert param
     *
     * @return
     * @throws VectorDBException
     */
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

    /**
     * query document
     * @param database
     * @param collection
     * @param param
     * @return
     * @throws VectorDBException
     */
    public List<Document> query(String database, String collection, QueryParam param) throws VectorDBException {
        return this.stub.queryDocument(
                new QueryParamInner(database, collection, param, this.readConsistency), false);
    }

    /**
     * search document
     * @param database
     * @param collection
     * @param param
     * @return
     * @throws VectorDBException
     */
    public List<List<Document>> search(String database, String collection, SearchByVectorParam param) throws VectorDBException {
        return this.stub.searchDocument(new SearchParamInner(
                database, collection, param, this.readConsistency), DataBaseTypeEnum.BASE).getDocuments();
    }

    /**
     * search document by ID
     * @param database
     * @param collection
     * @param param
     * @return
     * @throws VectorDBException
     */
    public List<List<Document>> searchById(String database, String collection, SearchByIdParam param) throws VectorDBException {
        return this.stub.searchDocument(new SearchParamInner(
                database, collection, param, this.readConsistency), DataBaseTypeEnum.BASE).getDocuments();
    }


    /**
     * search document by embedding items
     * @param database
     * @param collection
     * @param param
     * @return
     * @throws VectorDBException
     */
    public SearchRes searchByEmbeddingItems(String database, String collection, SearchByEmbeddingItemsParam param) throws VectorDBException {
        return this.stub.searchDocument(new SearchParamInner(
                database, collection, param, this.readConsistency), DataBaseTypeEnum.BASE);
    }

    /**
     * delete document
     * @param database
     * @param collection
     * @param param
     * @return
     * @throws VectorDBException
     */
    public AffectRes delete(String database, String collection, DeleteParam param) throws VectorDBException {
        return this.stub.deleteDocument(
                new DeleteParamInner(database, collection, param));
    }

    /**
     * update document use document object
     * @param database
     * @param collection
     * @param param
     * @param document
     * @return
     * @throws VectorDBException
     */
    public AffectRes update(String database, String collection, UpdateParam param, Document document) throws VectorDBException {
        boolean ai = false;
        if (document.getVector() instanceof String){
            ai = true;
        }
        return this.stub.updateDocument(
                new UpdateParamInner(database, collection, param, document), ai);
    }

    /**
     * update document use json object
     * @param database
     * @param collection
     * @param param
     * @param document
     * @return
     * @throws VectorDBException
     */
    public AffectRes update(String database, String collection, UpdateParam param, JSONObject document) throws VectorDBException {
        boolean ai = false;
        if (document.get("vector") instanceof String){
            ai = true;
        }
        return this.stub.updateDocument(
                new UpdateParamInner(database, collection, param, document), ai);
    }

    /**
     * sparse vector and vector hybrid search
     * @param database
     * @param collection
     * @param param: HybridSearchParam
     * @return
     * @throws VectorDBException
     */
    public SearchRes hybridSearch(String database, String collection, HybridSearchParam param) throws VectorDBException {
        boolean ai = false;
        if(param.getAnn()!=null && !param.getAnn().isEmpty() && param.getAnn().get(0).getData()!=null
                && !param.getAnn().get(0).getData().isEmpty()
                && param.getAnn().get(0).getData().get(0) instanceof String){
            ai = true;
        }
        return this.stub.hybridSearchDocument(new HybridSearchParamInner(
                database, collection, param, this.readConsistency), ai);
    }
}
