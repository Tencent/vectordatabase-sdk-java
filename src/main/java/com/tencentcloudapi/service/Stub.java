package com.tencentcloudapi.service;

import com.tencentcloudapi.model.Collection;
import com.tencentcloudapi.model.Database;
import com.tencentcloudapi.model.Document;
import com.tencentcloudapi.model.param.dml.QueryParam;
import com.tencentcloudapi.model.param.dml.SearchByIdParam;
import com.tencentcloudapi.model.param.dml.SearchParam;

import java.util.List;

/**
 * Stub for DB service API
 * User: wlleiiwang
 * Date: 2023/7/25
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
    void createCollection(Collection params);

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
    void upsertDocument(List<Document> documents);

    /**
     * query document
     */
    List<Document> queryDocument(QueryParam param);

    /**
     * search document
     */
    List<List<Document>> searchDocument(SearchParam param);

    /**
     * search document by id
     */
    List<Document> searchDocumentById(SearchByIdParam param);

    /**
     * delete document
     */
    void deleteDocument(List<String> documentIds);
}
