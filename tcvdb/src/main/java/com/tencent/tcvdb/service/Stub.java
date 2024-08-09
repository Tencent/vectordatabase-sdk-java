package com.tencent.tcvdb.service;

import com.tencent.tcvdb.model.Collection;
import com.tencent.tcvdb.model.Document;
import com.tencent.tcvdb.model.param.entity.*;
import com.tencent.tcvdb.service.param.*;
import com.tencent.tcvdb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvdb.model.Database;

import java.util.List;

/**
 * Stub for DB service API
 */
public interface Stub {
    /**
     * create database
     *
     * @param database database name
     */
    void createDatabase(Database database);

    /**
     * drop database
     *
     * @param database database name
     */
    void dropDatabase(Database database);


    /**
     * list databases
     *
     * @return database name {@link List}
     */
    List<String> listDatabases();

    /**
     * list all database, mapping database name to {@link DataBaseInfo}
     *
     * @return return map, key is database name, value is  {@link DataBaseInfo}
     */
    DataBaseInfoRes listDatabaseInfos();

    /**
     * create collection in database
     *
     * @param params create collection parameter
     * @param ai
     */
    void createCollection(CreateCollectionParam params, boolean ai);

    /**
     * list collections from database
     *
     * @param databaseName database name
     * @return {@link Collection} {@link List}
     */
    List<Collection> listCollections(String databaseName);

    /**
     * describe collection
     *
     * @param databaseName   database name
     * @param collectionName collectionName
     * @return {@link Collection}
     */
    Collection describeCollection(String databaseName, String collectionName);

    /**
     * truncate collection
     *
     * @param databaseName   database name
     * @param collectionName collection name
     * @param ai
     * @return {@link AffectRes}
     */
    AffectRes truncateCollection(String databaseName, String collectionName, boolean ai);

    /**
     * drop collection
     *
     * @param databaseName   database name
     * @param collectionName collection name
     */
    void dropCollection(String databaseName, String collectionName, boolean ai);

    /**
     * set collection alias
     *
     * @param databaseName   database name
     * @param collectionName collection name
     * @param aliasName      collection alias
     */
    AffectRes setAlias(String databaseName, String collectionName, String aliasName);

    /**
     * delete alias
     *
     * @param databaseName database name
     * @param aliasName    collection alias
     * @return {@link AffectRes}
     */
    AffectRes deleteAlias(String databaseName, String aliasName);

    /**
     * upsert document
     *
     * @param param upsert parameter
     * @param ai
     * @return {@link AffectRes}
     */
    AffectRes upsertDocument(InsertParamInner param, boolean ai);

    /**
     * query document
     *
     * @param param query parameter
     * @param b
     * @return {@link Document} {@link List}
     */
    List<Document> queryDocument(QueryParamInner param, boolean b);

    /**
     * search document from collection
     *
     * @param param search parameter
     * @param ai
     * @return {@link SearchRes}
     */
    SearchRes searchDocument(SearchParamInner param, boolean ai);

    /**
     * delete document
     *
     * @param param delete parameter
     * @param ai
     */
    AffectRes deleteDocument(DeleteParamInner param, boolean ai);

    /**
     * update documents
     *
     * @param param update parameter
     * @param ai
     */
    AffectRes updateDocument(UpdateParamInner param, boolean ai);

    /**
     * rebuild index
     *
     * @param param rebuild parameter
     */
    BaseRes rebuildIndex(RebuildIndexParamInner param);

}
