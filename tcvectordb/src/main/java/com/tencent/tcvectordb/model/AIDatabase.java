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

/**
 * AIDatabase and about CollectionView operating.
 */
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
     * truncate documentSet data of collection view
     * @param collectionViewName: collection view name
     * @return AffectRes
     */
    public AffectRes truncateCollectionView(String collectionViewName) {
        return stub.truncateCollectionView(this.databaseName, collectionViewName, DataBaseTypeEnum.AI_DB);
    }

    /**
     * list collection view
     * @return List<CollectionView>: collection view list
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
     * create collection view
     * @param param: createCollectionView create collection view param:
     *             <ol>
     *             <li>name            : The name of the collection view.</>
     *             <li>description     : An optional description of the collection view.</>
     *             <li>embedding       : Args for embedding.</>
     *             <li>splitter_process: Args for splitter process</>
     *             <li>index           : A list of the index properties for the documents in a collection.</>
     *             <li>parsing_process : Args for parsing process</>
     *             <li>expected_file_num: Expected total number of documents</>
     *             <li>average_file_size: Estimate the average document size</>
     *             </>
     * @return CollectionView
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
     * describe collection view info
     * @param collectionView: collection view name
     * @return CollectionView
     * @throws VectorDBException
     */
    public CollectionView describeCollectionView(String collectionView) throws VectorDBException {
        CollectionView collection = stub.describeCollectionView(this.databaseName, collectionView);
        collection.setStub(stub);
        collection.setReadConsistency(readConsistency);
        return collection;
    }

    /**
     * drop collection view
     * @param collectionViewName: collection view name
     * @return AffectRes
     * @throws VectorDBException
     */
    public AffectRes dropCollectionView(String collectionViewName) throws VectorDBException {
        return stub.dropCollectionView(this.databaseName, collectionViewName);
    }

    /**
     * set alias for collection view
     * @param collectionViewName: collection view name
     * @param aliasName: alias name to set
     * @return AffectRes
     */
    public AffectRes setAIAlias(String collectionViewName, String aliasName) {
        return stub.setAIAlias(this.databaseName, collectionViewName, aliasName);
    }

    /**
     * delete alias for collection view
     * @param aliasName: alias name to delete
     * @return AffectRes
     */
    public AffectRes deleteAIAlias(String aliasName) {
        return stub.deleteAIAlias(this.databaseName, aliasName);
    }


    @Override
    public String toString() {
        return String.format("{\"database\":\"%s\"}", this.databaseName);
    }
}
