package com.tencent.tcvectordb.service;

import com.tencent.tcvectordb.model.Collection;
import com.tencent.tcvectordb.model.Database;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.entity.BaseRes;
import com.tencent.tcvectordb.service.param.*;

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
     * describe collection
     */
    AffectRes flushCollection(String databaseName, String collectionName);

    /**
     * drop collection
     */
    void dropCollection(String databaseName, String collectionName);

    /**
     * set collection alias
     */
    AffectRes setCollectionAlias(String databaseName, String collectionName, String aliasName);

    /**
     * delete alias
     */
    AffectRes deleteCollectionAlias(String databaseName, String aliasName);

    /**
     * upsert document
     */
    AffectRes upsertDocument(InsertParamInner param);

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
    AffectRes deleteDocument(DeleteParamInner param);

    /**
     * delete document
     */
    AffectRes updateDocument(UpdateParamInner param);

    /**
     * rebuild index
     */
    BaseRes rebuildIndex(RebuildIndexParamInner param);
}
