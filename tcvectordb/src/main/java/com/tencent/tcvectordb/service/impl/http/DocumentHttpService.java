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
package com.tencent.tcvectordb.service.impl.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.*;
import com.tencent.tcvectordb.model.param.dml.AtomicEmbeddingParam;
import com.tencent.tcvectordb.model.param.entity.*;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.service.ApiPath;
import com.tencent.tcvectordb.service.param.*;
import com.tencent.tcvectordb.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * HTTP service implementation for document operations.
 */
public class DocumentHttpService extends BaseHttpService {

    /**
     * Insert or update documents in a collection.
     */
    public AffectRes upsertDocument(InsertParamInner param, boolean ai) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DOC_UPSERT);
        JsonNode jsonNode = this.post(url, param.toString(), ai);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * Query documents from a collection based on specified criteria.
     */
    public List<Document> queryDocument(QueryParamInner param, boolean ai) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DOC_QUERY);
        JsonNode jsonNode = this.post(url, param.toString(), ai);
        JsonNode docsNode = jsonNode.get("documents");
        List<Document> dosc = new ArrayList<>();
        if (docsNode == null) {
            return dosc;
        }
        try {
            Iterator<JsonNode> iterator = docsNode.elements();
            while (iterator.hasNext()) {
                JsonNode node = iterator.next();
                Document doc = nodeToDoc(node);
                dosc.add(doc);
            }
            return dosc;
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format("VectorDBServer response " +
                    "from query error: can't parse documents=%s", docsNode));
        }
    }

    /**
     * Search documents using vector similarity.
     */
    public SearchRes searchDocument(SearchParamInner param, DataBaseTypeEnum dbType) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DOC_SEARCH);
        if (DataBaseTypeEnum.isAIDataBase(dbType)) {
            url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_DOCUMENT_SEARCH);
        }
        JsonNode jsonNode = this.post(url, param.toString(), false);
        JsonNode multiDocsNode = jsonNode.get("documents");
        int code = 0;
        if (jsonNode.get("code") != null) {
            code = jsonNode.get("code").asInt();
        }
        String msg = "";
        if (jsonNode.get("msg") != null) {
            msg = jsonNode.get("msg").asText();
        }
        String warning = "";
        if (jsonNode.get("warning") != null) {
            warning = jsonNode.get("warning").asText();
        }
        SearchRes searchRes = new SearchRes(code, msg, warning, Collections.emptyList());
        if (jsonNode.get("embeddingExtraInfo") != null){
            EmbeddingExtraInfo embeddingExtraInfo = new EmbeddingExtraInfo();
            embeddingExtraInfo.setTokenUsed(jsonNode.get("embeddingExtraInfo").get("tokenUsed").asLong());
            searchRes.setEmbeddingExtraInfo(embeddingExtraInfo);
        }

        if (multiDocsNode == null) {
            return searchRes;
        }
        try {
            List<List<Document>> multiDosc = new ArrayList<>();
            Iterator<JsonNode> multiIter = multiDocsNode.elements();
            while (multiIter.hasNext()) {
                JsonNode docNode = multiIter.next();
                Iterator<JsonNode> iter = docNode.elements();
                List<Document> docs = new ArrayList<>();
                while (iter.hasNext()) {
                    JsonNode node = iter.next();
                    Document doc = nodeToDoc(node);
                    docs.add(doc);
                }
                multiDosc.add(docs);
            }
            searchRes.setDocuments(Collections.unmodifiableList(multiDosc));
            return searchRes;
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format("VectorDBServer response " +
                    "from search error: can't parse documents=%s", multiDocsNode));
        }
    }

    /**
     * Perform hybrid search combining vector and text search.
     */
    public HybridSearchRes hybridSearchDocument(HybridSearchParamInner param, boolean ai) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DOC_HYBRID_SEARCH);
        JsonNode jsonNode = this.post(url, param.toString(), ai);
        JsonNode multiDocsNode = jsonNode.get("documents");
        int code = 0;
        if (jsonNode.get("code") != null) {
            code = jsonNode.get("code").asInt();
        }
        String msg = "";
        if (jsonNode.get("msg") != null) {
            msg = jsonNode.get("msg").asText();
        }
        String warning = "";
        if (jsonNode.get("warning") != null) {
            warning = jsonNode.get("warning").asText();
        }
        if (multiDocsNode == null) {
            return new HybridSearchRes(code, msg, warning, Collections.emptyList());
        }
        try {
            List<List<Document>> multiDosc = new ArrayList<>();
            Iterator<JsonNode> multiIter = multiDocsNode.elements();
            while (multiIter.hasNext()) {
                JsonNode docNode = multiIter.next();
                Iterator<JsonNode> iter = docNode.elements();
                List<Document> docs = new ArrayList<>();
                while (iter.hasNext()) {
                    JsonNode node = iter.next();
                    Document doc = nodeToDoc(node);
                    docs.add(doc);
                }
                multiDosc.add(docs);
            }
            HybridSearchRes searchRes = new HybridSearchRes(code, msg, warning);
            if (jsonNode.get("embeddingExtraInfo") != null){
                EmbeddingExtraInfo embeddingExtraInfo = new EmbeddingExtraInfo();
                embeddingExtraInfo.setTokenUsed(jsonNode.get("embeddingExtraInfo").get("tokenUsed").asLong());
                searchRes.setEmbeddingExtraInfo(embeddingExtraInfo);
            }
            if (!param.getSearch().getIsArrayParam()){
                searchRes.setDocuments(Collections.unmodifiableList(multiDosc.get(0)));
                return searchRes;
            }
            searchRes.setDocuments(Collections.unmodifiableList(multiDosc));
            return searchRes;
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format("VectorDBServer response " +
                    "from hybrid search error: can't parse documents=%s", multiDocsNode));
        }
    }

    /**
     * Delete documents from a collection based on specified criteria.
     */
    public AffectRes deleteDocument(DeleteParamInner param) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DOC_DELETE);
        JsonNode jsonNode = this.post(url, param.toString(), false);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * Update existing documents in a collection.
     */
    public AffectRes updateDocument(UpdateParamInner param, boolean ai) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DOC_UPDATE);
        JsonNode jsonNode = this.post(url, param.toString(), ai);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * Count the number of documents matching the query criteria.
     */
    public BaseRes countDocument(QueryCountParamInner param, boolean ai) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DOC_COUNT);
        JsonNode jsonNode = this.post(url, param.toString(), ai);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * Query AI documents from collection views.
     */
    public List<DocumentSet> queryAIDocument(CollectionViewQueryParamInner queryParamInner) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_DOCUMENT_QUERY);
        JsonNode jsonNode = this.post(url, queryParamInner.toString(), true);
        JsonNode docsNode = jsonNode.get("documentSets");
        List<DocumentSet> dosc = new ArrayList<>();
        if (docsNode == null) {
            return dosc;
        }
        try {
            Iterator<JsonNode> iterator = docsNode.elements();
            while (iterator.hasNext()) {
                JsonNode node = iterator.next();
                DocumentSet doc = nodeToDocumentSet(node);
                dosc.add(doc);
            }
            return dosc;
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format("VectorDBServer response " +
                    "from query error: can't parse documents=%s", docsNode));
        }
    }

    /**
     * Delete AI documents from collection views.
     */
    public AffectRes deleteAIDocument(CollectionViewDeleteParamInner deleteParamInner) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_DOCUMENT_DELETE);
        JsonNode jsonNode = this.post(url, deleteParamInner.toString(), true);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * Search AI documents with content-based queries.
     */
    public SearchContentRes searchAIDocument(SearchDocParamInner searchDocParamInner) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_DOCUMENT_SEARCH);
        JsonNode jsonNode = this.post(url, searchDocParamInner.toString(), true);
        JsonNode multiDocsNode = jsonNode.get("documents");
        int code = 0;
        if (jsonNode.get("code") != null) {
            code = jsonNode.get("code").asInt();
        }
        String msg = "";
        if (jsonNode.get("msg") != null) {
            msg = jsonNode.get("msg").asText();
        }
        String warning = "";
        if (jsonNode.get("warning") != null) {
            warning = jsonNode.get("warning").asText();
        }
        if (multiDocsNode == null) {
            return new SearchContentRes(code, msg, warning, Collections.emptyList());
        }
        try {
            List<SearchContentInfo> multiDosc = new ArrayList<>();
            Iterator<JsonNode> multiIter = multiDocsNode.elements();
            while (multiIter.hasNext()) {
                JsonNode docNode = multiIter.next();
                SearchContentInfo doc = nodeToSearchDoc(docNode);
                multiDosc.add(doc);
            }
            return new SearchContentRes(code, msg, warning, multiDosc);
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format("VectorDBServer response " +
                    "from search error: can't parse documents=%s", multiDocsNode));
        }
    }

    /**
     * Update AI documents in collection views.
     */
    public AffectRes updateAIDocument(CollectionViewUpdateParamInner updateParamInner) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_DOCUMENT_UPDATE);
        JsonNode jsonNode = this.post(url, updateParamInner.toString(), true);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * Perform full-text search on documents.
     */
    public FullTextSearchRes fullTextSearch(FullTextSearchParamInner param, boolean ai) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DOC_FULL_TEXT_SEARCH);
        JsonNode jsonNode = this.post(url, param.toString(), false);
        JsonNode documentsNode = jsonNode.get("documents");
        int code = 0;
        if (jsonNode.get("code") != null) {
            code = jsonNode.get("code").asInt();
        }
        String msg = "";
        if (jsonNode.get("msg") != null) {
            msg = jsonNode.get("msg").asText();
        }
        String warning = "";
        if (jsonNode.get("warning") != null) {
            warning = jsonNode.get("warning").asText();
        }
        if (documentsNode == null) {
            return new FullTextSearchRes(code, msg, warning, Collections.emptyList());
        }
        try {
            List<Document> multiDosc = new ArrayList<>();
            Iterator<JsonNode> multiIter = documentsNode.elements();
            while (multiIter.hasNext()) {
                JsonNode docNode = multiIter.next();
                Iterator<JsonNode> iter = docNode.elements();
                List<Document> docs = new ArrayList<>();
                while (iter.hasNext()) {
                    JsonNode node = iter.next();
                    Document doc = nodeToDoc(node);
                    docs.add(doc);
                }
                multiDosc.addAll(docs);
            }
            return new FullTextSearchRes(code, msg, warning, Collections.unmodifiableList(multiDosc));
        } catch (JsonProcessingException ex) {
            throw new VectorDBException(String.format("VectorDBServer response " +
                    "from full search search error: can't parse documents=%s", documentsNode));
        }
    }

    // Note: The following method is implemented in BaseHttpService but may be used here
    // private SearchContentResult nodeToSearchResult(JsonNode node) - already in base
}