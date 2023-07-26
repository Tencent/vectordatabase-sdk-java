package com.tencentcloudapi.service;

import com.tencentcloudapi.model.Collection;
import com.tencentcloudapi.model.Database;
import com.tencentcloudapi.model.Document;
import com.tencentcloudapi.model.param.dml.*;

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
    void upsertDocument(InsertParam.InsertParamInner param);

    /**
     * query document
     */
    List<Document> queryDocument(QueryParam.QueryParamInner param);

    /**
     * search document
     */
    List<List<Document>> searchDocument(SearchParam.SearchParamInner param);

    /**
     * delete document
     */
    void deleteDocument(DeleteParam.DeleteParamInner param);
}
