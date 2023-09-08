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

package com.tencent.tcvectordb;

import com.tencent.tcvectordb.client.VectorDBClient;
import com.tencent.tcvectordb.model.Collection;
import com.tencent.tcvectordb.model.Database;
import com.tencent.tcvectordb.model.DocField;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvectordb.model.param.collection.FieldType;
import com.tencent.tcvectordb.model.param.collection.FilterIndex;
import com.tencent.tcvectordb.model.param.collection.HNSWParams;
import com.tencent.tcvectordb.model.param.collection.IndexType;
import com.tencent.tcvectordb.model.param.collection.MetricType;
import com.tencent.tcvectordb.model.param.collection.VectorIndex;
import com.tencent.tcvectordb.model.param.database.ConnectParam;

import com.tencent.tcvectordb.model.param.dml.InsertParam;
import com.tencent.tcvectordb.model.param.dml.QueryParam;
import com.tencent.tcvectordb.model.param.dml.SearchByVectorParam;
import com.tencent.tcvectordb.model.param.dml.SearchByIdParam;
import com.tencent.tcvectordb.model.param.dml.HNSWSearchParams;
import com.tencent.tcvectordb.model.param.dml.DeleteParam;
import com.tencent.tcvectordb.model.param.dml.Filter;

import java.util.List;
import java.util.*;

/**
 * VectorDB Java SDK usage example
 */
public class VectorDBExample {

    public static void testDatabases(VectorDBClient client) {
        // List databases
        System.out.println("-list db----------------------");
        List<String> dbs = client.listDatabase();
        System.out.println(dbs);
        // create database
        System.out.println("-create db----------------------");
        client.createDatabase("vdb001");
        System.out.println(client.listDatabase());
        // drop database
        System.out.println("-drop db----------------------");
        client.dropDatabase("vdb001");
        System.out.println(client.listDatabase());
    }

    private static CreateCollectionParam initCreateCollectionParam() {
        return CreateCollectionParam.newBuilder()
                .withName("coll")
                .withShardNum(3)
                .withReplicaNum(2)
                .withDescription("test collection0")
                .addField(new FilterIndex("id", FieldType.String, IndexType.PRIMARY_KEY))
                .addField(new VectorIndex("vector", 3, IndexType.HNSW,
                        MetricType.L2, new HNSWParams(64, 8)))
                .addField(new FilterIndex("otherStr", FieldType.String, IndexType.FILTER))
                .addField(new FilterIndex("otherInt", FieldType.Uint64, IndexType.FILTER))
                .build();
    }

    public static void testCollection(VectorDBClient client) {
        Database db = client.createDatabase("vdb001");
        // Database db = client.database("vdb001");
        // list collections
        System.out.println("-list collections----------------------");
        List<Collection> cols3 = db.listCollections();
        for (Collection col : cols3) {
            System.out.println(col.toString());
        }
        // create collection
        System.out.println("-create collections----------------------");
        CreateCollectionParam collectionParam = initCreateCollectionParam();
        db.createCollection(collectionParam);
        // list collections
        System.out.println("-list collections----------------------");
        List<Collection> cols = db.listCollections();
        for (Collection col : cols) {
            System.out.println(col.toString());
        }
        // describe collection
        System.out.println("-describe collection----------------------");
        Collection coll = db.describeCollection("coll");
        System.out.println(coll.toString());
        // drop collection
        System.out.println("-drop collection----------------------");
        db.dropCollection("coll");
        // list collections
        System.out.println("-list collections----------------------");
        List<Collection> cols2 = db.listCollections();
        for (Collection col : cols2) {
            System.out.println(col.toString());
        }
        System.out.println("-drop db----------------------");
        client.dropDatabase("vdb001");
    }

    public static void testDocument(VectorDBClient client) throws InterruptedException {
        Database db = client.createDatabase("vdb001");
        // Database db = client.database("vdb001");
        System.out.println("-create collections----------------------");
        CreateCollectionParam collectionParam = initCreateCollectionParam();
        Collection collection = db.createCollection(collectionParam);
        // Collection collection = db.collection("coll1");
        // upsert
        System.out.println("-upsert----------------------");
        Document doc1 = Document.newBuilder()
                .withId("0001")
                .addFilterField(new DocField("otherStr", "doc1"))
                .addFilterField(new DocField("otherInt", 1))
                .withVector(Arrays.asList(0.2123, 0.23, 0.213))
                .build();
        Document doc2 = Document.newBuilder()
                .withId("0002")
                .addFilterField(new DocField("otherStr", "doc2"))
                .addFilterField(new DocField("otherInt", 2))
                .withVector(Arrays.asList(0.4123, 0.43, 0.413))
                .build();
        Document doc3 = Document.newBuilder()
                .withId("0003")
                .addFilterField(new DocField("otherStr", "doc3"))
                .addFilterField(new DocField("otherInt", 3))
                .withVector(Arrays.asList(0.8123, 0.83, 0.813))
                .build();
        InsertParam insertParam = InsertParam.newBuilder()
                .addDocument(doc1)
                .addDocument(doc2)
                .addDocument(doc3)
                .build();
        collection.upsert(insertParam);
        // notice：upsert操作可用会有延迟
        Thread.sleep(1000*10);
        // query
        System.out.println("-query----------------------");
        QueryParam queryParam = QueryParam.newBuilder()
                .withDocumentIds(Arrays.asList("0001", "0002", "0003"))
                .build();
        List<Document> qdos = collection.query(queryParam);
        for (Document doc : qdos) {
            System.out.println(doc.toString());
        }
        // search by vector
        System.out.println("-searchByVector----------------------");
        SearchByVectorParam searchByVectorParam = SearchByVectorParam.newBuilder()
                .addVector(Arrays.asList(0.3123, 0.43, 0.213))
                .withHNSWSearchParams(new HNSWSearchParams(10))
                .withLimit(10)
                .build();
        List<List<Document>> svDocs = collection.search(searchByVectorParam);
        for (List<Document> docs : svDocs) {
            for (Document doc : docs) {
                System.out.println(doc.toString());
            }
        }
        // search by id
        System.out.println("-searchById----------------------");
        SearchByIdParam searchByIdParam = SearchByIdParam.newBuilder()
                .withDocumentIds(Arrays.asList("0001", "0002"))
                .withHNSWSearchParams(new HNSWSearchParams(10))
                .withLimit(10)
                .build();
        List<List<Document>> siDocs = collection.searchById(searchByIdParam);
        int i = 0;
        for (List<Document> docs : siDocs) {
            System.out.println(i++);
            for (Document doc : docs) {
                System.out.println(doc.toString());
            }
        }
        // search by filter
        System.out.println("-searchByFilter----------------------");
        SearchByVectorParam searchByFilterParam = SearchByVectorParam.newBuilder()
                .addVector(Arrays.asList(0.3123, 0.43, 0.213))
                .withFilter(new Filter("otherStr=\"doc1\"").or("otherInt=3"))
                .withHNSWSearchParams(new HNSWSearchParams(10))
                .withRetrieveVector(true)
                .withLimit(10)
                .build();
        List<List<Document>> sfDocs = collection.search(searchByFilterParam);
        for (List<Document> docs : sfDocs) {
            for (Document doc : docs) {
                System.out.println(doc.toString());
            }
        }
        // delete
        System.out.println("-delete----------------------");
        DeleteParam deleteParam = DeleteParam.newBuilder()
                .withDocumentIds(Arrays.asList("0001", "0002", "0003"))
                .build();
        collection.delete(deleteParam);
        // notice：delete操作可用会有延迟
        Thread.sleep(1000*5);
        // query
        List<Document> qdos2 = collection.query(queryParam);
        for (Document doc : qdos2) {
            System.out.println(doc.toString());
        }
        client.dropDatabase("vdb001");
    }

    public static void main(String[] args) throws InterruptedException {
        // 创建VectorDB Client
        ConnectParam connectParam = initConnectParam();
        VectorDBClient client = new VectorDBClient(connectParam);
        // Database相关示例
        testDatabases(client);
        // Collection相关示例
        testCollection(client);
        // Document相关示例
        testDocument(client);
        // Filter示例
        testFilter();
    }

    public static void testFilter() {
        System.out.println(new Filter("author=\"jerry\"")
                .and("a=1")
                .or("r=\"or\"")
                .orNot("rn=2")
                .andNot("an=\"andNot\"")
                .getCond());
        System.out.println(Filter.in("key", Arrays.asList("v1", "v2", "v3")));
        System.out.println(Filter.in("key", Arrays.asList(1, 2, 3)));
    }

    private static ConnectParam initConnectParam() {
        return ConnectParam.newBuilder()
                .withUrl("http://10.0.X.X")
                .withUsername("root")
                .withKey("eC4bLRy2va******************************")
                .withTimeout(30)
                .build();
    }
}