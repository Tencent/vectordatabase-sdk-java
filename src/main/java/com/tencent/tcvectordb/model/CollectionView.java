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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.tcvectordb.exception.ParamException;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.param.collection.IndexField;
import com.tencent.tcvectordb.model.param.collectionView.EmbeddingParams;
import com.tencent.tcvectordb.model.param.collectionView.LoadAndSplitTextParam;
import com.tencent.tcvectordb.model.param.collectionView.SplitterPreprocessParams;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.*;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.service.Stub;
import com.tencent.tcvectordb.service.param.*;

import java.util.*;

/**
 * VectorDB Collection
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
    private List<String> alias;

    protected List<IndexField> indexes;

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

    public CollectionView() {
    }

    public List<DocumentSet> query(CollectionViewQueryParam param) throws VectorDBException {
        List<DocumentSet> documentSets = this.stub.queryAIDocument(
                new CollectionViewQueryParamInner(database, collectionView, param, this.readConsistency));
        documentSets.forEach(documentSet -> {
            documentSet.setCollectionViewName(collectionView);
            documentSet.setStub(stub);
            documentSet.setDatabase(database);
        });
        return documentSets;
    }

    public List<DocumentSet> query() throws VectorDBException {
        List<DocumentSet> documentSets = this.stub.queryAIDocument(
                new CollectionViewQueryParamInner(database, collectionView,
                        CollectionViewQueryParam.newBuilder().build(), this.readConsistency));

        documentSets.forEach(documentSet -> {
            documentSet.setCollectionViewName(collectionView);
            documentSet.setStub(stub);
            documentSet.setDatabase(database);
        });
        return documentSets;
    }

    public List<DocumentSet> query(int limit) throws VectorDBException {
        List<DocumentSet> documentSets = this.stub.queryAIDocument(
                new CollectionViewQueryParamInner(database, collectionView,
                        CollectionViewQueryParam.newBuilder().withLimit(limit).build(),
                        this.readConsistency));
        documentSets.forEach(documentSet -> {
            documentSet.setCollectionViewName(collectionView);
            documentSet.setStub(stub);
            documentSet.setDatabase(database);
        });
        return documentSets;
    }

    public List<DocumentSet> query(int limit, int offset) throws VectorDBException {
        List<DocumentSet> documentSets = this.stub.queryAIDocument(
                new CollectionViewQueryParamInner(database, collectionView,
                        CollectionViewQueryParam.newBuilder().withLimit(limit).withOffset(offset).build(),
                        this.readConsistency));

        documentSets.forEach(documentSet -> {
            documentSet.setCollectionViewName(collectionView);
            documentSet.setStub(stub);
            documentSet.setDatabase(database);
        });
        return documentSets;
    }

    public DocumentSet getDocumentSetByName(String documentSetName) throws VectorDBException {
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

    public DocumentSet getDocumentSetById(String documentSetId) throws VectorDBException {
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

    public List<SearchContentInfo> search(SearchByContentsParam param) throws VectorDBException {
        return this.stub.searchAIDocument(new SearchDocParamInner(
                database, collectionView, param, this.readConsistency)).getDocuments();
    }


    public AffectRes deleteDocumentSets(CollectionViewConditionParam param) throws VectorDBException {
        return this.stub.deleteAIDocument(
                new CollectionViewDeleteParamInner(database, collectionView, param));
    }

    public AffectRes deleteByDocumentSetName(String documentSetName) throws VectorDBException {
        return this.stub.deleteAIDocument(
                new CollectionViewDeleteParamInner(database, collectionView,
                        CollectionViewConditionParam.newBuilder().withDocumentSetIds(Arrays.asList(documentSetName)).build()));
    }

    public AffectRes deleteByDocumentSetId(String documentSetId) throws VectorDBException {
        return this.stub.deleteAIDocument(
                new CollectionViewDeleteParamInner(database, collectionView,
                        CollectionViewConditionParam.newBuilder().withDocumentSetIds(Arrays.asList(documentSetId)).build()));
    }

    public AffectRes update(CollectionViewConditionParam param, Map<String, Object> updateFieldValues) throws VectorDBException {

        return this.stub.updateAIDocument(
                new CollectionViewUpdateParamInner(database, collectionView, param, updateFieldValues));
    }

    public void loadAndSplitText(LoadAndSplitTextParam loadAndSplitTextParam, Map<String, Object> metaDataMap) throws Exception {
        this.stub.upload(database, collectionView, loadAndSplitTextParam, metaDataMap);
    }

    public DocumentFileContent getFile(String fileName, String fileId) {
        return this.stub.getFile(database, collectionView, fileName, fileId).getDocumentSet();
    }

    public BaseRes rebuildIndex(RebuildIndexParam rebuildIndexParam) throws VectorDBException {
        return this.stub.rebuildAIIndex(new RebuildIndexParamInner(database, collectionView, rebuildIndexParam));
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new ParamException(String.format(
                    "Create collection param error: %s", e));
        }
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
