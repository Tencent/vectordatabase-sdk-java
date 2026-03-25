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
import com.tencent.tcvectordb.model.Collection;
import com.tencent.tcvectordb.model.param.collection.UploadFileParam;
import com.tencent.tcvectordb.model.param.collectionView.CreateCollectionViewParam;
import com.tencent.tcvectordb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvectordb.model.param.collectionView.LoadAndSplitTextParam;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.model.param.dml.AtomicEmbeddingParam;
import com.tencent.tcvectordb.model.param.entity.*;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.model.param.user.*;
import com.tencent.tcvectordb.rpc.pool.ChannelPool;
import com.tencent.tcvectordb.service.impl.grpc.*;
import com.tencent.tcvectordb.service.param.*;

import java.util.List;
import java.util.Map;

/**
 * gRPC implementation of Stub interface.
 * Delegates operations to modular gRPC services and falls back to HTTP for unsupported operations.
 */
public class GrpcStub extends HttpStub {

    private final int maxReceiveMessageSize = 100*1024*1024;
    private String authorization;
    private int timeout = 10;
    private ConnectParam connectParam;
    private final ChannelPool channelPool;
    
    // gRPC Service modules
    private DatabaseGrpcService databaseGrpcService;
    private CollectionGrpcService collectionGrpcService;
    private DocumentGrpcService documentGrpcService;
    private IndexGrpcService indexGrpcService;
    private UserGrpcService userGrpcService;
    private EmbeddingGrpcService embeddingGrpcService;
    private DocumentAIGrpcService documentAIGrpcService;

    public GrpcStub(ConnectParam param){
        super();
        connectParam = param;
        this.authorization = String.format("Bearer account=%s&api_key=%s",param.getUsername(), param.getKey());

        if (param.getTimeout() > 0) {
            this.timeout = param.getTimeout();
        }
        channelPool = new ChannelPool(param, maxReceiveMessageSize, this.authorization);
        
        // Initialize all gRPC service modules
        this.databaseGrpcService = new DatabaseGrpcService();
        this.databaseGrpcService.init(param, channelPool, this.authorization, this.timeout, maxReceiveMessageSize);
        
        this.collectionGrpcService = new CollectionGrpcService();
        this.collectionGrpcService.init(param, channelPool, this.authorization, this.timeout, maxReceiveMessageSize);
        this.collectionGrpcService.setStub(this);  // Set stub reference for collections
        
        this.documentGrpcService = new DocumentGrpcService();
        this.documentGrpcService.init(param, channelPool, this.authorization, this.timeout, maxReceiveMessageSize);
        
        this.indexGrpcService = new IndexGrpcService();
        this.indexGrpcService.init(param, channelPool, this.authorization, this.timeout, maxReceiveMessageSize);
        
        this.userGrpcService = new UserGrpcService();
        this.userGrpcService.init(param, channelPool, this.authorization, this.timeout, maxReceiveMessageSize);
        
        this.embeddingGrpcService = new EmbeddingGrpcService();
        this.embeddingGrpcService.init(param, channelPool, this.authorization, this.timeout, maxReceiveMessageSize);
        
        this.documentAIGrpcService = new DocumentAIGrpcService();
        this.documentAIGrpcService.init(param, channelPool, this.authorization, this.timeout, maxReceiveMessageSize);
    }

    @Override
    public synchronized void close() {
        super.close();
        if (this.channelPool!=null){
            this.channelPool.close();
        }
    }

    /*****************Database***********************/

    @Override
    public void createDatabase(Database database) {
        this.databaseGrpcService.createDatabase(database);
    }

    @Override
    public void dropDatabase(Database database) {
        this.databaseGrpcService.dropDatabase(database);
    }

    @Override
    public AffectRes createAIDatabase(AIDatabase aiDatabase) {
        super.initHttpStub(this.connectParam);
        return super.createAIDatabase(aiDatabase);
    }

    @Override
    public DataBaseTypeRes describeDatabase(Database database) {
        return this.databaseGrpcService.describeDatabase(database);
    }

    @Override
    public AffectRes dropAIDatabase(AIDatabase aiDatabase) {
        super.initHttpStub(this.connectParam);
        return super.dropAIDatabase(aiDatabase);
    }

    @Override
    public List<String> listDatabases() {
        return this.databaseGrpcService.listDatabases();
    }

    @Override
    public Map<String, DataBaseType> listDatabaseInfos() {
        return this.databaseGrpcService.listDatabaseInfos();
    }

    /*****************Collection*********************/

    @Override
    public void createCollection(CreateCollectionParam params) {
        this.collectionGrpcService.createCollection(params);
    }

    @Override
    public void createCollectionView(CreateCollectionViewParam params) {
        super.initHttpStub(this.connectParam);
        super.createCollectionView(params);
    }

    @Override
    public List<Collection> listCollections(String databaseName) {
        return this.collectionGrpcService.listCollections(databaseName);
    }

    @Override
    public Collection describeCollection(String databaseName, String collectionName) {
        return this.collectionGrpcService.describeCollection(databaseName, collectionName);
    }

    @Override
    public AffectRes truncateCollection(String databaseName, String collectionName, DataBaseTypeEnum dbType) {
        return this.collectionGrpcService.truncateCollection(databaseName, collectionName, dbType);
    }

    @Override
    public AffectRes truncateCollectionView(String databaseName, String collectionName, DataBaseTypeEnum dbType) {
        super.initHttpStub(this.connectParam);
        return super.truncateCollectionView(databaseName, collectionName, dbType);
    }

    @Override
    public void dropCollection(String databaseName, String collectionName) {
        this.collectionGrpcService.dropCollection(databaseName, collectionName);
    }

    @Override
    public AffectRes setAlias(String databaseName, String collectionName, String aliasName) {
        return this.collectionGrpcService.setAlias(databaseName, collectionName, aliasName);
    }

    @Override
    public AffectRes deleteAlias(String databaseName, String aliasName) {
        return this.collectionGrpcService.deleteAlias(databaseName, aliasName);
    }

    @Override
    public AffectRes setAIAlias(String databaseName, String collectionName, String aliasName) {
        super.initHttpStub(this.connectParam);
        return super.setAIAlias(databaseName, collectionName, aliasName);
    }

    @Override
    public AffectRes deleteAIAlias(String databaseName, String aliasName) {
        super.initHttpStub(this.connectParam);
        return super.deleteAIAlias(databaseName, aliasName);
    }

    @Override
    public List<CollectionView> listCollectionView(String databaseName) {
        super.initHttpStub(this.connectParam);
        return super.listCollectionView(databaseName);
    }

    @Override
    public CollectionView describeCollectionView(String databaseName, String collectionName) {
        super.initHttpStub(this.connectParam);
        return super.describeCollectionView(databaseName, collectionName);
    }

    @Override
    public AffectRes dropCollectionView(String databaseName, String collectionName) {
        super.initHttpStub(this.connectParam);
        return super.dropCollectionView(databaseName, collectionName);
    }

    /*****************Document***********************/

    @Override
    public AffectRes upsertDocument(InsertParamInner param, boolean ai) {
        return this.documentGrpcService.upsertDocument(param, ai);
    }

    @Override
    public List<Document> queryDocument(QueryParamInner param, boolean ai) {
        return this.documentGrpcService.queryDocument(param, ai);
    }

    @Override
    public SearchRes searchDocument(SearchParamInner param, DataBaseTypeEnum dbType) {
        if (dbType.equals(DataBaseTypeEnum.AI_DB)){
            super.initHttpStub(this.connectParam);
            return super.searchDocument(param, dbType);
        }
        return this.documentGrpcService.searchDocument(param, dbType);
    }

    @Override
    public HybridSearchRes hybridSearchDocument(HybridSearchParamInner param, boolean ai) {
        return this.documentGrpcService.hybridSearchDocument(param, ai);
    }

    @Override
    public AffectRes deleteDocument(DeleteParamInner param) {
        return this.documentGrpcService.deleteDocument(param);
    }

    @Override
    public AffectRes updateDocument(UpdateParamInner param, boolean ai) {
        return this.documentGrpcService.updateDocument(param, ai);
    }

    @Override
    public BaseRes countDocument(QueryCountParamInner param, boolean ai) {
        return this.documentGrpcService.countDocument(param, ai);
    }

    @Override
    public List<DocumentSet> queryAIDocument(CollectionViewQueryParamInner queryParamInner) {
        super.initHttpStub(this.connectParam);
        return super.queryAIDocument(queryParamInner);
    }

    @Override
    public AffectRes deleteAIDocument(CollectionViewDeleteParamInner deleteParamInner) {
        super.initHttpStub(this.connectParam);
        return super.deleteAIDocument(deleteParamInner);
    }

    @Override
    public SearchContentRes searchAIDocument(SearchDocParamInner searchDocParamInner) {
        super.initHttpStub(this.connectParam);
        return super.searchAIDocument(searchDocParamInner);
    }

    @Override
    public AffectRes updateAIDocument(CollectionViewUpdateParamInner updateParamInner) {
        super.initHttpStub(this.connectParam);
        return super.updateAIDocument(updateParamInner);
    }

    @Override
    public FullTextSearchRes fullTextSearch(FullTextSearchParamInner param, boolean ai) {
        return this.documentGrpcService.fullTextSearch(param, ai);
    }

    /*****************Index**************************/

    @Override
    public BaseRes rebuildIndex(RebuildIndexParamInner param) {
        return this.indexGrpcService.rebuildIndex(param);
    }

    @Override
    public BaseRes rebuildAIIndex(RebuildIndexParamInner param) {
        super.initHttpStub(this.connectParam);
        return super.rebuildAIIndex(param);
    }

    @Override
    public BaseRes addIndex(AddIndexParamInner addIndexParamInner) {
        return this.indexGrpcService.addIndex(addIndexParamInner);
    }

    @Override
    public BaseRes modifyVectorIndex(ModifyIndexParamInner param, boolean ai) {
        return this.indexGrpcService.modifyVectorIndex(param, ai);
    }

    @Override
    public BaseRes dropIndex(DropIndexParamInner dropIndexParamInner) {
        return this.indexGrpcService.dropIndex(dropIndexParamInner);
    }

    /*****************User***************************/

    @Override
    public BaseRes createUser(UserCreateParam userCreateParam) {
        return this.userGrpcService.createUser(userCreateParam);
    }

    @Override
    public BaseRes grantToUser(UserGrantParam param) {
        return this.userGrpcService.grantToUser(param);
    }

    @Override
    public BaseRes revokeFromUser(UserRevokeParam param) {
        return this.userGrpcService.revokeFromUser(param);
    }

    @Override
    public UserDescribeRes describeUser(UserDescribeParam userDescribeParam) {
        return this.userGrpcService.describeUser(userDescribeParam);
    }

    @Override
    public UserListRes listUser() {
        return this.userGrpcService.listUser();
    }

    @Override
    public BaseRes dropUser(UserDropParam userDropParam) {
        return this.userGrpcService.dropUser(userDropParam);
    }

    @Override
    public BaseRes changeUserPassword(UserChangePasswordParam build) {
        return this.userGrpcService.changeUserPassword(build);
    }

    /*****************Embedding**********************/

    @Override
    public AtomicEmbeddingRes atomicEmbedding(AtomicEmbeddingParam param) {
        super.initHttpStub(this.connectParam);
        return super.atomicEmbedding(param);
    }

    /*****************Service (DocumentAI)***********/

    @Override
    public void upload(String databaseName, String collectionName, LoadAndSplitTextParam loadAndSplitTextParam, Map<String, Object> metaDataMap) throws Exception {
        super.initHttpStub(this.connectParam);
        super.upload(databaseName, collectionName, loadAndSplitTextParam, metaDataMap);
    }

    @Override
    public void collectionUpload(String databaseName, String collectionName, UploadFileParam loadAndSplitTextParam, Map<String, Object> metaDataMap) throws Exception {
        super.initHttpStub(this.connectParam);
        super.collectionUpload(databaseName, collectionName, loadAndSplitTextParam, metaDataMap);
    }

    @Override
    public GetDocumentSetRes getFile(String databaseName, String collectionName, String fileName, String fileId) {
        super.initHttpStub(this.connectParam);
        return super.getFile(databaseName, collectionName, fileName, fileId);
    }

    @Override
    public GetChunksRes getChunks(String databaseName, String collectionName, String documentSetName, String documentSetId, Integer limit, Integer offset) {
        super.initHttpStub(this.connectParam);
        return super.getChunks(databaseName, collectionName, documentSetName, documentSetId, limit, offset);
    }

    @Override
    public GetImageUrlRes GetImageUrl(GetImageUrlParamInner param) {
        super.initHttpStub(this.connectParam);
        return super.GetImageUrl(param);
    }

    @Override
    public QueryFileDetailRes queryFileDetails(QueryFileDetailsParamInner param) {
        super.initHttpStub(this.connectParam);
        return super.queryFileDetails(param);
    }
}
