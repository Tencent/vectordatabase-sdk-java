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
package com.tencent.tcvectordb.service.impl.grpc;

import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.*;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.*;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.rpc.Interceptor.BackendServiceInterceptor;
import com.tencent.tcvectordb.rpc.proto.Olama;
import com.tencent.tcvectordb.rpc.proto.SearchEngineGrpc;
import com.tencent.tcvectordb.service.ApiPath;
import com.tencent.tcvectordb.service.param.*;
import io.grpc.ManagedChannel;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * gRPC service implementation for document operations.
 */
public class DocumentGrpcService extends BaseGrpcService {

    /**
     * Insert or update documents in a collection.
     */
    public AffectRes upsertDocument(InsertParamInner param, boolean ai) {
        Olama.UpsertRequest.Builder builder = Olama.UpsertRequest.newBuilder();
        if (param.getDatabase()!=null){
            builder.setDatabase(param.getDatabase());
        }
        if (param.getCollection()!=null){
            builder.setCollection(param.getCollection());
        }
        if (param.getBuildIndex()==null){
            builder.setBuildIndex(true);
        }else {
            builder.setBuildIndex(param.getBuildIndex());
        }

        if (param.getDocuments()!=null && !param.getDocuments().isEmpty()){
            for (Object document : param.getDocuments()) {
                if (document instanceof Document){
                    Olama.Document doc= convertDocument2OlamaDoc((Document) document);
                    builder.addDocuments(doc);
                }else if (document instanceof JSONObject){
                    Olama.Document doc= convertDocumentJSON2OlamaDoc((JSONObject) document);
                    builder.addDocuments(doc);

                }else {
                    throw new VectorDBException("upsert failed, because of incorrect documents type, " +
                            "which must be []Document or []JSONObject");
                }
            }
        }
        logQuery(ApiPath.DOC_UPSERT, builder);
        ManagedChannel channel = channelPool.getChannel();
        Olama.UpsertResponse response;
        response = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                .withInterceptors(new BackendServiceInterceptor(ai))
                .upsert(builder.build());

        logResponse(ApiPath.DOC_UPSERT, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer upsert data error: not Successful, code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        AffectRes affectRes = new AffectRes(response.getCode(), response.getMsg(), response.getWarning(), response.getAffectedCount());
        if (response.hasEmbeddingExtraInfo()){
            EmbeddingExtraInfo embeddingExtraInfo = new EmbeddingExtraInfo();
            embeddingExtraInfo.setTokenUsed(response.getEmbeddingExtraInfo().getTokenUsed());
            affectRes.setEmbeddingExtraInfo(embeddingExtraInfo);
        }
        return affectRes;
    }

    /**
     * Query documents from a collection based on specified criteria.
     */
    public List<Document> queryDocument(QueryParamInner param, boolean ai) {
        Olama.QueryRequest.Builder queryBuilder = Olama.QueryRequest.newBuilder()
                .setReadConsistency(param.getReadConsistency().getReadConsistency());
        if (param.getDatabase()!=null){
            queryBuilder.setDatabase(param.getDatabase());
        }
        if (param.getCollection()!=null){
            queryBuilder.setCollection(param.getCollection());
        }
        QueryParam queryParam = param.getQuery();
        if (queryParam==null){
            throw new VectorDBException("VectorDBServer error: query param is null");
        }
        Olama.QueryCond.Builder queryCondBuilder = Olama.QueryCond.newBuilder().setRetrieveVector(queryParam.isRetrieveVector())
                .setLimit(queryParam.getLimit()).setOffset(queryParam.getOffset());
        if(queryParam.getDocumentIds()!=null){
            queryCondBuilder.addAllDocumentIds(queryParam.getDocumentIds());
        }
        if(queryParam.getOutputFields()!=null){
            queryCondBuilder.addAllOutputFields(queryParam.getOutputFields());
        }
        if (queryParam.getFilter()!=null){
            queryCondBuilder.setFilter(queryParam.getFilter());
        }
        if (queryParam.getSort()!=null && queryParam.getSort().size()>0){
            queryCondBuilder.addAllSort(queryParam.getSort().stream().map(sort -> {
                return Olama.OrderRule.newBuilder().setFieldName(sort.getFieldName()).setDesc(sort.getDirection().equals("desc")).build();
            }).collect(Collectors.toList()));
        }
        queryBuilder.setQuery(queryCondBuilder.build());
        logQuery(ApiPath.DOC_QUERY, queryBuilder);
        ManagedChannel channel = channelPool.getChannel();
        Olama.QueryResponse queryResponse = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                    .withInterceptors(new BackendServiceInterceptor(ai))
                    .query(queryBuilder.build());

        logResponse(ApiPath.DOC_QUERY, queryResponse);
        if(queryResponse==null){
            throw new VectorDBException("VectorDBServer error: query not response");
        }
        if (queryResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: query not Success, body code=%s, message=%s",
                    queryResponse.getCode(), queryResponse.getMsg()));
        }

        List<Olama.Document> documentsList = queryResponse.getDocumentsList();

        return documentsList.stream().map(document -> {
            return convertDocument(document);
        }).collect(Collectors.toList());
    }

    /**
     * Search documents using vector similarity.
     */
    public SearchRes searchDocument(SearchParamInner param, DataBaseTypeEnum dbType) {
        Olama.SearchRequest.Builder builder = Olama.SearchRequest.newBuilder().
                setReadConsistency(param.getReadConsistency().getReadConsistency());
        if (param.getDatabase()!=null){
            builder.setDatabase(param.getDatabase());
        }
        if (param.getCollection()!=null){
            builder.setCollection(param.getCollection());
        }
        SearchParam searchParam = param.getSearch();
        Olama.SearchCond.Builder searchConBuilder = Olama.SearchCond.newBuilder()
                .setRetrieveVector(searchParam.isRetrieveVector()).setLimit(searchParam.getLimit());
        if (searchParam.getOutputFields()!=null){
            searchConBuilder.addAllOutputfields(searchParam.getOutputFields());
        }
        if (searchParam.getFilter()!=null){
            searchConBuilder.setFilter(searchParam.getFilter());
        }
        if (searchParam instanceof SearchByVectorParam){
            ((SearchByVectorParam)searchParam).getVectors().forEach(vector->{
                Olama.VectorArray.Builder vectorArrayBuilder =  Olama.VectorArray.newBuilder();
                vector.forEach(ele->vectorArrayBuilder.addVector(Float.valueOf(ele.toString())));
                searchConBuilder.addVectors(vectorArrayBuilder.build());
            });

        }
        if (searchParam instanceof SearchByIdParam){
            searchConBuilder.addAllDocumentIds(((SearchByIdParam)searchParam).getDocumentIds());
        }
        if (searchParam instanceof SearchByEmbeddingItemsParam){
            searchConBuilder.addAllEmbeddingItems(((SearchByEmbeddingItemsParam)searchParam).getEmbeddingItems());
        }

        if (searchParam.getParams()!=null){
            if (searchParam.getParams() instanceof GeneralParams){
                GeneralParams params = (GeneralParams) searchParam.getParams();
                searchConBuilder.setParams(Olama.SearchParams.newBuilder().setEf(params.getEf())
                        .setNprobe(params.getNProbe())
                        .setRadius((float) params.getRadius()).build());
            }else if (searchParam.getParams() instanceof HNSWSearchParams){
                HNSWSearchParams params = (HNSWSearchParams) searchParam.getParams();
                searchConBuilder.setParams(Olama.SearchParams.newBuilder().setEf(params.getEf()).build());
            }
        }

        if (searchParam.getRadius()!=null){
            searchConBuilder.setRange(true);
            searchConBuilder.getParamsBuilder().setRadius(searchParam.getRadius());
        }
        builder.setSearch(searchConBuilder.build());
        logQuery(ApiPath.DOC_SEARCH, builder);
        ManagedChannel channel = channelPool.getChannel();
        Olama.SearchResponse searchResponse = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                    .search(builder.build());

        logResponse(ApiPath.DOC_SEARCH, searchResponse);
        if(searchResponse==null){
            throw new VectorDBException("VectorDBServer error: search not response");
        }
        if (searchResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: search not Success, body code=%s, message=%s",
                    searchResponse.getCode(), searchResponse.getMsg()));
        }
        List<List<Document>> documentsList = new ArrayList<>();
        for (Olama.SearchResult searchResult : searchResponse.getResultsList()) {
            documentsList.add(searchResult.getDocumentsList().stream().map(this::convertDocument)
                    .collect(Collectors.toList()));
        }

        SearchRes searchRes = new SearchRes(searchResponse.getCode(),searchResponse.getMsg(), searchResponse.getWarning(), documentsList);
        if (searchResponse.getEmbeddingExtraInfo()!=null){
            searchRes.setEmbeddingExtraInfo(new EmbeddingExtraInfo(searchResponse.getEmbeddingExtraInfo().getTokenUsed()));
        }
        return searchRes;
    }

    /**
     * Perform hybrid search combining vector and text search.
     */
    public HybridSearchRes hybridSearchDocument(HybridSearchParamInner param, boolean ai) {
        Olama.SearchRequest.Builder builder = Olama.SearchRequest.newBuilder().
                setReadConsistency(param.getReadConsistency().getReadConsistency());
        if (param.getDatabase()!=null){
            builder.setDatabase(param.getDatabase());
        }
        if (param.getCollection()!=null){
            builder.setCollection(param.getCollection());
        }
        HybridSearchParam searchParam = param.getSearch();
        Olama.SearchCond.Builder searchConBuilder = Olama.SearchCond.newBuilder()
                .setRetrieveVector(searchParam.isRetrieveVector()).setLimit(searchParam.getLimit());
        if (searchParam.getOutputFields()!=null){
            searchConBuilder.addAllOutputfields(searchParam.getOutputFields());
        }
        if (searchParam.getFilter()!=null){
            searchConBuilder.setFilter(searchParam.getFilter());
        }
        if (searchParam.getAnn()!=null && !searchParam.getAnn().isEmpty()){
            searchConBuilder.addAllAnn(searchParam.getAnn().stream()
                    .map(annOption -> {
                        Olama.AnnData.Builder annBuilder = Olama.AnnData.newBuilder()
                                .setFieldName(annOption.getFieldName());
                        if (annOption.getDocumentIds()!=null){
                            annBuilder.addAllDocumentIds(annOption.getDocumentIds());
                        }
                        if (annOption.getData()!=null){
                            if (annOption.getData().get(0) instanceof String){
                                annBuilder.addAllEmbeddingItems(annOption.getData().stream().map(item->(String)item).collect(Collectors.toList()));
                            }if (annOption.getData().get(0) instanceof List){
                                annBuilder.addAllData(annOption.getData().stream()
                                        .map(item-> Olama.VectorArray.newBuilder().addAllVector(((List<Object>)item).
                                                stream().map(ele->Float.parseFloat(ele.toString())).collect(Collectors.toList())).build())
                                        .collect(Collectors.toList()));
                            }
                        }
                        if (annOption.getLimit()!=null){
                            annBuilder.setLimit(annOption.getLimit());
                        }
                        if (annOption.getParams()!=null){
                            if (annOption.getParams() instanceof GeneralParams){
                                GeneralParams params = (GeneralParams) annOption.getParams();
                                annBuilder.setParams(Olama.SearchParams.newBuilder().setEf(params.getEf())
                                        .setNprobe(params.getNProbe())
                                        .setRadius((float) params.getRadius()).build());
                            }else if (annOption.getParams() instanceof HNSWSearchParams){
                                HNSWSearchParams params = (HNSWSearchParams) annOption.getParams();
                                annBuilder.setParams(Olama.SearchParams.newBuilder().setEf(params.getEf()).build());
                            }
                        }
                        return annBuilder.build();
                    }).collect(Collectors.toList()));
        }
        if (searchParam.getMatch()!=null && !searchParam.getMatch().isEmpty()){
            searchConBuilder.addAllSparse(searchParam.getMatch().stream().map(matchOption -> {
                Olama.SparseData.Builder sparseBuilder = Olama.SparseData.newBuilder().setFieldName(matchOption.getFieldName());
                matchOption.getData().forEach(sparseVectors->{
                    sparseBuilder.addData(Olama.SparseVectorArray.newBuilder().addAllSpVector(sparseVectors.stream()
                            .map(vectors-> Olama.SparseVecItem.newBuilder().setTermId((Long) vectors.get(0)).
                                    setScore((Float.parseFloat(vectors.get(1).toString()))).
                                    build()).collect(Collectors.toList())).build());
                });
                if(matchOption.getLimit()!=null){
                    sparseBuilder.setLimit(matchOption.getLimit());
                }

                if (matchOption.getCutoffFrequency()!=null || matchOption.getTerminateAfter()!=null){
                    Olama.SparseSearchParams.Builder sparseSearchParamsBuilder = Olama.SparseSearchParams.newBuilder();
                    if (matchOption.getCutoffFrequency()!=null){
                        sparseSearchParamsBuilder.setCutoffFrequency(matchOption.getCutoffFrequency());
                    }
                    if (matchOption.getTerminateAfter()!=null){
                        sparseSearchParamsBuilder.setTerminateAfter(matchOption.getTerminateAfter());
                    }
                    sparseBuilder.setParams(sparseSearchParamsBuilder.build()).build();
                }
                return sparseBuilder.build();
            }).collect(Collectors.toList()));
        }
        if (searchParam.getRerank()!=null){
            Olama.RerankParams.Builder rerankBuilder = Olama.RerankParams.newBuilder()
                    .setMethod(searchParam.getRerank().getMethod());
            if (searchParam.getRerank() instanceof WeightRerankParam){
                WeightRerankParam weightRerankParam = (WeightRerankParam)searchParam.getRerank();
                Map<String, Float> weightMap = new HashMap<>();
                for (int i = 0; i < weightRerankParam.getFieldList().size(); i++) {
                    weightMap.put(weightRerankParam.getFieldList().get(i), weightRerankParam.getWeight().get(i).floatValue());
                }
                rerankBuilder.putAllWeights(weightMap);
            }else if (searchParam.getRerank() instanceof RRFRerankParam){
                RRFRerankParam rrfRerankParam = (RRFRerankParam)searchParam.getRerank();
                rerankBuilder.setRrfK(rrfRerankParam.getRrfK());
            }
            searchConBuilder.setRerankParams(rerankBuilder.build());
        }
        builder.setSearch(searchConBuilder);
        logQuery(ApiPath.DOC_HYBRID_SEARCH, builder);
        ManagedChannel channel = channelPool.getChannel();
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = SearchEngineGrpc.newBlockingStub(channel).withInterceptors(new BackendServiceInterceptor(ai));
        Olama.SearchResponse searchResponse = searchEngineBlockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).hybridSearch(builder.build());

        logResponse(ApiPath.DOC_HYBRID_SEARCH, searchResponse);

        if(searchResponse==null){
            throw new VectorDBException("VectorDBServer error: search not response");
        }
        if (searchResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: search not Success, body code=%s, message=%s",
                    searchResponse.getCode(), searchResponse.getMsg()));
        }
        HybridSearchRes hybridSearchRes = new HybridSearchRes(searchResponse.getCode(),searchResponse.getMsg(), searchResponse.getWarning());
        if (searchResponse.getEmbeddingExtraInfo()!=null){
            EmbeddingExtraInfo embeddingExtraInfo = new EmbeddingExtraInfo();
            embeddingExtraInfo.setTokenUsed(searchResponse.getEmbeddingExtraInfo().getTokenUsed());
            hybridSearchRes.setEmbeddingExtraInfo(embeddingExtraInfo);
        }

        List<List<Document>> documentsList = new ArrayList<>();
        for (Olama.SearchResult searchResult : searchResponse.getResultsList()) {
            List<Document> documents = searchResult.getDocumentsList().stream().map(this::convertDocument)
                    .collect(Collectors.toList());
            if (!searchParam.getIsArrayParam()){
                hybridSearchRes.setDocuments(Collections.unmodifiableList(documents));
                return  hybridSearchRes;
            }else {
                documentsList.add(documents);
            }
        }
        hybridSearchRes.setDocuments(Collections.unmodifiableList(documentsList));
        return  hybridSearchRes;
    }

    /**
     * Delete documents from a collection based on specified criteria.
     */
    public AffectRes deleteDocument(DeleteParamInner param) {
        Olama.QueryCond.Builder queryCondBuilder = Olama.QueryCond.newBuilder();
        DeleteParam paramQuery = param.getQuery();
        if(paramQuery.getDocumentIds()!=null && !paramQuery.getDocumentIds().isEmpty()){
            queryCondBuilder.addAllDocumentIds(paramQuery.getDocumentIds());
        }
        if (paramQuery.getFilter()!=null && !paramQuery.getFilter().isEmpty()){
            queryCondBuilder.setFilter(paramQuery.getFilter());
        }
        if(param.getQuery().getLimit()!=null){
            queryCondBuilder.setLimit(param.getQuery().getLimit());
        }
        Olama.DeleteRequest.Builder deleteRequestBuilder = Olama.DeleteRequest.newBuilder();
        if (param.getDatabase()!=null){
            deleteRequestBuilder.setDatabase(param.getDatabase());
        }
        if (param.getCollection()!=null){
            deleteRequestBuilder.setCollection(param.getCollection());
        }

        Olama.DeleteRequest deleteRequest = deleteRequestBuilder.setQuery(queryCondBuilder.build()).build();
        logQuery(ApiPath.DOC_DELETE, deleteRequest);
        ManagedChannel channel = channelPool.getChannel();
        Olama.DeleteResponse deleteResponse = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS).dele(deleteRequest);

        logResponse(ApiPath.DOC_DELETE, deleteResponse);
        if(deleteResponse==null){
            throw new VectorDBException("VectorDBServer error: deleteResponse not response");
        }
        if (deleteResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: delete not Success, body code=%s, message=%s",
                    deleteResponse.getCode(), deleteResponse.getMsg()));
        }
        return new AffectRes(deleteResponse.getCode(), deleteResponse.getMsg(),"",deleteResponse.getAffectedCount());
    }

    /**
     * Update existing documents in a collection.
     */
    public AffectRes updateDocument(UpdateParamInner param, boolean ai) {
        Olama.QueryCond.Builder queryCondBuilder = Olama.QueryCond.newBuilder();
        UpdateParam paramQuery = param.getQuery();
        if(paramQuery.getDocumentIds()!=null && !paramQuery.getDocumentIds().isEmpty()){
            queryCondBuilder.addAllDocumentIds(paramQuery.getDocumentIds());
        }
        if (paramQuery.getFilter()!=null && !paramQuery.getFilter().isEmpty()){
            queryCondBuilder.setFilter(paramQuery.getFilter());
        }

        Olama.UpdateRequest.Builder updateRequestBuilder = Olama.UpdateRequest.newBuilder();
        if (param.getDatabase()!=null) {
            updateRequestBuilder.setDatabase(param.getDatabase());
        }
        if (param.getCollection()!=null) {
            updateRequestBuilder.setCollection(param.getCollection());
        }
        if (param.getUpdate()!=null){
            updateRequestBuilder.setQuery(queryCondBuilder.build())
                    .setUpdate(convertDocument2OlamaDoc(param.getUpdate()));

        } else if (param.getUpdateData()!=null) {
            updateRequestBuilder.setQuery(queryCondBuilder.build())
                    .setUpdate(convertDocumentJSON2OlamaDoc(param.getUpdateData()));
        }
        logQuery(ApiPath.DOC_UPDATE, updateRequestBuilder.build());
        ManagedChannel channel = channelPool.getChannel();
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = SearchEngineGrpc.newBlockingStub(channel).withInterceptors(new BackendServiceInterceptor(ai));
        Olama.UpdateResponse  updateResponse = searchEngineBlockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS)
                    .update(updateRequestBuilder.build());


        logResponse(ApiPath.DOC_UPDATE, updateResponse);
        if(updateResponse==null){
            throw new VectorDBException("VectorDBServer error: update not response");
        }
        if (updateResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: update not Success, body code=%s, message=%s",
                    updateResponse.getCode(), updateResponse.getMsg()));
        }
        return new AffectRes(updateResponse.getCode(), updateResponse.getMsg(),updateResponse.getWarning(),updateResponse.getAffectedCount());
    }

    /**
     * Count the number of documents matching the query criteria.
     */
    public BaseRes countDocument(QueryCountParamInner param, boolean ai) {
        Olama.CountRequest.Builder countCondBuilder = Olama.CountRequest.newBuilder();
        if(param.getDatabase()!=null){
            countCondBuilder.setDatabase(param.getDatabase());
        }
        if(param.getCollection()!=null){
            countCondBuilder.setCollection(param.getCollection());
        }
        if (param.getQuery()!=null) {
            Olama.QueryCond.Builder queryBuilder =  Olama.QueryCond.newBuilder();
            if (param.getQuery().getFilter()!=null){
                queryBuilder.setFilter(param.getQuery().getFilter());
            }
            countCondBuilder.setQuery(queryBuilder.build());
        }
        if (param.getReadConsistency()!=null){
             countCondBuilder.setReadConsistency(param.getReadConsistency().getReadConsistency());
        }
        Olama.CountRequest countRequest = countCondBuilder.build();
        logQuery(ApiPath.DOC_COUNT, countCondBuilder);
        ManagedChannel channel = channelPool.getChannel();
        Olama.CountResponse countResponse = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS).count(countRequest);

        logResponse(ApiPath.DOC_COUNT, countResponse);
        if(countResponse==null){
            throw new VectorDBException("VectorDBServer error: count not response");
        }
        if (countResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: count not Success, body code=%s, message=%s",
                    countResponse.getCode(), countResponse.getMsg()));
        }
        return new BaseRes(countResponse.getCode(), countResponse.getMsg(),"", countResponse.getCount());
    }

    /**
     * Query AI documents from collection views.
     * Note: AI document operations are not supported via gRPC and must use HTTP fallback.
     */
    public List<DocumentSet> queryAIDocument(CollectionViewQueryParamInner queryParamInner) {
        throw new UnsupportedOperationException("AI document operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * Delete AI documents from collection views.
     * Note: AI document operations are not supported via gRPC and must use HTTP fallback.
     */
    public AffectRes deleteAIDocument(CollectionViewDeleteParamInner deleteParamInner) {
        throw new UnsupportedOperationException("AI document operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * Search AI documents with content-based queries.
     * Note: AI document operations are not supported via gRPC and must use HTTP fallback.
     */
    public SearchContentRes searchAIDocument(SearchDocParamInner searchDocParamInner) {
        throw new UnsupportedOperationException("AI document operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * Update AI documents in collection views.
     * Note: AI document operations are not supported via gRPC and must use HTTP fallback.
     */
    public AffectRes updateAIDocument(CollectionViewUpdateParamInner updateParamInner) {
        throw new UnsupportedOperationException("AI document operations not supported via gRPC, use HTTP fallback");
    }

    /**
     * Perform full-text search on documents.
     */
    public FullTextSearchRes fullTextSearch(FullTextSearchParamInner param, boolean ai) {
        Olama.SearchRequest.Builder builder = Olama.SearchRequest.newBuilder().
                setReadConsistency(param.getReadConsistency().getReadConsistency());
        if (param.getDatabase()!=null){
            builder.setDatabase(param.getDatabase());
        }
        if (param.getCollection()!=null){
            builder.setCollection(param.getCollection());
        }
        FullTextSearchParam searchParam = param.getSearch();
        Olama.SearchCond.Builder searchConBuilder = Olama.SearchCond.newBuilder()
                .setRetrieveVector(searchParam.isRetrieveVector()).setLimit(searchParam.getLimit());
        if (searchParam.getOutputFields()!=null){
            searchConBuilder.addAllOutputfields(searchParam.getOutputFields());
        }
        if (searchParam.getFilter()!=null){
            searchConBuilder.setFilter(searchParam.getFilter());
        }

        if (searchParam.getMatch()!=null){
            MatchParam matchOption = searchParam.getMatch();
            Olama.SparseData.Builder sparseBuilder = Olama.SparseData.newBuilder().setFieldName(matchOption.getFieldName());
            matchOption.getData().forEach(sparseVectors->{
                sparseBuilder.addData(Olama.SparseVectorArray.newBuilder().addAllSpVector(sparseVectors.stream()
                        .map(vectors-> Olama.SparseVecItem.newBuilder().setTermId((Long) vectors.get(0)).
                                setScore((Float.parseFloat(vectors.get(1).toString()))).
                                build()).collect(Collectors.toList())).build());
            });

            if (matchOption.getCutoffFrequency()!=null || matchOption.getTerminateAfter()!=null){
                Olama.SparseSearchParams.Builder sparseSearchParamsBuilder = Olama.SparseSearchParams.newBuilder();
                if (matchOption.getCutoffFrequency()!=null){
                    sparseSearchParamsBuilder.setCutoffFrequency(matchOption.getCutoffFrequency());
                }
                if (matchOption.getTerminateAfter()!=null){
                    sparseSearchParamsBuilder.setTerminateAfter(matchOption.getTerminateAfter());
                }
                sparseBuilder.setParams(sparseSearchParamsBuilder.build()).build();
            }
            searchConBuilder.addSparse(sparseBuilder.build());
        }
        builder.setSearch(searchConBuilder.build());
        logQuery(ApiPath.DOC_FULL_TEXT_SEARCH, builder);
        ManagedChannel channel = channelPool.getChannel();
        SearchEngineGrpc.SearchEngineBlockingStub searchEngineBlockingStub = SearchEngineGrpc.newBlockingStub(channel).withInterceptors(new BackendServiceInterceptor(ai));
        Olama.SearchResponse searchResponse = searchEngineBlockingStub.withDeadlineAfter(this.timeout, TimeUnit.SECONDS).fullTextSearch(builder.build());

        logResponse(ApiPath.DOC_FULL_TEXT_SEARCH, searchResponse);
        if(searchResponse==null){
            throw new VectorDBException("VectorDBServer error: full text search not response");
        }
        if (searchResponse.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: full text search not Success, body code=%s, message=%s",
                    searchResponse.getCode(), searchResponse.getMsg()));
        }
        List<Document> documents = new ArrayList<>();
        for (Olama.SearchResult searchResult : searchResponse.getResultsList()) {
            documents.addAll(searchResult.getDocumentsList().stream().map(this::convertDocument)
                    .collect(Collectors.toList()));
        }
        return new FullTextSearchRes(searchResponse.getCode(),searchResponse.getMsg(), searchResponse.getWarning(), documents);
    }
}
