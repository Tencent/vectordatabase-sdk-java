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
import com.tencent.tcvectordb.model.param.collection.UploadFileParam;
import com.tencent.tcvectordb.model.param.collectionView.CreateCollectionViewParam;
import com.tencent.tcvectordb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvectordb.model.param.collectionView.LoadAndSplitTextParam;
import com.tencent.tcvectordb.model.param.dml.AtomicEmbeddingParam;
import com.tencent.tcvectordb.model.param.entity.*;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.model.param.user.*;
import com.tencent.tcvectordb.service.param.*;

import java.util.List;
import java.util.Map;

/**
 * Stub for DB service API
 */
public interface Stub {

    /*****************Database***********************/

    /**
     * Create a new database instance
     */
    void createDatabase(Database database);

    /**
     * Drop an existing database
     */
    void dropDatabase(Database database);

    /**
     * Create a new AI database instance
     */
    AffectRes createAIDatabase(AIDatabase aiDatabase);

    /**
     * Describe database information and type
     */
    DataBaseTypeRes describeDatabase(Database database);

    /**
     * Drop an existing AI database
     */
    AffectRes dropAIDatabase(AIDatabase aiDatabase);

    /**
     * List all available database names
     */
    List<String> listDatabases();

    /**
     * List all databases with their type information
     */
    Map<String, DataBaseType> listDatabaseInfos();

    /*****************Collection*********************/

    /**
     * Create a new collection with specified parameters
     */
    void createCollection(CreateCollectionParam params);

    /**
     * Create a new AI collection view
     */
    void createCollectionView(CreateCollectionViewParam params);

    /**
     * List all collections in the specified database
     */
    List<Collection> listCollections(String databaseName);

    /**
     * Get detailed information about a specific collection
     */
    Collection describeCollection(String databaseName, String collectionName);

    /**
     * Remove all documents from a collection without dropping the collection
     */
    AffectRes truncateCollection(String databaseName, String collectionName, DataBaseTypeEnum dbType);

    /**
     * Remove all documents from a collection view without dropping it
     */
    AffectRes truncateCollectionView(String databaseName, String collectionName, DataBaseTypeEnum dbType);

    /**
     * Drop an existing collection permanently
     */
    void dropCollection(String databaseName, String collectionName);

    /**
     * Set an alias name for a collection
     */
    AffectRes setAlias(String databaseName, String collectionName, String aliasName);

    /**
     * Delete an existing collection alias
     */
    AffectRes deleteAlias(String databaseName, String aliasName);

    /**
     * Set an alias name for an AI collection
     */
    AffectRes setAIAlias(String databaseName, String collectionName, String aliasName);

    /**
     * Delete an existing AI collection alias
     */
    AffectRes deleteAIAlias(String databaseName, String aliasName);

    /**
     * List all collection views in the specified database
     */
    List<CollectionView> listCollectionView(String databaseName);

    /**
     * Get detailed information about a specific collection view
     */
    CollectionView describeCollectionView(String databaseName, String collectionName);

    /**
     * Drop an existing collection view permanently
     */
    AffectRes dropCollectionView(String databaseName, String collectionName);

    /*****************Document***********************/

    /**
     * Insert or update documents in a collection
     */
    AffectRes upsertDocument(InsertParamInner param, boolean ai);

    /**
     * Query documents from a collection based on specified criteria
     */
    List<Document> queryDocument(QueryParamInner param, boolean ai);

    /**
     * Search documents using vector similarity
     */
    SearchRes searchDocument(SearchParamInner param, DataBaseTypeEnum dbType);

    /**
     * Perform hybrid search combining vector and text search
     */
    HybridSearchRes hybridSearchDocument(HybridSearchParamInner param, boolean ai);

    /**
     * Delete documents from a collection based on specified criteria
     */
    AffectRes deleteDocument(DeleteParamInner param);

    /**
     * Update existing documents in a collection
     */
    AffectRes updateDocument(UpdateParamInner param, boolean ai);

    /**
     * Count the number of documents matching the query criteria
     */
    BaseRes countDocument(QueryCountParamInner param, boolean ai);

    /**
     * Query AI documents from collection views
     */
    List<DocumentSet> queryAIDocument(CollectionViewQueryParamInner queryParamInner);

    /**
     * Delete AI documents from collection views
     */
    AffectRes deleteAIDocument(CollectionViewDeleteParamInner deleteParamInner);

    /**
     * Search AI documents with content-based queries
     */
    SearchContentRes searchAIDocument(SearchDocParamInner searchDocParamInner);

    /**
     * Update AI documents in collection views
     */
    AffectRes updateAIDocument(CollectionViewUpdateParamInner updateParamInner);

    /**
     * Perform full-text search on documents
     */
    FullTextSearchRes fullTextSearch(FullTextSearchParamInner param, boolean ai);

    /*****************Index**************************/

    /**
     * Rebuild the index for a collection to optimize search performance
     */
    BaseRes rebuildIndex(RebuildIndexParamInner param);

    /**
     * Rebuild the AI index for collection views
     */
    BaseRes rebuildAIIndex(RebuildIndexParamInner param);

    /**
     * Add a new index to improve query performance
     */
    BaseRes addIndex(AddIndexParamInner addIndexParamInner);

    /**
     * Modify existing vector index configuration
     */
    BaseRes modifyVectorIndex(ModifyIndexParamInner param, boolean ai);

    /**
     * Drop an existing index from a collection
     */
    BaseRes dropIndex(DropIndexParamInner dropIndexParamInner);

    /*****************User***************************/

    /**
     * Create a new user account with specified permissions
     */
    BaseRes createUser(UserCreateParam userCreateParam);

    /**
     * Grant permissions to an existing user
     */
    BaseRes grantToUser(UserGrantParam param);

    /**
     * Revoke permissions from an existing user
     */
    BaseRes revokeFromUser(UserRevokeParam param);

    /**
     * Get detailed information about a specific user
     */
    UserDescribeRes describeUser(UserDescribeParam userDescribeParam);

    /**
     * List all users in the system
     */
    UserListRes listUser();

    /**
     * Drop an existing user account
     */
    BaseRes dropUser(UserDropParam userDropParam);

    /**
     * Change password for an existing user
     */
    BaseRes changeUserPassword(UserChangePasswordParam build);


    /*****************Embedding************************/

    /**
     * Generate atomic embeddings for text content
     */
    AtomicEmbeddingRes atomicEmbedding(AtomicEmbeddingParam param);

    /*****************Service************************/

    /**
     * Upload and process text files for AI document processing
     */
    void upload(String databaseName, String collectionName, LoadAndSplitTextParam loadAndSplitTextParam, Map<String, Object> metaDataMap) throws Exception;

    /**
     * Upload files to a collection for processing
     */
    void collectionUpload(String databaseName, String collection, UploadFileParam loadAndSplitTextParam, Map<String, Object> metaDataMap) throws Exception;

    /**
     * Retrieve file information from a collection
     */
    GetDocumentSetRes getFile(String databaseName, String collectionName, String fileName, String fileId);

    /**
     * Get document chunks with pagination support
     */
    GetChunksRes getChunks(String databaseName, String collectionName, String documentSetName, String documentSetId,
                           Integer limit, Integer offset);

    /**
     * Get image URL for document processing
     */
    GetImageUrlRes GetImageUrl(GetImageUrlParamInner param);

    /**
     * Query detailed file information
     */
    QueryFileDetailRes queryFileDetails(QueryFileDetailsParamInner param);

    /**
     * Close the service connection and release resources
     */
    void close();
}
