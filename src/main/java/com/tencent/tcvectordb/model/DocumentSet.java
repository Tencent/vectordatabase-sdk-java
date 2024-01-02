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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.param.dml.BaseQuery;
import com.tencent.tcvectordb.model.param.dml.CollectionViewConditionParam;
import com.tencent.tcvectordb.model.param.dml.QueryParam;
import com.tencent.tcvectordb.model.param.dml.SearchByContentsParam;
import com.tencent.tcvectordb.model.param.entity.*;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.service.Stub;
import com.tencent.tcvectordb.service.param.CollectionViewDeleteParamInner;
import com.tencent.tcvectordb.service.param.CollectionViewQueryParamInner;
import com.tencent.tcvectordb.service.param.SearchDocParamInner;
import com.tencent.tcvectordb.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * VectorDB Document
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentSet {
    @JsonIgnore
    private Stub stub;
    private String database;
    protected String collectionViewName;
    private ReadConsistencyEnum readConsistency = ReadConsistencyEnum.EVENTUAL_CONSISTENCY;
    private String documentSetId;
    private String documentSetName;
    private String textPrefix;
    private DocumentSetInfo documentSetInfo;
    private List<DocField> docFields;
    private Map<String, Object> docKeyValue;

    public List<DocField> getDocFields() {
        return docFields;
    }

    public Map<String, Object> getDocKeyValue() {
        return docKeyValue;
    }

    public void setDocFields(List<DocField> docFields) {
        this.docFields = docFields;
    }

    public Object getObject(String key) {
        if (Objects.isNull(docFields) || docFields.isEmpty()) {
            return null;
        }
        ensureDocKeyValue();

        return docKeyValue.get(key);
    }

    private void ensureDocKeyValue() {
        if (Objects.isNull(docKeyValue)) {
            docKeyValue = new TreeMap<>();
            for (DocField docField : docFields) {
                docKeyValue.put(docField.getName(), docField.getValue());
            }
        }
    }

    public List<SearchContentInfo> search(SearchByContentsParam param) throws VectorDBException {
        param.setDocumentSetName(Arrays.asList(documentSetName));
        return this.stub.searchAIDocument(new SearchDocParamInner(
                database, collectionViewName, param, readConsistency)).getDocuments();
    }

    public GetChunksRes getChunks() throws VectorDBException {
        return this.stub.getChunks(database, collectionViewName, documentSetName, documentSetId, null, null);
    }

    public GetChunksRes getChunks(Integer limit) throws VectorDBException {
        return this.stub.getChunks(database, collectionViewName, documentSetName, documentSetId, limit, null);
    }

    public GetChunksRes getChunks(Integer limit, Integer offset) throws VectorDBException {
        return this.stub.getChunks(database, collectionViewName, documentSetName, documentSetId, limit, offset);
    }

    public AffectRes delete() throws VectorDBException {
        return this.stub.deleteAIDocument(
                new CollectionViewDeleteParamInner(database, collectionViewName,
                        CollectionViewConditionParam.newBuilder().withDocumentSetIds(Arrays.asList(documentSetId)).build()));
    }

    public String getText() {
        return this.stub.getFile(database, collectionViewName, documentSetName, documentSetId).getDocumentSet().getText();
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

    public String getCollectionViewName() {
        return collectionViewName;
    }

    public void setCollectionViewName(String collectionViewname) {
        this.collectionViewName = collectionViewname;
    }

    @Override
    public String toString() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        if (StringUtils.isNotBlank(documentSetId)) {
            node.put("documentSetId", documentSetId);
        }
        if (StringUtils.isNotEmpty(documentSetName)) {
            node.put("documentSetName", documentSetName);
        }
        if (StringUtils.isNotEmpty(textPrefix)) {
            node.put("textPrefix", textPrefix);
        }
        if (documentSetInfo!=null) {
            node.put("documentSetInfo", JsonUtils.toJsonString(documentSetInfo));
        }

        if (docFields != null && !docFields.isEmpty()) {
            for (DocField field : docFields) {
                switch (field.getFieldType()) {
                    case Uint64:
                        node.put(field.getName(), Long.valueOf(field.getValue().toString()));
                        break;
                    case Array:
                        List<String> strValues = (List<String>) ((List) field.getValue());
                        ArrayNode strNode = JsonNodeFactory.instance.arrayNode();
                        strValues.forEach(strNode::add);
                        node.set(field.getName(), strNode);
                    default:
                        node.put(field.getName(), field.getStringValue());
                }
            }
        }
        return node.toString();
    }

    private DocumentSet(Builder builder) {
        this.docFields = builder.docFields;
        this.documentSetInfo = builder.documentSetInfo;
        this.documentSetName = builder.documnetSetName;
        this.documentSetId = builder.documentSetId;
        this.textPrefix = builder.textPrefix;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String documentSetId;
        private String documnetSetName;
        private String textPrefix;
        private DocumentSetInfo documentSetInfo;
        private List<DocField> docFields;

        public Builder() {
            this.docFields = new ArrayList<>();
        }

        public Builder withDocumentSetId(String documentSetId) {
            this.documentSetId = documentSetId;
            return this;
        }

        public Builder withDocumnetSetName(String documnetSetName) {
            this.documnetSetName = documnetSetName;
            return this;
        }

        public Builder withTextPrefix(String textPrefix) {
            this.textPrefix = textPrefix;
            return this;
        }

        public Builder withDocumentSetInfo(DocumentSetInfo documentSetInfo) {
            this.documentSetInfo = documentSetInfo;
            return this;
        }

        /**
         * This is a deprecated method.
         *
         * @param field
         * @return
         * @deprecated This method is deprecated and should not be used anymore. Please use the
         * addDocField(DocField field) or addDocFields(List<DocField> docFields) instead.
         */
        @Deprecated
        public Builder addFilterField(DocField field) {
            this.docFields.add(field);
            return this;
        }

        public Builder addDocField(DocField docField) {
            this.docFields.add(docField);
            return this;
        }

        public Builder addDocFields(List<DocField> docFields) {
            this.docFields.addAll(docFields);
            return this;
        }

        public DocumentSet build() {
            return new DocumentSet(this);
        }
    }
}