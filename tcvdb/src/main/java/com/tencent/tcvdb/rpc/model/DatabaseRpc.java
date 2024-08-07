package com.tencent.tcvdb.rpc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tencent.tcvdb.exception.VectorDBException;
import com.tencent.tcvdb.model.Collection;
import com.tencent.tcvdb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvdb.model.param.entity.AffectRes;
import com.tencent.tcvdb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvdb.rpc.client.RPCClient;

import java.util.List;

public class DatabaseRpc{
    @JsonIgnore
    private final RPCClient client;
    private final String databaseName;
    @JsonIgnore
    private final ReadConsistencyEnum readConsistency;

    public DatabaseRpc(RPCClient client, String databaseName, ReadConsistencyEnum readConsistency) {
        this.client = client;
        this.databaseName = databaseName;
        this.readConsistency = readConsistency;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public ReadConsistencyEnum getReadConsistency() {
        return readConsistency;
    }

    public CollectionRpc createCollection(CreateCollectionParam param) throws VectorDBException {
        param.setDatabase(databaseName);
        param.setReadConsistency(readConsistency);
        CollectionRpc collectionRpc = client.createCollection(param, false);
        collectionRpc.setClient(client);
        collectionRpc.setReadConsistency(readConsistency);
        return collectionRpc;
    }

    public List<CollectionRpc> listCollections() throws VectorDBException {
        List<CollectionRpc> collections = client.listCollections(this.databaseName, false);
        collections.forEach(c -> {
            c.setClient(client);
            c.setReadConsistency(readConsistency);
        });
        return collections;
    }

    public AffectRes truncateCollections(String collectionName) {
        return client.truncateCollections(this.databaseName, collectionName);
    }

    public CollectionRpc describeCollection(String collectionName) {
        CollectionRpc collection = client.describeCollection(this.databaseName, collectionName);
        collection.setClient(client);
        collection.setReadConsistency(readConsistency);
        return collection;
    }

    public void dropCollection(String collectionName) {
        client.dropCollection(this.databaseName, collectionName);
    }

    public AffectRes setAlias(String collectionName, String aliasName) {
        return client.setAlias(this.databaseName, collectionName, aliasName);
    }

    public AffectRes deleteAlias(String aliasName) {
        return client.deleteAlias(this.databaseName, aliasName);
    }

    public Collection collection(String collectionName) {
        return describeCollection(collectionName);
    }

    @Override
    public String toString() {
        return String.format("{\"database\":\"%s\"}", this.databaseName);
    }
}
