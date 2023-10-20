package com.tencent.tcvectordb.service;

import com.tencent.tcvectordb.model.collection.AICollection;
import com.tencent.tcvectordb.model.collection.Collection;
import com.tencent.tcvectordb.model.Database;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.collection.CreateAICollectionParam;
import com.tencent.tcvectordb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvectordb.model.param.entity.*;
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
     * create ai_database
     */
    void createAIDatabase(Database database);

    /**
     * decribe database
     * @param database
     */
    DataBaseTypeRes describeDatabase(Database database);

    /**
     * drop ai_database
     */
    void dropAIDatabase(Database database);

    /**
     * list databases
     */
    List<String> listDatabases();

    /**
     * create collection
     */
    void createCollection(CreateCollectionParam params);

    /**
     * create AI collection
     */
    void createAICollection(CreateAICollectionParam params);

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
    AffectRes truncateCollection(String databaseName, String collectionName);

    /**
     * drop collection
     */
    void dropCollection(String databaseName, String collectionName);

    /**
     * set collection alias
     */
    AffectRes setAlias(String databaseName, String collectionName, String aliasName);

    /**
     * delete alias
     */
    AffectRes deleteAlias(String databaseName, String aliasName);

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
    SearchRes searchDocument(SearchParamInner param);

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

    AffectRes setAIAlias(String databaseName, String collectionName, String aliasName);

    AffectRes deleteAIAlias(String databaseName, String aliasName);

    List<Collection> listAICollections(String databaseName);

    AICollection describeAICollection(String databaseName, String collectionName);

    void dropAICollection(String databaseName, String collectionName);

    List<Document> queryAIDocument(QueryParamInner queryParamInner);

    AffectRes deleteAIDocument(DeleteParamInner deleteParamInner);

    SearchRes searchAIDocument(SearchParamInner searchParamInner);

    AffectRes updateAIDocument(UpdateParamInner updateParamInner);

    BaseRes Upload(String databaseName, String collectionName, String filePath);
}
