package com.tencent.tcvectordb.service;

import com.tencent.tcvectordb.model.Collection;
import com.tencent.tcvectordb.model.Database;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvectordb.service.param.DeleteParamInner;
import com.tencent.tcvectordb.service.param.InsertParamInner;
import com.tencent.tcvectordb.service.param.QueryParamInner;
import com.tencent.tcvectordb.service.param.SearchParamInner;

import java.util.List;

/**
 * Stub for DB service API
 */
public interface Stub {

    /**
     * create database
     */
    void createDatabase(Database database);

    /**
     * drop database
     */
    void dropDatabase(Database database);

    /**
     * list databases
     */
    List<String> listDatabases();

    /**
     * create collection
     */
    void createCollection(CreateCollectionParam params);

    /**
     * list collections
     */
    List<Collection> listCollections(String databaseName);

    /**
     * describe collection
     */
    Collection describeCollection(String databaseName, String collectionName);

    /**
     * drop collection
     */
    void dropCollection(String databaseName, String collectionName);

    /**
     * upsert document
     */
    void upsertDocument(InsertParamInner param);

    /**
     * query document
     */
    List<Document> queryDocument(QueryParamInner param);

    /**
     * search document
     */
    List<List<Document>> searchDocument(SearchParamInner param);

    /**
     * delete document
     */
    void deleteDocument(DeleteParamInner param);
}
