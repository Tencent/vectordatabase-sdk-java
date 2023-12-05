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

    public AffectRes truncateCollectionView(String collectionName) {
        return stub.truncateCollectionView(this.databaseName, collectionName, DataBaseTypeEnum.AI_DB);
    }

    public List<CollectionView> listCollectionView() throws VectorDBException {
        List<CollectionView> collections = stub.listCollectionView(this.databaseName);
        collections.forEach(c -> {
            c.setStub(stub);
            c.setReadConsistency(readConsistency);
        });
        return collections;
    }

    public CollectionView createCollectionView(CreateCollectionViewParam param) throws VectorDBException {
        param.setDatabase(databaseName);
        param.setReadConsistency(readConsistency);
        stub.createCollectionView(param);
        param.setStub(this.stub);
        return param;
    }

    public CollectionView describeCollectionView(String collectionName) throws VectorDBException {
        CollectionView collection = stub.describeCollectionView(this.databaseName, collectionName);
        collection.setStub(stub);
        collection.setReadConsistency(readConsistency);
        return collection;
    }

    public AffectRes dropCollectionView(String collectionName) throws VectorDBException {
        return stub.dropCollectionView(this.databaseName, collectionName);
    }

    public AffectRes setAIAlias(String collectionName, String aliasName) {
        return stub.setAIAlias(this.databaseName, collectionName, aliasName);
    }

    public AffectRes deleteAIAlias(String aliasName) {
        return stub.deleteAIAlias(this.databaseName, aliasName);
    }


    @Override
    public String toString() {
        return String.format("{\"database\":\"%s\"}", this.databaseName);
    }
}
