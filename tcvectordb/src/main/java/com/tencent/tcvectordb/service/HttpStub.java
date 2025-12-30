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

import com.tencent.tcvectordb.model.Collection;
import com.tencent.tcvectordb.model.*;
import com.tencent.tcvectordb.model.param.collection.UploadFileParam;
import com.tencent.tcvectordb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvectordb.model.param.collectionView.*;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.model.param.dml.AtomicEmbeddingParam;
import com.tencent.tcvectordb.model.param.entity.*;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.model.param.user.*;
import com.tencent.tcvectordb.service.param.*;
import okhttp3.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import com.tencent.tcvectordb.service.impl.http.DatabaseHttpService;
import com.tencent.tcvectordb.service.impl.http.CollectionHttpService;
import com.tencent.tcvectordb.service.impl.http.DocumentHttpService;
import com.tencent.tcvectordb.service.impl.http.IndexHttpService;
import com.tencent.tcvectordb.service.impl.http.UserHttpService;
import com.tencent.tcvectordb.service.impl.http.DocumentAIHttpService;
import com.tencent.tcvectordb.service.impl.http.EmbeddingHttpService;

/**
 * HTTP Stub for DB service API
 */
public class HttpStub implements Stub {
    private DatabaseHttpService databaseService;
    private CollectionHttpService collectionService;
    private DocumentHttpService documentService;
    private IndexHttpService indexService;
    private UserHttpService userService;
    private DocumentAIHttpService documentAIService;
    private EmbeddingHttpService embeddingService;
    private ConnectParam connectParam;
    private OkHttpClient client;
    private Headers.Builder headersBuilder;
    
    public HttpStub(){}

    public HttpStub(ConnectParam connectParam) {
        initHttpStub(connectParam);
    }

    protected void initHttpStub(ConnectParam connectParam) {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    this.connectParam = connectParam;

                    // Create authorization header
                    String authorization = String.format("Bearer account=%s&api_key=%s",
                            connectParam.getUsername(), connectParam.getKey());
                    this.headersBuilder = new Headers.Builder()
                            .add("Authorization", authorization);

                    // Create a single shared OkHttpClient for all services
                    this.client = new OkHttpClient.Builder()
                            .connectTimeout(this.connectParam.getConnectTimeout(), TimeUnit.SECONDS)
                            .readTimeout(connectParam.getTimeout(), TimeUnit.SECONDS)
                            .writeTimeout(connectParam.getTimeout(), TimeUnit.SECONDS)
                            .connectionPool(new ConnectionPool(
                                    this.connectParam.getMaxIdleConnections(), this.connectParam.getKeepAliveDuration(), TimeUnit.SECONDS))
                            .build();
                    
                    // Initialize module services with shared client and headers
                    this.databaseService = new DatabaseHttpService();
                    this.databaseService.init(this.connectParam, this.client, this.headersBuilder);
                    this.collectionService = new CollectionHttpService();
                    this.collectionService.init(this.connectParam, this.client, this.headersBuilder);
                    this.documentService = new DocumentHttpService();
                    this.documentService.init(this.connectParam, this.client, this.headersBuilder);
                    this.indexService = new IndexHttpService();
                    this.indexService.init(this.connectParam, this.client, this.headersBuilder);
                    this.userService = new UserHttpService();
                    this.userService.init(this.connectParam, this.client, this.headersBuilder);
                    this.embeddingService = new EmbeddingHttpService();
                    this.embeddingService.init(this.connectParam, this.client, this.headersBuilder);
                    this.documentAIService = new DocumentAIHttpService();
                    this.documentAIService.init(this.connectParam, this.client, this.headersBuilder);
                }
            }
        }
    }

    @Override
    public void createDatabase(Database database) {
        this.databaseService.createDatabase(database);
    }

    @Override
    public void dropDatabase(Database database) {
        this.databaseService.dropDatabase(database);
    }

    @Override
    public AffectRes createAIDatabase(AIDatabase aiDatabase) {
        return this.databaseService.createAIDatabase(aiDatabase);
    }

    @Override
    public DataBaseTypeRes describeDatabase(Database database) {
        return this.databaseService.describeDatabase(database);
    }

    @Override
    public AffectRes dropAIDatabase(AIDatabase aiDatabase) {
        return this.databaseService.dropAIDatabase(aiDatabase);
    }

    @Override
    public List<String> listDatabases() {
        return this.databaseService.listDatabases();
    }

    @Override
    public Map<String, DataBaseType> listDatabaseInfos() {
        return this.databaseService.listDatabaseInfos();
    }

    @Override
    public void createCollection(CreateCollectionParam param) {
        this.collectionService.createCollection(param);
    }

    @Override
    public void createCollectionView(CreateCollectionViewParam params) {
        this.collectionService.createCollectionView(params);
    }

    @Override
    public List<Collection> listCollections(String databaseName) {
        return this.collectionService.listCollections(databaseName);
    }


    @Override
    public Collection describeCollection(String databaseName, String collectionName) {
        return this.collectionService.describeCollection(databaseName, collectionName);
    }

    @Override
    public AffectRes truncateCollection(String databaseName, String collectionName, DataBaseTypeEnum dbType) {
        return this.collectionService.truncateCollection(databaseName, collectionName, dbType);
    }

    @Override
    public AffectRes truncateCollectionView(String databaseName, String collectionName, DataBaseTypeEnum dbType) {
        return this.collectionService.truncateCollectionView(databaseName, collectionName, dbType);
    }

    @Override
    public void dropCollection(String databaseName, String collectionName) {
        this.collectionService.dropCollection(databaseName, collectionName);
    }

    @Override
    public AffectRes setAlias(String databaseName, String collectionName, String aliasName) {
        return this.collectionService.setAlias(databaseName, collectionName, aliasName);
    }

    @Override
    public AffectRes deleteAlias(String databaseName, String aliasName) {
        return this.collectionService.deleteAlias(databaseName, aliasName);
    }

    @Override
    public AffectRes setAIAlias(String databaseName, String collectionName, String aliasName) {
        return this.collectionService.setAIAlias(databaseName, collectionName, aliasName);
    }

    @Override
    public AffectRes deleteAIAlias(String databaseName, String aliasName) {
        return this.collectionService.deleteAIAlias(databaseName, aliasName);
    }

    @Override
    public List<CollectionView> listCollectionView(String databaseName) {
        return this.collectionService.listCollectionView(databaseName);
    }

    @Override
    public CollectionView describeCollectionView(String databaseName, String collectionName) {
        return this.collectionService.describeCollectionView(databaseName, collectionName);
    }

    @Override
    public AffectRes dropCollectionView(String databaseName, String collectionName) {
        return this.collectionService.dropCollectionView(databaseName, collectionName);
    }

    /*****************Document***********************/

    @Override
    public AffectRes upsertDocument(InsertParamInner param, boolean ai) {
        return this.documentService.upsertDocument(param, ai);
    }

    @Override
    public List<Document> queryDocument(QueryParamInner param, boolean ai) {
        return this.documentService.queryDocument(param, ai);
    }

    @Override
    public SearchRes searchDocument(SearchParamInner param, DataBaseTypeEnum dbType) {
        return this.documentService.searchDocument(param, dbType);
    }

    @Override
    public HybridSearchRes hybridSearchDocument(HybridSearchParamInner param, boolean ai) {
        return this.documentService.hybridSearchDocument(param, ai);
    }

    @Override
    public AffectRes deleteDocument(DeleteParamInner param) {
        return this.documentService.deleteDocument(param);
    }

    @Override
    public AffectRes updateDocument(UpdateParamInner param, boolean ai) {
        return this.documentService.updateDocument(param, ai);
    }

    @Override
    public BaseRes countDocument(QueryCountParamInner param, boolean ai) {
        return this.documentService.countDocument(param, ai);
    }

    @Override
    public List<DocumentSet> queryAIDocument(CollectionViewQueryParamInner queryParamInner) {
        return this.documentService.queryAIDocument(queryParamInner);
    }

    @Override
    public AffectRes deleteAIDocument(CollectionViewDeleteParamInner deleteParamInner) {
        return this.documentService.deleteAIDocument(deleteParamInner);
    }

    @Override
    public SearchContentRes searchAIDocument(SearchDocParamInner searchDocParamInner) {
        return this.documentService.searchAIDocument(searchDocParamInner);
    }

    @Override
    public AffectRes updateAIDocument(CollectionViewUpdateParamInner updateParamInner) {
        return this.documentService.updateAIDocument(updateParamInner);
    }

    @Override
    public FullTextSearchRes fullTextSearch(FullTextSearchParamInner param, boolean ai) {
        return this.documentService.fullTextSearch(param, ai);
    }

    /*****************Index**************************/

    @Override
    public BaseRes rebuildIndex(RebuildIndexParamInner param) {
        return this.indexService.rebuildIndex(param);
    }

    @Override
    public BaseRes rebuildAIIndex(RebuildIndexParamInner param) {
        return this.indexService.rebuildAIIndex(param);
    }

    @Override
    public BaseRes addIndex(AddIndexParamInner addIndexParamInner) {
        return this.indexService.addIndex(addIndexParamInner);
    }

    @Override
    public BaseRes modifyVectorIndex(ModifyIndexParamInner param, boolean ai) {
        return this.indexService.modifyVectorIndex(param, ai);
    }

    @Override
    public BaseRes dropIndex(DropIndexParamInner dropIndexParamInner) {
        return this.indexService.dropIndex(dropIndexParamInner);
    }

    /*****************User***************************/

    @Override
    public BaseRes createUser(UserCreateParam userCreateParam) {
        return this.userService.createUser(userCreateParam);
    }

    @Override
    public BaseRes grantToUser(UserGrantParam param) {
        return this.userService.grantToUser(param);
    }

    @Override
    public BaseRes revokeFromUser(UserRevokeParam param) {
        return this.userService.revokeFromUser(param);
    }

    @Override
    public UserDescribeRes describeUser(UserDescribeParam userDescribeParam) {
        return this.userService.describeUser(userDescribeParam);
    }

    @Override
    public UserListRes listUser() {
        return this.userService.listUser();
    }

    @Override
    public BaseRes dropUser(UserDropParam userDropParam) {
        return this.userService.dropUser(userDropParam);
    }

    @Override
    public BaseRes changeUserPassword(UserChangePasswordParam build) {
        return this.userService.changeUserPassword(build);
    }

    /*****************Embedding************************/

    @Override
    public AtomicEmbeddingRes atomicEmbedding(AtomicEmbeddingParam param) {
        return this.embeddingService.atomicEmbedding(param);
    }

    /*****************DocumentAI (Service)************************/

    @Override
    public void upload(String databaseName, String collectionViewName, LoadAndSplitTextParam loadAndSplitTextParam, Map<String, Object> metaDataMap) throws Exception {
        this.documentAIService.upload(databaseName, collectionViewName, loadAndSplitTextParam, metaDataMap);
    }

    @Override
    public void collectionUpload(String databaseName, String collectionName, UploadFileParam loadAndSplitTextParam, Map<String, Object> metaDataMap) throws Exception {
        this.documentAIService.collectionUpload(databaseName, collectionName, loadAndSplitTextParam, metaDataMap);
    }

    @Override
    public GetDocumentSetRes getFile(String databaseName, String collectionName, String documentSetName, String documentSetId) {
        return this.documentAIService.getFile(databaseName, collectionName, documentSetName, documentSetId);
    }

    @Override
    public GetChunksRes getChunks(String databaseName, String collectionName, String documentSetName, String documentSetId,
                                  Integer limit, Integer offset) {
        return this.documentAIService.getChunks(databaseName, collectionName, documentSetName, documentSetId, limit, offset);
    }

    @Override
    public GetImageUrlRes GetImageUrl(GetImageUrlParamInner param) {
        return this.documentAIService.getImageUrl(param);
    }

    @Override
    public QueryFileDetailRes queryFileDetails(QueryFileDetailsParamInner param) {
        return this.documentAIService.queryFileDetails(param);
    }

    @Override
    public synchronized void close() {
        if (this.client != null){
            this.client.dispatcher().executorService().shutdown();
        }
    }
}
