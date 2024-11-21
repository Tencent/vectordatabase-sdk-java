package com.tencent.tcvectordb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.param.collectionView.CreateCollectionViewParam;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.entity.DataBaseType;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.service.Stub;

import java.util.List;
import java.util.Map;

public class AIDatabase {
    private final Stub stub;
    private final String databaseName;
    @JsonIgnore
    private final ReadConsistencyEnum readConsistency;

    public AIDatabase(Stub stub, String databaseName, ReadConsistencyEnum readConsistency) {
        this.stub = stub;
        this.databaseName = databaseName;
        this.readConsistency = readConsistency;
//        ensureDataBaseType();
    }

    private void ensureDataBaseType() throws VectorDBException {
        Map<String, DataBaseType> dataBaseTypeMap = stub.listDatabaseInfos();
        if (!dataBaseTypeMap.containsKey(this.databaseName)){
            throw new VectorDBException("database not existed");
        }
        if (!DataBaseTypeEnum.isAIDataBase(DataBaseTypeEnum.valueOf(dataBaseTypeMap.get(this.databaseName).getDbType()))){
            throw new VectorDBException("database is not ai database");
        }
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public ReadConsistencyEnum getReadConsistency() {
        return readConsistency;
    }

    /**
     * clear the document of the collectionView
     * @param collectionViewName: name of the collectionView
     * @return
     */
    public AffectRes truncateCollectionView(String collectionViewName) {
        return stub.truncateCollectionView(this.databaseName, collectionViewName, DataBaseTypeEnum.AI_DB);
    }

    /**
     * Get collection view list.
     * @return List<CollectionView>
     * @throws VectorDBException
     */
    public List<CollectionView> listCollectionView() throws VectorDBException {
        List<CollectionView> collections = stub.listCollectionView(this.databaseName);
        collections.forEach(c -> {
            c.setStub(stub);
            c.setReadConsistency(readConsistency);
        });
        return collections;
    }

    /**
     * create a collection view.
     * @param param collectionView : The name of the collection view.
     *             description     : An optional description of the collection view.
     *             embedding       : Args for embedding.
     *             splitterPreprocess: Args for splitter process
     *             index           : A list of the index properties for the documents in a collection.
     *             expectedFileNum: Expected total number of documents
     *             averageFileSize: Estimate the average document size
     *             shard            : The shard number of the collection.
     *                                Shard will divide a large dataset into smaller subsets.
     *             replicas         : The replicas number of the collection.
     *                                Replicas refers to the number of identical copies of each primary shard,
     *                                used for disaster recovery and load balancing.
     *             parsingProcess  : Document parsing parameters
     * @return
     * @throws VectorDBException
     */
    public CollectionView createCollectionView(CreateCollectionViewParam param) throws VectorDBException {
        param.setDatabase(databaseName);
        param.setReadConsistency(readConsistency);
        stub.createCollectionView(param);
        param.setStub(this.stub);
        return param;
    }

    /**
     * Get a CollectionView by name.
     * @param collectionView
     * @return
     * @throws VectorDBException
     */
    public CollectionView describeCollectionView(String collectionView) throws VectorDBException {
        CollectionView collection = stub.describeCollectionView(this.databaseName, collectionView);
        collection.setStub(stub);
        collection.setReadConsistency(readConsistency);
        return collection;
    }

    /**
     * drop the CollectionView
     * @param collectionViewName: name of the CollectionView
     * @return
     * @throws VectorDBException
     */
    public AffectRes dropCollectionView(String collectionViewName) throws VectorDBException {
        return stub.dropCollectionView(this.databaseName, collectionViewName);
    }

    /**
     * Set alias for collection view.
     * @param collectionViewName:The name of the collection_view.
     * @param aliasName:alias name to set
     * @return AffectRes: contains affectedCount
     */
    public AffectRes setAIAlias(String collectionViewName, String aliasName) {
        return stub.setAIAlias(this.databaseName, collectionViewName, aliasName);
    }

    /**
     * delete alias for collection view.
     * @param aliasName:alias name to delete
     * @return AffectRes: contains affectedCount
     */
    public AffectRes deleteAIAlias(String aliasName) {
        return stub.deleteAIAlias(this.databaseName, aliasName);
    }


    @Override
    public String toString() {
        return String.format("{\"database\":\"%s\"}", this.databaseName);
    }
}
