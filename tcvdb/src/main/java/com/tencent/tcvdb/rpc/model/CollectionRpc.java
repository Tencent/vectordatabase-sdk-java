package com.tencent.tcvdb.rpc.model;

import com.tencent.tcvdb.exception.VectorDBException;
import com.tencent.tcvdb.model.Collection;
import com.tencent.tcvdb.model.Document;
import com.tencent.tcvdb.model.param.dml.*;
import com.tencent.tcvdb.model.param.entity.AffectRes;
import com.tencent.tcvdb.model.param.entity.BaseRes;
import com.tencent.tcvdb.model.param.entity.SearchRes;
import com.tencent.tcvdb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvdb.rpc.client.RPCClient;

import java.util.List;

public class CollectionRpc extends Collection {
    private RPCClient client;

    public CollectionRpc(RPCClient client, String database, String collection, ReadConsistencyEnum readConsistency) {
        super();
        this.client = client;
        this.setDatabase(database);
        this.collection = collection;
        this.setReadConsistency(readConsistency);
    }
    public CollectionRpc(RPCClient client, String database, String collection) {
        super();
        this.client = client;
        this.setDatabase(database);
        this.collection = collection;
    }

    public CollectionRpc( String database, String collection) {
        super();
        this.setDatabase(database);
        this.collection = collection;
    }

    public void setClient(RPCClient client) {
        this.client = client;
    }

    @Override
    public AffectRes upsert(InsertParam param) throws VectorDBException {
        return client.upsert(this.database, this.collection, param);
    }

    @Override
    public List<Document> query(QueryParam param) throws VectorDBException {
        return client.query(this.database, this.collection,param);
    }

    @Override
    public SearchRes search(SearchParam param) throws VectorDBException {
        return client.search(this.database, this.collection,param);
    }

    @Override
    public AffectRes delete(DeleteParam param) throws VectorDBException {
        return client.delete(this.database, this.collection,param);
    }

    @Override
    public AffectRes update(UpdateParam param, Document document) throws VectorDBException {
        return client.update(this.database, this.collection,param, document);
    }

    @Override
    public BaseRes rebuildIndex(RebuildIndexParam rebuildIndexParam) {
        return client.rebuildIndex(this.database, this.collection, rebuildIndexParam);
    }


}
