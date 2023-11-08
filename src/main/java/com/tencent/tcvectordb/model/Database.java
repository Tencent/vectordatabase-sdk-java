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
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.collection.AICollection;
import com.tencent.tcvectordb.model.collection.Collection;
import com.tencent.tcvectordb.model.param.collection.CreateAICollectionParam;
import com.tencent.tcvectordb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.entity.DataBaseType;
import com.tencent.tcvectordb.model.param.entity.DataBaseTypeRes;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.service.Stub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * VectorDB Database
 */
public class Database {
    private final Stub stub;
    private final String databaseName;
    @JsonIgnore
    private final ReadConsistencyEnum readConsistency;

    @JsonIgnore
    private DataBaseTypeEnum dbType;
    private static final Logger logger = LoggerFactory.getLogger(Database.class.getName());

    public Database(Stub stub, String databaseName, ReadConsistencyEnum readConsistency) {
        this.stub = stub;
        this.databaseName = databaseName;
        this.readConsistency = readConsistency;
        this.dbType = DataBaseTypeEnum.BASE;
        initDataBaseType();
    }

    public Database(Stub stub, String databaseName, ReadConsistencyEnum readConsistency, DataBaseTypeEnum dbType) {
        this.stub = stub;
        this.databaseName = databaseName;
        this.readConsistency = readConsistency;
        this.dbType = dbType;
    }

    private void initDataBaseType() throws VectorDBException{
        Map<String, DataBaseType> dataBaseTypeMap = stub.listDatabaseInfos();
        if (!dataBaseTypeMap.containsKey(this.databaseName)){
            throw new VectorDBException("database not existed");
        }
        this.dbType = DataBaseTypeEnum.valueOf(dataBaseTypeMap.get(this.databaseName).getDbType());
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public ReadConsistencyEnum getReadConsistency() {
        return readConsistency;
    }

    public DataBaseTypeEnum getDBType(){
        return dbType;
    }

    public Collection createCollection(CreateCollectionParam param) throws VectorDBException {
        param.setDatabase(databaseName);
        param.setReadConsistency(readConsistency);
        stub.createCollection(param);
        param.setStub(this.stub);
        return param;
    }

    public List<Collection> listCollections() throws VectorDBException {
        List<Collection> collections = stub.listCollections(this.databaseName);
        collections.forEach(c -> {
            c.setStub(stub);
            c.setReadConsistency(readConsistency);
        });
        return collections;
    }

    public AffectRes truncateCollections(String collectionName) {
        return stub.truncateCollection(this.databaseName, collectionName, this.dbType);
    }

    public Collection describeCollection(String collectionName) throws VectorDBException {
        Collection collection = stub.describeCollection(this.databaseName, collectionName);
        collection.setStub(stub);
        collection.setReadConsistency(readConsistency);
        return collection;
    }

    public void dropCollection(String collectionName) throws VectorDBException {
        stub.dropCollection(this.databaseName, collectionName);
    }

    public AffectRes setAlias(String collectionName, String aliasName) {
        return stub.setAlias(this.databaseName, collectionName, aliasName);
    }

    public AffectRes deleteAlias(String aliasName) {
        return stub.deleteAlias(this.databaseName, aliasName);
    }

    public Collection collection(String collectionName) throws VectorDBException {
        return describeCollection(collectionName);
    }

    public List<AICollection> listAICollections() throws VectorDBException {
        // 只有ai database可以查看ai表
        if (!(this.dbType.equals(DataBaseTypeEnum.AI_DOC) || this.dbType.equals(DataBaseTypeEnum.AI_DB))){
            throw new VectorDBException("database can not support create ai collection");
        }
        List<AICollection> collections = stub.listAICollections(this.databaseName);
        collections.forEach(c -> {
            c.setStub(stub);
            c.setReadConsistency(readConsistency);
        });
        return collections;
    }

    public AICollection createAICollection(CreateAICollectionParam param) throws VectorDBException {
        param.setDatabase(databaseName);
        param.setReadConsistency(readConsistency);
        stub.createAICollection(param);
        param.setStub(this.stub);
        return param;
    }

    public AICollection describeAICollection(String collectionName) throws VectorDBException {
        AICollection collection = stub.describeAICollection(this.databaseName, collectionName);
        collection.setStub(stub);
        collection.setReadConsistency(readConsistency);
        return collection;
    }

    public void dropAICollection(String collectionName) throws VectorDBException {
        stub.dropAICollection(this.databaseName, collectionName);
    }

    public AffectRes setAIAlias(String collectionName, String aliasName) {
        if (!(this.dbType.equals(DataBaseTypeEnum.AI_DOC) || this.dbType.equals(DataBaseTypeEnum.AI_DB))){
            return new AffectRes();
        }
        return stub.setAIAlias(this.databaseName, collectionName, aliasName);
    }

    public AffectRes deleteAIAlias(String aliasName) {
        if (!(this.dbType.equals(DataBaseTypeEnum.AI_DOC) || this.dbType.equals(DataBaseTypeEnum.AI_DB))){
            return new AffectRes();
        }
        return stub.deleteAIAlias(this.databaseName, aliasName);
    }


    @Override
    public String toString() {
        return String.format("{\"database\":\"%s\"}", this.databaseName);
    }
}
