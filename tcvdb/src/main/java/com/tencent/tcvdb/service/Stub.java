package com.tencent.tcvdb.service;

import com.tencent.tcvdb.model.Collection;
import com.tencent.tcvdb.model.Document;
import com.tencent.tcvdb.model.param.entity.*;
import com.tencent.tcvdb.service.param.*;
import com.tencent.tcvdb.model.*;
import com.tencent.tcvdb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvdb.model.param.entity.*;
import com.tencent.tcvdb.service.param.*;
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
     */
    void createCollection(CreateCollectionParam params);

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
     * @return {@link AffectRes}
     */
    AffectRes truncateCollection(String databaseName, String collectionName);

    /**
     * drop collection
     *
     * @param databaseName   database name
     * @param collectionName collection name
     */
    void dropCollection(String databaseName, String collectionName);

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
     * @return {@link AffectRes}
     */
    AffectRes upsertDocument(InsertParamInner param);

    /**
     * query document
     *
     * @param param query parameter
     * @return {@link Document} {@link List}
     */
    List<Document> queryDocument(QueryParamInner param);

    /**
     * search document from collection
     *
     * @param param search parameter
     * @return {@link SearchRes}
     */
    SearchRes searchDocument(SearchParamInner param);

    /**
     * delete document
     *
     * @param param delete parameter
     */
    AffectRes deleteDocument(DeleteParamInner param);

    /**
     * update documents
     *
     * @param param update parameter
     */
    AffectRes updateDocument(UpdateParamInner param);

    /**
     * rebuild index
     *
     * @param param rebuild parameter
     */
    BaseRes rebuildIndex(RebuildIndexParamInner param);

}
