package com.tencent.tcvectordb.service;

import com.tencent.tcvectordb.model.*;
import com.tencent.tcvectordb.model.param.collectionView.CreateCollectionViewParam;
import com.tencent.tcvectordb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvectordb.model.param.entity.*;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.service.param.*;

import java.util.List;
import java.util.Map;

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
    AffectRes createAIDatabase(AIDatabase aiDatabase);

    /**
     * decribe database
     * @param database
     */
    DataBaseTypeRes describeDatabase(Database database);

    /**
     * drop ai_database
     */
    AffectRes dropAIDatabase(AIDatabase aiDatabase);

    /**
     * list databases
     */
    List<String> listDatabases();

    /**
     *
     * @return
     */
    Map<String, DataBaseType> listDatabaseInfos();

    /**
     * create collection
     */
    void createCollection(CreateCollectionParam params);

    /**
     * create AI collection
     */
    void createCollectionView(CreateCollectionViewParam params);

    /**
     * list collections
     */
    List<Collection> listCollections(String databaseName);

    /**
     * describe collection
     */
    Collection describeCollection(String databaseName, String collectionName);

    /**
     * truncate collection
     */
    AffectRes truncateCollection(String databaseName, String collectionName, DataBaseTypeEnum dbType);

    /**
     * truncate collection
     */
    AffectRes truncateCollectionView(String databaseName, String collectionName, DataBaseTypeEnum dbType);

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
    SearchRes searchDocument(SearchParamInner param, DataBaseTypeEnum dbType);

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

    List<CollectionView> listCollectionView(String databaseName);

    CollectionView describeCollectionView(String databaseName, String collectionName);

    AffectRes dropCollectionView(String databaseName, String collectionName);

    List<DocumentSet> queryAIDocument(CollectionViewQueryParamInner queryParamInner);

    AffectRes deleteAIDocument(CollectionViewDeleteParamInner deleteParamInner);

    SearchContentRes searchAIDocument(SearchDocParamInner searchDocParamInner);

    AffectRes updateAIDocument(CollectionViewUpdateParamInner updateParamInner);

    void upload(String databaseName, String collectionName, String documentSetName, String filePath, Map<String, Object> metaDataMap) throws Exception;

    GetDocumentSetRes getFile(String databaseName, String collectionName, String fileName, String fileId);

    BaseRes rebuildAIIndex(RebuildIndexParamInner param);
}
