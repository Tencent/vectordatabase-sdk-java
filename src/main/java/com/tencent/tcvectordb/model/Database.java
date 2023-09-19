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

import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.service.Stub;

import java.util.List;

/**
 * VectorDB Database
 */
public class Database {
    private final Stub stub;
    private final String databaseName;

    public Database(Stub stub, String databaseName) {
        this.stub = stub;
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public Collection createCollection(CreateCollectionParam param) throws VectorDBException {
        param.setDatabase(databaseName);
        stub.createCollection(param);
        param.setStub(this.stub);
        return param;
    }

    public List<Collection> listCollections() throws VectorDBException {
        List<Collection> collections = stub.listCollections(this.databaseName);
        collections.forEach(c -> c.setStub(stub));
        return collections;
    }

    public AffectRes truncateCollections(String collectionName) {
        return stub.truncateCollection(this.databaseName, collectionName);
    }

    public Collection describeCollection(String collectionName) throws VectorDBException {
        Collection collection = stub.describeCollection(this.databaseName, collectionName);
        collection.setStub(stub);
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

    @Override
    public String toString() {
        return String.format("{\"database\":\"%s\"}", this.databaseName);
    }
}
