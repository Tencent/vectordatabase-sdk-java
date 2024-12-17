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
import com.tencent.tcvectordb.client.VectorDBClient;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.entity.DataBaseType;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.service.Stub;

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

    public Database(Stub stub, String databaseName, ReadConsistencyEnum readConsistency) {
        this.stub = stub;
        this.databaseName = databaseName;
        this.readConsistency = readConsistency;
//        ensureDataBaseType();
    }

    private void ensureDataBaseType() throws VectorDBException{
        Map<String, DataBaseType> dataBaseTypeMap = stub.listDatabaseInfos();
        if (!dataBaseTypeMap.containsKey(this.databaseName)){
            throw new VectorDBException("database not existed");
        }
        if(DataBaseTypeEnum.isAIDataBase(DataBaseTypeEnum.valueOf(dataBaseTypeMap.get(this.databaseName).getDbType()))){
            throw new VectorDBException("database is ai database");
        }
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public ReadConsistencyEnum getReadConsistency() {
        return readConsistency;
    }

    /***
     * create collection
     * @param param createCollectionParam: the parameters of the collection to be created
     *              databaseName: the name of the database to be created
     *              collection: the name of the collection to be created
     *              replicaNum: the number of replicas
     *              shardNum: the number of shards
     *              description: the description of the collection
     *              indexes: list of the index fields
     *              embedding: Embedding class
     *              ttlConfig: TTLConfig class
     *
     * @return collection object
     */
    public Collection createCollection(CreateCollectionParam param) throws VectorDBException {
        param.setDatabase(databaseName);
        param.setReadConsistency(readConsistency);
        stub.createCollection(param);
        param.setStub(this.stub);
        return param;
    }

    /***
     * create collection if not exists
     * @param param createCollectionParam: the parameters of the collection to be created
     *              databaseName: the name of the database to be created
     *              collection: the name of the collection to be created
     *              replicaNum: the number of replicas
     *              shardNum: the number of shards
     *              description: the description of the collection
     *              indexes: list of the index fields
     *              embedding: Embedding class
     *
     * @return collection object
     * @throws VectorDBException
     */
    public Collection createCollectionIfNotExists(CreateCollectionParam param) throws VectorDBException {
        Collection collections = null;
        try {
            collections = stub.describeCollection(this.databaseName, param.getCollection());
        }catch (Exception e){}
        param.setDatabase(databaseName);
        param.setReadConsistency(readConsistency);
        if (collections==null){
            stub.createCollection(param);
        }
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

    /**
     * this method is deprecated, recommend use {@link Database#IsExistsCollection(String)}
     * exists collection, true if collection exists else false
     * @param collection
     * @return boolean
     * @throws VectorDBException
     */
    @Deprecated
    public Boolean existsCollection(String collection) throws VectorDBException {
        Collection collectionInfo = null;
        try {
            collectionInfo = stub.describeCollection(this.databaseName, collection);
        }catch (Exception e){

        }
        return collectionInfo!=null;
    }

    /**
     * exists collection, true if collection exists else false
     * @param collection
     * @return boolean
     * @throws VectorDBException
     */
    public Boolean IsExistsCollection(String collection) throws VectorDBException {
        Collection collectionInfo = null;
        try {
            collectionInfo = stub.describeCollection(this.databaseName, collection);
        }catch (Exception e){

        }
        return collectionInfo!=null;
    }

    /**
     * truncate collection
     * @param collectionName
     * @return
     */
    public AffectRes truncateCollections(String collectionName) {
        return stub.truncateCollection(this.databaseName, collectionName, DataBaseTypeEnum.BASE_DB);
    }

    /**
     * describe collection
     * @param collectionName
     * @return Collection if collection exist
     * @throws VectorDBException
     */
    public Collection describeCollection(String collectionName) throws VectorDBException {
        Collection collection = stub.describeCollection(this.databaseName, collectionName);
        collection.setStub(stub);
        collection.setReadConsistency(readConsistency);
        return collection;
    }

    /**
     * drop collection
     * @param collectionName
     * @throws VectorDBException
     */
    public void dropCollection(String collectionName) throws VectorDBException {
        stub.dropCollection(this.databaseName, collectionName);
    }

    /**
     * set alias for collection
     * @param collectionName
     * @param aliasName
     * @return
     */
    public AffectRes setAlias(String collectionName, String aliasName) {
        return stub.setAlias(this.databaseName, collectionName, aliasName);
    }

    /**
     * delete alias of collection
     * @param aliasName
     * @return
     */
    public AffectRes deleteAlias(String aliasName) {
        return stub.deleteAlias(this.databaseName, aliasName);
    }

    /**
     * get collection info
     * @param collectionName
     * @return
     * @throws VectorDBException
     */
    public Collection collection(String collectionName) throws VectorDBException {
        return describeCollection(collectionName);
    }

    @Override
    public String toString() {
        return String.format("{\"database\":\"%s\"}", this.databaseName);
    }
}
