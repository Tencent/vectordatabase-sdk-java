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
import com.tencent.tcvectordb.model.Collection;
import com.tencent.tcvectordb.model.Database;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.entity.BaseRes;
import com.tencent.tcvectordb.model.param.entity.HybridSearchRes;
import com.tencent.tcvectordb.model.param.entity.SearchRes;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.service.HttpStub;
import com.tencent.tcvectordb.service.Stub;
import com.tencent.tcvectordb.service.param.*;
import org.json.JSONObject;
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

    public void close() {
        if (stub != null) {
            stub.close();
        }
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
     * create database if not existed
     * @param databaseName database's name to create. The name of the database. A database name can only include
     *         numbers, letters, and underscores, and must not begin with a letter, and length
     *         must between 1 and 128.
     * @return Database object
     * @throws VectorDBException
     */
    public Database createDatabaseIfNotExists(String databaseName) throws VectorDBException {
        Database db = database(databaseName, readConsistency);
        List<String> databaseNames = stub.listDatabases();
        if (databaseNames.contains(databaseName)){
            return new Database(stub, databaseName, this.readConsistency);
        }
        stub.createDatabase(db);
        return db;
    }

    /**
     * this method is deprecated, recommend use {@link VectorDBClient#IsExistsDatabase(String)}
     * check database exists, return true if existed else false
     * @param databaseName database's name
     * @return Boolean
     * @throws VectorDBException
     */
    @Deprecated
    public Boolean existsDatabase(String databaseName) throws VectorDBException {
        List<String> databaseNames = stub.listDatabases();
        if(databaseNames!=null && databaseNames.contains(databaseName)){
            return true;
        }
        return false;
    }

    /**
     * check database exists, return true if existed else false
     * @param databaseName database's name
     * @return Boolean
     * @throws VectorDBException
     */
    public Boolean IsExistsDatabase(String databaseName) throws VectorDBException {
        List<String> databaseNames = stub.listDatabases();
        if(databaseNames!=null && databaseNames.contains(databaseName)){
            return true;
        }
        return false;
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
     * this method is deprecated, recommend use {@link VectorDBClient#IsExistsCollection(String, String)}
     * exists collection, true if collection exists else false
     * @param databaseName
     * @param collection
     * @return boolean
     * @throws VectorDBException
     */
    @Deprecated
    public Boolean existsCollection(String databaseName, String collection) throws VectorDBException {
        Collection collectionInfo = null;
        try {
            collectionInfo = stub.describeCollection(databaseName, collection);
        }catch (Exception e){}
        return collectionInfo!=null;
    }

    /**
     * exists collection, true if collection exists else false
     * @param databaseName
     * @param collection
     * @return boolean
     * @throws VectorDBException
     */
    public Boolean IsExistsCollection(String databaseName, String collection) throws VectorDBException {
        Collection collectionInfo = null;
        try {
            collectionInfo = stub.describeCollection(databaseName, collection);
        }catch (Exception e){}
        return collectionInfo!=null;
    }

    /**
     * create collection if not existed
     * @param databaseName
     * @param param
     * @return boolean
     * @throws VectorDBException
     */
    public Collection createCollectionIfNotExists(String databaseName, CreateCollectionParam param) throws VectorDBException {
        Collection collections = null;
        try {
            collections = stub.describeCollection(databaseName, param.getCollection());
        }catch (Exception e){}
        param.setDatabase(databaseName);
        param.setReadConsistency(readConsistency);
        if (collections==null){
            stub.createCollection(param);
        }
        param.setStub(this.stub);
        return param;
    }

    /**
     * upsert document
     * @param database: database name
     * @param collection: collection name
     * @param param InsertParam: upsert data. buildIndex is whether to build index, default is true, documents
     *              is a list of JSONObject or Document.
     *              eg: Arrays.asList(
     *                new JSONObject("{\"id\":\"0013\",\"vector\":[0.2123, 0.21, 0.213],\"bookName\":\"三国演义\",\"author\":\"吴承恩\",\"page\":21,\"segment\":\"富贵功名，前缘分定，为人切莫欺心。\"}"),
     *                new JSONObject("{\"id\":\"0014\",\"vector\":[0.2123, 0.21, 0.213],\"bookName\":\"三国演义\",\"author\":\"吴承恩\",\"page\":21,\"segment\":\"富贵功名，前缘分定，为人切莫欺心。\"}")
     *              ); or
     *              Arrays.asList(Document.newBuilder()
     *                         .withId("0001")
     *                         .withVector(generateRandomVector(768))
     *                         .withSparseVector(sparseVectors.get(0))
     *                         .addDocField(new DocField("bookName", "三国演义"))
     *                         .addDocField(new DocField("author", "罗贯中"))
     *                         .addDocField(new DocField("page", 21))
     *                         .addDocField(new DocField("segment", "富贵功名，前缘分定，为人切莫欺心。"))
     *                         .addDocField(new DocField("text", "富贵功名，前缘分定，为人切莫欺心。"))
     *                         .build());
     * @return AffectRes.class
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
     * @param param QueryParam:
     *        limit(int): Limit return row's count
     *        offset(int): Skip offset rows of query result set
     *        retrieve_vector(bool): Whether to return vector values.
     *        filter(Filter): filter rows before return result
     *        document_ids(List): filter rows by id list
     *        output_fields(List): return columns by column name list
     * @return List<Document>
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
     * @param param SearchByVectorParam:
     *              vectors: List<List<Double>>, search documents by the vectors
     *              limit(int): Limit return row's count
     *              retrieve_vector(bool): Whether to return vector values.
     *              filter(Filter): filter rows before return result
     *              output_fields(List): return columns by column name list
     *              radius(Float): radius of search
     *              params(Params): params for search, eg:HNSWSearchParams, GeneralParams
     * @return List<List<Document>>: the size of the result is the same as the size of vectors
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
     * @param param SearchByVectorParam:
     *              documentIds: List<String>, search documents by the document ids
     *              limit(int): Limit return row's count
     *              retrieve_vector(bool): Whether to return vector values.
     *              filter(Filter): filter rows before return result
     *              output_fields(List): return columns by column name list
     *              radius(Float): radius of search
     *              params(Params): params for search, eg:HNSWSearchParams, GeneralParams
     * @return List<List<Document>>: the size of the result is the same as the size of documentIds
     * @throws VectorDBException
     */
    public List<List<Document>> searchById(String database, String collection, SearchByIdParam param) throws VectorDBException {
        return this.stub.searchDocument(new SearchParamInner(
                database, collection, param, this.readConsistency), DataBaseTypeEnum.BASE).getDocuments();
    }


    /**
     * search document by contents that would be embedded to vectors from the collection
     * @param database
     * @param collection
     * @param param SearchByVectorParam:
     *              embeddingItems: List<String>, search documents by the content
     *              limit(int): Limit return row's count
     *              retrieve_vector(bool): Whether to return vector values.
     *              filter(Filter): filter rows before return result
     *              output_fields(List): return columns by column name list
     *              radius(Float): radius of search
     *              params(Params): params for search, eg:HNSWSearchParams, GeneralParams
     * @return List<List<Document>>: the size of the result is the same as the size of embeddingItems
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
     * @param param DeleteParam: delete document that retrieved by filter and documentIds
     * @return AffectRes
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
     * @param param: update param used for retrieving document
     * @param document(Document.class): the document to be updated
     * @return AffectRes
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
     * @param param: update param used for retrieving document
     * @param document(JSONObject.class): the document to be updated
     * @return AffectRes
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
     * @param param HybridSearchParam:
     *      ann(List<AnnOption>): ann options, annOption used for vector search,
     *      match(List<MatchOption>): match options, matchOption used for sparse vector search
     *      retrieve_vector(bool): Whether to return vector and sparse vector values.
     *      filter(Filter): filter rows before return result
     *      document_ids(List): filter rows by id list
     *      output_fields(List): return columns by column name list
     *      Limit(int): limit the number of rows returned
     *      rerank(RerankParam): rerank param, RRFRerankParam or WeightRerankParam
     * @return HybridSearchRes: the size of the result is the same as the size of embeddingItems
     * @throws VectorDBException
     */
    public HybridSearchRes hybridSearch(String database, String collection, HybridSearchParam param) throws VectorDBException {
        boolean ai = false;
        if(param.getAnn()!=null && !param.getAnn().isEmpty() && param.getAnn().get(0).getData()!=null
                && !param.getAnn().get(0).getData().isEmpty()
                && param.getAnn().get(0).getData().get(0) instanceof String){
            ai = true;
        }
        return this.stub.hybridSearchDocument(new HybridSearchParamInner(
                database, collection, param, this.readConsistency), ai);
    }

    /**
     * Used to add a scalar field index to an existing collection
     * (the scalar field may contain historical data or a newly added empty field)
     * @param database
     * @param collection
     * @param addIndexParam:
     * @return
     * @throws VectorDBException
     */
    public BaseRes AddIndex(String database, String collection, AddIndexParam addIndexParam) throws VectorDBException {
        return this.stub.addIndex(
                new AddIndexParamInner(database, collection, addIndexParam));
    }

}
