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

package com.tencent.tcvectordb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.param.collection.IndexField;
import com.tencent.tcvectordb.model.param.collectionView.EmbeddingParams;
import com.tencent.tcvectordb.model.param.collectionView.LoadAndSplitTextParam;
import com.tencent.tcvectordb.model.param.collectionView.ParsingProcessParam;
import com.tencent.tcvectordb.model.param.collectionView.SplitterPreprocessParams;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.*;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.service.Stub;
import com.tencent.tcvectordb.service.param.*;
import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.*;

/**
 * VectorDB CollectionView
 *<ol>
 *<li> database: the database name of collection </li>
 *<li> collectionView: name of collectionView, this name must be unique</li>
 *<li> description: description of the collection,</li>
 *<li> indexes: index field of the collection;
 *      field type could be string，uint64, array
 *      index type could be primaryKey, filter if the field is scalar field, id field must be primaryKey;
 *      fieldElementType could be string if the field type array;</li>
 *<li> alias: alias of the collection </li>
 *<li> embedding: embedding config should be set if collection use embedding function </li>
 *<li> expectedFileNum: expected file numbers of the collectionView </li>
 *<li> averageFileSize: average size of the file uploaded to the collectionView </li>
 *<li> parsingProcess: document parsing parameters </li>
 *</ol>
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionView {
    @JsonIgnore
    private Stub stub;
    private String database;
    protected String collectionView;
    @JsonIgnore
    protected ReadConsistencyEnum readConsistency;
    protected String description;
    private String createTime;
    private AIStatus stats;
    protected SplitterPreprocessParams splitterPreprocess;
    protected EmbeddingParams embedding;
    protected Integer expectedFileNum;
    protected Integer averageFileSize;
    private List<String> alias;

    protected List<IndexField> indexes;

    protected ParsingProcessParam parsingProcess;

    @JsonIgnore
    private String connectCollectionName;

    public void setConnectCollectionName(String connectCollectionName) {
        this.connectCollectionName = connectCollectionName;
    }

    public List<IndexField> getIndexes() {
        return indexes;
    }

    public String getCollectionView() {
        return collectionView;
    }

    public AIStatus getStats() {
        return stats;
    }

    public SplitterPreprocessParams getSplitterPreprocess() {
        return splitterPreprocess;
    }

    public List<String> getAlias() {
        return alias;
    }

    public Stub getStub() {
        return stub;
    }

    public void setStub(Stub stub) {
        this.stub = stub;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }


    public ReadConsistencyEnum getReadConsistency() {
        return readConsistency;
    }

    public void setReadConsistency(ReadConsistencyEnum readConsistency) {
        this.readConsistency = readConsistency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public EmbeddingParams getEmbedding() {
        return embedding;
    }

    public void setEmbedding(EmbeddingParams embedding) {
        this.embedding = embedding;
    }

    public Integer getExpectedFileNum() {
        return expectedFileNum;
    }

    public void setExpectedFileNum(Integer expectedFileNum) {
        this.expectedFileNum = expectedFileNum;
    }

    public Integer getAverageFileSize() {
        return averageFileSize;
    }

    public void setAverageFileSize(Integer averageFileSize) {
        this.averageFileSize = averageFileSize;
    }

    public ParsingProcessParam getParsingProcess() {
        return parsingProcess;
    }

    public void setParsingProcess(ParsingProcessParam parsingProcess) {
        this.parsingProcess = parsingProcess;
    }

    public CollectionView() {
    }

    /**
     * query document set by params include document_set_id, document_set_name, filter, limit, offset, output_fields
     * @param param CollectionViewQueryParam
     *             document_set_id  : DocumentSet's id to query
     *             document_set_name: DocumentSet's name to query
     *             filter           : The optional filter condition of the scalar index field.
     *             limit            : The limit of the query result
     *             offset           : The offset of the query result
     *             output_fields    : The fields to return when query
     *             timeout          : An optional duration of time in seconds to allow for the request
     *                                When timeout is set to None, will use the connect timeout
     * @return List<DocumentSet>
     * @throws VectorDBException
     */
    public List<DocumentSet> query(CollectionViewQueryParam param) throws VectorDBException {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }

        List<DocumentSet> documentSets = this.stub.queryAIDocument(
                new CollectionViewQueryParamInner(database, collectionView, param, this.readConsistency));
        for (DocumentSet documentSet : documentSets) {
            documentSet.setCollectionViewName(collectionView);
            documentSet.setStub(stub);
            documentSet.setDatabase(database);
        }
        return documentSets;
    }

    /**
     * query document set by default params include limit is 3, offset is 0 and output_fields is all fields
     * @return
     * @throws VectorDBException
     */
    public List<DocumentSet> query() throws VectorDBException {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }
        List<DocumentSet> documentSets = this.stub.queryAIDocument(
                new CollectionViewQueryParamInner(database, collectionView,
                        CollectionViewQueryParam.newBuilder().build(), this.readConsistency));

        for (DocumentSet documentSet : documentSets) {
            documentSet.setCollectionViewName(collectionView);
            documentSet.setStub(stub);
            documentSet.setDatabase(database);
        }
        return documentSets;
    }

    /**
     * query document set by limit
     * @param limit
     * @return
     * @throws VectorDBException
     */
    public List<DocumentSet> query(int limit) throws VectorDBException {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }
        List<DocumentSet> documentSets = this.stub.queryAIDocument(
                new CollectionViewQueryParamInner(database, collectionView,
                        CollectionViewQueryParam.newBuilder().withLimit(limit).build(),
                        this.readConsistency));
        for (DocumentSet documentSet : documentSets) {
            documentSet.setCollectionViewName(collectionView);
            documentSet.setStub(stub);
            documentSet.setDatabase(database);
        }
        return documentSets;
    }

    /**
     * query document set by limit and offset
     * @param limit
     * @param offset
     * @return
     * @throws VectorDBException
     */
    public List<DocumentSet> query(int limit, int offset) throws VectorDBException {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }
        List<DocumentSet> documentSets = this.stub.queryAIDocument(
                new CollectionViewQueryParamInner(database, collectionView,
                        CollectionViewQueryParam.newBuilder().withLimit(limit).withOffset(offset).build(),
                        this.readConsistency));

        for (DocumentSet documentSet : documentSets) {
            documentSet.setCollectionViewName(collectionView);
            documentSet.setStub(stub);
            documentSet.setDatabase(database);
        }
        return documentSets;
    }

    /**
     * query document set by documentSetName
     * @param documentSetName
     * @return
     * @throws VectorDBException
     */
    public DocumentSet getDocumentSetByName(String documentSetName) throws VectorDBException {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }
        List<DocumentSet> documentSets = this.stub.queryAIDocument(
                new CollectionViewQueryParamInner(database, collectionView,
                        CollectionViewQueryParam.newBuilder().withDocumentSetNames(Arrays.asList(documentSetName)).build(),
                        this.readConsistency));
        if (documentSets.size()>0){
            DocumentSet documentSet = documentSets.get(0);
            documentSet.setCollectionViewName(collectionView);
            documentSet.setStub(stub);
            documentSet.setDatabase(database);
            return documentSet;
        }
        throw new VectorDBException("data not existed!");
    }

    /**
     * query document set by documentSetId
     * @param documentSetId
     * @return
     * @throws VectorDBException
     */
    public DocumentSet getDocumentSetById(String documentSetId) throws VectorDBException {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }
        List<DocumentSet> documentSets = this.stub.queryAIDocument(
                new CollectionViewQueryParamInner(database, collectionView,
                        CollectionViewQueryParam.newBuilder().withDocumentSetIds(Arrays.asList(documentSetId)).build(),
                        this.readConsistency));
        if (documentSets.size()>0){
            DocumentSet documentSet = documentSets.get(0);
            documentSet.setCollectionViewName(collectionView);
            documentSet.setStub(stub);
            documentSet.setDatabase(database);
            return documentSet;
        }
        throw new VectorDBException("data not existed!");
    }

    /**
     * search documentSet by contents with similarity
     * @param param SearchByContentsParam:
     *              contents: The contents to search
     *              document_set_name: DocumentSet's name
     *              expand_chunk     : Parameters for Forward and Backward Expansion of Chunks
     *              rerank           : Parameters for Rerank
     *              filter           : The optional filter condition of the scalar index field
     *              limit            : The limit of the query result, not support now
     *
     * @return List<SearchContentInfo>
     * @throws VectorDBException
     */
    public List<SearchContentInfo> search(SearchByContentsParam param) throws VectorDBException {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }
        return this.stub.searchAIDocument(new SearchDocParamInner(
                database, collectionView, param, this.readConsistency)).getDocuments();
    }


    /**
     * delete document set by documentSetName, documentSetId or filter
     * @param param CollectionViewConditionParam:
     *              documentSetId: DocumentSet's id to filter
     *              documentSetNamed: DocumentSet's name to filter
     *              filter           : The optional filter condition of the scalar index field
     * @return AffectRes
     * @throws VectorDBException
     */
    public AffectRes deleteDocumentSets(CollectionViewConditionParam param) throws VectorDBException {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }
        return this.stub.deleteAIDocument(
                new CollectionViewDeleteParamInner(database, collectionView, param));
    }

    /**
     * delete document set by documentSetName
     * @param documentSetName
     * @return AffectRes
     * @throws VectorDBException
     */
    public AffectRes deleteByDocumentSetName(String documentSetName) throws VectorDBException {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }
        return this.stub.deleteAIDocument(
                new CollectionViewDeleteParamInner(database, collectionView,
                        CollectionViewConditionParam.newBuilder().withDocumentSetNames(Arrays.asList(documentSetName)).build()));
    }

    /**
     * delete document set by documentSetId
     * @param documentSetId: DocumentSet's id to filter
     * @return AffectRes
     * @throws VectorDBException
     */
    public AffectRes deleteByDocumentSetId(String documentSetId) throws VectorDBException {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }
        return this.stub.deleteAIDocument(
                new CollectionViewDeleteParamInner(database, collectionView,
                        CollectionViewConditionParam.newBuilder().withDocumentSetIds(Arrays.asList(documentSetId)).build()));
    }

    /**
     * update a document set by documentSetName, documentSetId or filter
     * @param param CollectionViewConditionParam:
     *               documentSetId: DocumentSet's id to update
     *               documentSetNamed: DocumentSet's name to update
     *               filter           : The optional filter condition of the scalar index field
     * @param updateFieldValues: The update field values
     * @return
     * @throws VectorDBException
     */
    public AffectRes update(CollectionViewConditionParam param, Map<String, Object> updateFieldValues) throws VectorDBException {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }
        return this.stub.updateAIDocument(
                new CollectionViewUpdateParamInner(database, collectionView, param, updateFieldValues));
    }

    /**
     * Upload local file or file input stream, parse and save it remotely.
     * @param loadAndSplitTextParam:
     *             localFilePath  : File path to load
     *             documentSetName: File name as DocumentSet
     *             splitterProcess : Args for splitter process
     *             parsingProcess  : Document parsing parameters
     *             fileInputStream: file input stream; user input stream, when use this way,  documentSetName、inputStreamSize and fileType params must be specified
     *             fileType:        file type
     *             inputStreamSize : input stream size
     * @param metaDataMap: extra properties to save
     * @throws Exception
     */
    public void loadAndSplitText(LoadAndSplitTextParam loadAndSplitTextParam, Map<String, Object> metaDataMap) throws Exception {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }
        this.stub.upload(database, collectionView,  loadAndSplitTextParam, metaDataMap);
    }

    /**
     * Upload local file or file input stream, parse and save it remotely.
     * @param loadAndSplitTextParam:
     *             localFilePath  : File path to load
     *             documentSetName: File name as DocumentSet
     *             splitterProcess : Args for splitter process
     *             parsingProcess  : Document parsing parameters
     *             fileInputStream: file input stream; user input stream, when use this way,  documentSetName、inputStreamSize and fileType params must be specified
     *             fileType:        file type
     *             inputStreamSize : input stream size
     * @throws Exception
     */
    public void loadAndSplitText(LoadAndSplitTextParam loadAndSplitTextParam) throws Exception {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }
        this.stub.upload(database, collectionView,  loadAndSplitTextParam, Collections.EMPTY_MAP);
    }

    /**
     * get file content by fileName and fileId
     * @param fileName: file name as same as documentSetName
     * @param fileId: file id as same as documentSetName
     * @return
     */
    public DocumentFileContent getFile(String fileName, String fileId) {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }
        return this.stub.getFile(database, collectionView, fileName, fileId).getDocumentSet();
    }

    /**
     * rebuild index
     * @param rebuildIndexParam: index param
     * @return BaseRes
     * @throws VectorDBException
     */
    public BaseRes rebuildIndex(RebuildIndexParam rebuildIndexParam) throws VectorDBException {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }
        return this.stub.rebuildAIIndex(new RebuildIndexParamInner(database, collectionView, rebuildIndexParam));
    }

    /**
     * get chunks of documentSet
     * @param documentSetName: documentSet's name
     * @param limit
     * @param offset
     * @return GetChunksRes
     */
    public GetChunksRes getChunks(String documentSetName, Integer limit, Integer offset) {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }
        return this.stub.getChunks(database, collectionView, documentSetName, null, limit, offset);
    }

    /**
     * get chunks of documentSet by default limit
     * @param documentSetName: documentSet's name
     * @return GetChunksRes
     */
    public GetChunksRes getChunks(String documentSetName) {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }
        return this.stub.getChunks(database, collectionView, documentSetName, null, null, null);
    }

    /**
     * get chunks of documentSet by documentSetId, documentSetName, limit and offset
     * @param documentSetId: documentSet's id
     * @param documentSetName: documentSet's name
     * @param limit: limit
     * @param offset: offset
     * @return GetChunksRes
     */
    public GetChunksRes getChunks(String documentSetId, String documentSetName, Integer limit, Integer offset) {
        String collectionView = this.collectionView;
        if (this.connectCollectionName != null) {
            collectionView = this.connectCollectionName;
        }
        return this.stub.getChunks(database, collectionView, documentSetName, documentSetId, limit, offset);
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }


    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class IndexStatus {
        private String status;
        private Date startTime;

        public String getStatus() {
            return status;
        }

        public Date getStartTime() {
            return startTime;
        }

        @Override
        public String toString() {
            return "IndexStatus{" +
                    "status='" + status + '\'' +
                    ", startTime=" + startTime +
                    '}';
        }
    }
}
