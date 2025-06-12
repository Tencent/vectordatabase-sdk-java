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
import com.tencent.tcvectordb.model.param.collection.Embedding;
import com.tencent.tcvectordb.model.param.collection.FilterIndexConfig;
import com.tencent.tcvectordb.model.param.collection.IndexField;
import com.tencent.tcvectordb.model.param.collection.TTLConfig;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.*;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.service.Stub;
import com.tencent.tcvectordb.service.param.*;
import org.json.JSONObject;
import org.json.Property;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * VectorDB Collection
 *
 * <ol>
 * <li> database: the database name of collection </li>
 * <li> collection: name of collection, this name must be unique</li>
 * <li> replicaNum：the replica num of the collection, The number of replicas is limited by the instance type.
 *  eg: replicaNum must be zero if instance is free; the other instance's replicaNum must larger than zero </li>
 * <li> shardNum: the shard num of the collection, the data of the collection will be split into shardNum parts</li>
 * <li> description: description of the collection,</li>
 * <li> indexes: index field of the collection;
 *      field type could be string，uint64, array, vector and sparse vector.
 *      index type could be primaryKey, filter if the field is scalar field, id field must be primaryKey;
 *      index type could be FLAT, HNSW ... if the filed type is vector;
 *      index type should be inverted if the field type is sparse vector;
 *      metric type must be set if the field type is vector or sparse vector;
 *      fieldElementType could be string if the field type array;
 *      dimension must be set if the filed type is vector;
 *      </li>
 *
 * <li> alias: alias of the collection </li>
 * <li> embedding: embedding config should be set if collection use embedding function </li>
 * <li> TTLConfig: ttl config should be set if collection use ttl function </li>
 * </ol>
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Collection{
    @JsonIgnore
    private Stub stub;
    protected String database;
    protected String collection;
    protected int replicaNum = 2;
    protected int shardNum = 1;
    protected String description;
    protected List<IndexField> indexes;
    private String createTime;
    @JsonIgnore
    protected ReadConsistencyEnum readConsistency;
    private long documentCount;
    private Collection.IndexStatus indexStatus;
    private List<String> alias;
    protected Embedding embedding;

    protected TTLConfig ttlConfig;

    protected FilterIndexConfig filterIndexConfig;

    @JsonIgnore
    private String connectCollectionName;

    public void setConnectCollectionName(String connectCollectionName) {
        this.connectCollectionName = connectCollectionName;
    }
    public TTLConfig getTtlConfig() {
        return ttlConfig;
    }

    public void setTtlConfig(TTLConfig ttlConfig) {
        this.ttlConfig = ttlConfig;
    }

    public FilterIndexConfig getFilterIndexConfig() {
        return filterIndexConfig;
    }

    public void setFilterIndexConfig(FilterIndexConfig filterIndexConfig) {
        this.filterIndexConfig = filterIndexConfig;
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

    public String getCollection() {
        return collection;
    }

    public int getReplicaNum() {
        return replicaNum;
    }

    public int getShardNum() {
        return shardNum;
    }

    public String getDescription() {
        return description;
    }

    public List<IndexField> getIndexes() {
        return indexes;
    }

    public String getCreateTime() {
        return createTime;
    }

    public long getDocumentCount() {
        return documentCount;
    }

    public Collection.IndexStatus getIndexStatus() {
        return indexStatus;
    }

    public List<String> getAlias() {
        return alias;
    }

    public ReadConsistencyEnum getReadConsistency() {
        return readConsistency;
    }

    public void setReadConsistency(ReadConsistencyEnum readConsistency) {
        this.readConsistency = readConsistency;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public void setReplicaNum(int replicaNum) {
        this.replicaNum = replicaNum;
    }

    public void setShardNum(int shardNum) {
        this.shardNum = shardNum;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIndexes(List<IndexField> indexes) {
        this.indexes = indexes;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setDocumentCount(long documentCount) {
        this.documentCount = documentCount;
    }

    public void setIndexStatus(IndexStatus indexStatus) {
        this.indexStatus = indexStatus;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    public Embedding getEmbedding() {
        return embedding;
    }

    public void setEmbedding(Embedding embedding) {
        this.embedding = embedding;
    }


    /**
     * upsert document, upsert documents into the collection
     * @param param InsertParam: upsert data. buildIndex is whether to build index, default is true, documents
     *              is a list of JSONObject or Document.
     *              eg: Arrays.asList(
     *                new JSONObject("{\"id\":\"0013\",\"vector\":[0.2123, 0.21, 0.213],\"bookName\":\"三国演义\",\"author\":\"吴承恩\",\"page\":21,\"segment\":\"富贵功名，前缘分定，为人切莫欺心。\"}"),
     *                new JSONObject("{\"id\":\"0014\",\"vector\":[0.2123, 0.21, 0.213],\"bookName\":\"三国演义\",\"author\":\"吴承恩\",\"page\":21,\"segment\":\"富贵功名，前缘分定，为人切莫欺心。\"}")
     *              ); or
     *              Arrays.asList(Document.newBuilder()
     *                         .withId("0001")
     *                         .withVector(generateRandomVector(768))
     *                         .withSparseVector(sparseVectors.get(0))
     *                         .addDocField(new DocField("bookName", "三国演义"))
     *                         .addDocField(new DocField("author", "罗贯中"))
     *                         .addDocField(new DocField("page", 21))
     *                         .addDocField(new DocField("segment", "富贵功名，前缘分定，为人切莫欺心。"))
     *                         .addDocField(new DocField("text", "富贵功名，前缘分定，为人切莫欺心。"))
     *                         .build());
     *
     * @return AffectRes(affectedCount, msg, code)
     * @throws VectorDBException
     */
    public AffectRes upsert(InsertParam param) throws VectorDBException {
        String collection = this.collection;
        if (this.connectCollectionName != null){
            collection = this.connectCollectionName;
        }
        InsertParamInner insertParam = new InsertParamInner(
                database, collection, param);
        boolean ai = false;
        if((param.getDocuments().get(0)!=null)){
            if (param.getDocuments().get(0) instanceof Document){
                if (((Document)param.getDocuments().get(0)).getVector() instanceof String){
                    ai = true;
                }
            }
            if (param.getDocuments().get(0) instanceof JSONObject){
                if (((JSONObject)param.getDocuments().get(0)).get("vector") instanceof String){
                    ai = true;
                }
            }
        }
        return this.stub.upsertDocument(insertParam, ai);
    }

    /**
     * query document from collection
     * @param param QueryParam:
     *        limit(int): Limit return row's count
     *        offset(int): Skip offset rows of query result set
     *        retrieve_vector(bool): Whether to return vector values.
     *        filter(Filter): filter rows before return result
     *        document_ids(List): filter rows by id list
     *        output_fields(List): return columns by column name list
     * @return List<Document>
     * @throws VectorDBException
     */
    public List<Document> query(QueryParam param) throws VectorDBException {
        String collection = this.collection;
        if (this.connectCollectionName != null){
            collection = this.connectCollectionName;
        }
        boolean ai = false;
        return this.stub.queryDocument(
                new QueryParamInner(database, collection, param, this.readConsistency), ai);
    }

    /**
     * search document by vector from the collection
     * @param param SearchByVectorParam:
     *              vectors: List<List<Double>>, search documents by the vectors
     *              limit(int): Limit return row's count
     *              retrieve_vector(bool): Whether to return vector values.
     *              filter(Filter): filter rows before return result
     *              output_fields(List): return columns by column name list
     *              radius(Float): radius of search
     *              params(Params): params for search, eg:HNSWSearchParams, GeneralParams
     * @return List<List<Document>>: the size of the result is the same as the size of vectors
     * @throws VectorDBException
     */
    public List<List<Document>> search(SearchByVectorParam param) throws VectorDBException {
        String collection = this.collection;
        if (this.connectCollectionName != null){
            collection = this.connectCollectionName;
        }
        return this.stub.searchDocument(new SearchParamInner(
                database, collection, param, this.readConsistency), DataBaseTypeEnum.BASE).getDocuments();
    }

    /**
     * search document by documents represented by id from the collection
     * @param param SearchByVectorParam:
     *              documentIds: List<String>, search documents by the document ids
     *              limit(int): Limit return row's count
     *              retrieve_vector(bool): Whether to return vector values.
     *              filter(Filter): filter rows before return result
     *              output_fields(List): return columns by column name list
     *              radius(Float): radius of search
     *              params(Params): params for search, eg:HNSWSearchParams, GeneralParams
     * @return List<List<Document>>: the size of the result is the same as the size of documentIds
     * @throws VectorDBException
     */
    public List<List<Document>> searchById(SearchByIdParam param) throws VectorDBException {
        String collection = this.collection;
        if (this.connectCollectionName != null){
            collection = this.connectCollectionName;
        }
        return this.stub.searchDocument(new SearchParamInner(
                database, collection, param, this.readConsistency), DataBaseTypeEnum.BASE).getDocuments();
    }

    /**
     * search document by contents that would be embedded to vectors from the collection
     * @param param SearchByVectorParam:
     *              embeddingItems: List<String>, search documents by the content
     *              limit(int): Limit return row's count
     *              retrieve_vector(bool): Whether to return vector values.
     *              filter(Filter): filter rows before return result
     *              output_fields(List): return columns by column name list
     *              radius(Float): radius of search
     *              params(Params): params for search, eg:HNSWSearchParams, GeneralParams
     * @return List<List<Document>>: the size of the result is the same as the size of embeddingItems
     * @throws VectorDBException
     */
    public SearchRes searchByEmbeddingItems(SearchByEmbeddingItemsParam param) throws VectorDBException {
        String collection = this.collection;
        if (this.connectCollectionName != null){
            collection = this.connectCollectionName;
        }
        return this.stub.searchDocument(new SearchParamInner(
                database, collection, param, this.readConsistency), DataBaseTypeEnum.BASE);
    }

    /**
     * hybrid search document using vector and sparse vector from the collection
     * @param param HybridSearchParam:
     *      ann(List<AnnOption>): ann options, annOption used for vector search,
     *      match(List<MatchOption>): match options, matchOption used for sparse vector search
     *      retrieve_vector(bool): Whether to return vector and sparse vector values.
     *      filter(Filter): filter rows before return result
     *      document_ids(List): filter rows by id list
     *      output_fields(List): return columns by column name list
     *      Limit(int): limit the number of rows returned
     *      rerank(RerankParam): rerank param, RRFRerankParam or WeightRerankParam
     * @return HybridSearchRes: the size of the result is the same as the size of embeddingItems
     * @throws VectorDBException
     */
    public HybridSearchRes hybridSearch(HybridSearchParam param) throws VectorDBException {
        boolean ai = false;
        if(param.getAnn()!=null && !param.getAnn().isEmpty() && param.getAnn().get(0).getData()!=null
                && !param.getAnn().get(0).getData().isEmpty()
                && param.getAnn().get(0).getData().get(0) instanceof String){
            ai = true;
        }
        String collection = this.collection;
        if (this.connectCollectionName != null){
            collection = this.connectCollectionName;
        }
        return this.stub.hybridSearchDocument(new HybridSearchParamInner(
                database, collection, param, this.readConsistency), ai);
    }


    /**
     * delete document
     * @param param DeleteParam: delete document that retrieved by filter and documentIds
     * @return AffectRes
     * @throws VectorDBException
     */
    public AffectRes delete(DeleteParam param) throws VectorDBException {
        String collection = this.collection;
        if (this.connectCollectionName != null){
            collection = this.connectCollectionName;
        }
        return this.stub.deleteDocument(
                new DeleteParamInner(database, collection, param));
    }

    /**
     * update document
     * @param param: update param used for retrieving document
     * @param document(Document.class): the document to be updated
     * @return AffectRes
     * @throws VectorDBException
     */
    public AffectRes update(UpdateParam param, Document document) throws VectorDBException {
        String collection = this.collection;
        if (this.connectCollectionName != null){
            collection = this.connectCollectionName;
        }
        boolean ai = false;
        if (document.getVector() instanceof String){
            ai = true;
        }
        return this.stub.updateDocument(
                new UpdateParamInner(database, collection, param, document), ai);
    }

    /**
     * update document
     * @param param: update param used for retrieving document
     * @param document(JSONObject.class): the document to be updated
     * @return AffectRes
     * @throws VectorDBException
     */
    public AffectRes update(UpdateParam param, JSONObject document) throws VectorDBException {
        String collection = this.collection;
        if (this.connectCollectionName != null){
            collection = this.connectCollectionName;
        }
        boolean ai = false;
        if (document.get("vector") instanceof String){
            ai = true;
        }
        return this.stub.updateDocument(
                new UpdateParamInner(database, collection, param, document), ai);
    }

    /**
     * rebuild index
     * @param rebuildIndexParam: rebuild index param
     * @return BaseRes
     */
    public BaseRes rebuildIndex(RebuildIndexParam rebuildIndexParam) {
        String collection = this.collection;
        if (this.connectCollectionName != null){
            collection = this.connectCollectionName;
        }
        return this.stub.rebuildIndex(new RebuildIndexParamInner(database, collection, rebuildIndexParam));
    }

    /**
     * this method is deprecated, recommend use {@link Collection#addIndex(String, String, AddIndexParam)}
     * Used to add a scalar field index to an existing collection
     * (the scalar field may contain historical data or a newly added empty field)
     * @param database
     * @param collection
     * @param addIndexParam:
     * @return
     * @throws VectorDBException
     */
    @Deprecated
    public BaseRes AddIndex(String database, String collection, AddIndexParam addIndexParam) throws VectorDBException {
        return this.stub.addIndex(
                new AddIndexParamInner(database, collection, addIndexParam));
    }


    /**
     * Used to add a scalar field index to an existing collection
     * (the scalar field may contain historical data or a newly added empty field)
     * @param database
     * @param collection
     * @param addIndexParam:
     * @return
     * @throws VectorDBException
     */
    public BaseRes addIndex(String database, String collection, AddIndexParam addIndexParam) throws VectorDBException {
        return this.stub.addIndex(
                new AddIndexParamInner(database, collection, addIndexParam));
    }

    /**
     * Used to add a scalar field index to an existing collection
     * (the scalar field may contain historical data or a newly added empty field)
     * @param addIndexParam:
     * @return
     * @throws VectorDBException
     */
    public BaseRes addIndex(AddIndexParam addIndexParam) throws VectorDBException {
        String collection = this.collection;
        if (this.connectCollectionName != null){
            collection = this.connectCollectionName;
        }
        return this.stub.addIndex(
                new AddIndexParamInner(database, collection, addIndexParam));
    }

    /**
     * Used to query the number of documents that match the query, if countQueryParam is null,
     * return all rows number of the collection
     * @param countQueryParam:
     * @return
     * @throws VectorDBException
     */
    public BaseRes count(CountQueryParam countQueryParam) throws VectorDBException {
        String collection = this.collection;
        if (this.connectCollectionName != null){
            collection = this.connectCollectionName;
        }
        return this.stub.countDocument(
                new QueryCountParamInner(database, collection, countQueryParam, this.readConsistency), false);
    }

    /**
     * Currently, this method is only for dense vectors, i.e. vector
     * Supports re-specifying vector index parameters, HNSW supports re-specifying M and efConstruction, IVF supports re-specifying nlist (IVF_PQ supports re-specifying M and nlist)
     * Supports re-specifying similarity calculation method
     * The new configuration after the vector index is modified is defined by the field vectorIndexes
     * After adjusting the parameters, this interface will trigger a rebuild, and the rebuild rules are specified by the field rebuildRules
     * @param modifyVectorIndexParam
     * @return
     * @throws VectorDBException
     */
    public BaseRes modifyVectorIndex(ModifyVectorIndexParam modifyVectorIndexParam) throws VectorDBException {
        String collection = this.collection;
        if (this.connectCollectionName != null){
            collection = this.connectCollectionName;
        }
        return this.stub.modifyVectorIndex(
                new ModifyIndexParamInner(database, collection, modifyVectorIndexParam), false);
    }


    /**
     * full text search
     * @param param FullTextSearchParam:
     *      match(MatchOption): matchOption used for sparse vector search
     *      retrieve_vector(bool): Whether to return vector and sparse vector values.
     *      filter(Filter): filter rows before return result
     *      output_fields(List): return columns by column name list
     *      Limit(int): limit the number of rows returned
     * @return FullTextSearchRes:
     *      documents: List<Document>: the List of document
     *
     * @throws VectorDBException
     */
    public FullTextSearchRes fullTextSearch(FullTextSearchParam param) throws VectorDBException {
        boolean ai = false;
        String collection = this.collection;
        if (this.connectCollectionName != null){
            collection = this.connectCollectionName;
        }
        return this.stub.fullTextSearch(new FullTextSearchParamInner(
                database, collection, param, this.readConsistency), ai);
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

        public IndexStatus() {
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        public IndexStatus(String status, String startTime) {
            this.status = status;
            if (!startTime.isEmpty()){
                String formatPattern = "yyyy-MM-dd HH:mm:ss";

                SimpleDateFormat dateFormat = new SimpleDateFormat(formatPattern);
                try {
                    Date date = dateFormat.parse(startTime);
                    this.startTime = date;
                    System.out.println("Parsed Date: " + date);
                } catch (ParseException e) {
                    System.err.println("Failed to parse date: " + e.getMessage());
                }
            }
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
