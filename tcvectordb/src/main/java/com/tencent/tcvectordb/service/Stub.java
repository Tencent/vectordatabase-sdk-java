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
package com.tencent.tcvectordb.service;

import com.tencent.tcvectordb.model.*;
import com.tencent.tcvectordb.model.param.collectionView.CreateCollectionViewParam;
import com.tencent.tcvectordb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvectordb.model.param.collectionView.LoadAndSplitTextParam;
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
    AffectRes upsertDocument(InsertParamInner param, boolean ai);

    /**
     * query document
     */
    List<Document> queryDocument(QueryParamInner param, boolean ai);

    /**
     * search document
     */
    SearchRes searchDocument(SearchParamInner param, DataBaseTypeEnum dbType);


    /**
     * hybrid search document
     */
    HybridSearchRes hybridSearchDocument(HybridSearchParamInner param, boolean ai);

    /**
     * delete document
     */
    AffectRes deleteDocument(DeleteParamInner param);

    /**
     * delete document
     */
    AffectRes updateDocument(UpdateParamInner param, boolean ai);

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

    void upload(String databaseName, String collectionName, LoadAndSplitTextParam loadAndSplitTextParam, Map<String, Object> metaDataMap) throws Exception;

    GetDocumentSetRes getFile(String databaseName, String collectionName, String fileName, String fileId);

    GetChunksRes getChunks(String databaseName, String collectionName, String documentSetName, String documentSetId,
                           Integer limit, Integer offset);

    BaseRes rebuildAIIndex(RebuildIndexParamInner param);

    BaseRes addIndex(AddIndexParamInner addIndexParamInner);

    void close();

    BaseRes countDocument(QueryCountParamInner param, boolean ai);
}
