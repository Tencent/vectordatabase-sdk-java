package com.tencentcloudapi;

import com.google.common.collect.Lists;
import com.tencentcloudapi.client.VectorDBClient;
import com.tencentcloudapi.model.Collection;
import com.tencentcloudapi.model.Database;
import com.tencentcloudapi.model.DocField;
import com.tencentcloudapi.model.Document;
import com.tencentcloudapi.model.param.collection.*;
import com.tencentcloudapi.model.param.database.ConnectParam;
import com.tencentcloudapi.model.param.dml.*;

import java.util.List;

/**
 * VectorDB Java SDK usage example
 * User: wlleiiwang
 * Date: 2023/7/24
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

    public static void testCollection(VectorDBClient client) {
        Database db = client.createDatabase("vdb001");
//        Database db = client.database("vdb001");
        // list collections
        System.out.println("-list collections----------------------");
        List<Collection> cols3 = db.listCollections();
        for (Collection col : cols3) {
            System.out.println(col.toString());
        }
        // create collection
        System.out.println("-create collections----------------------");
        CreateCollectionParam collectionParam = CreateCollectionParam.newBuilder()
                .withName("coll")
                .withShardNum(3)
                .withReplicaNum(2)
                .withDescription("test collection0")
                .addField(new ScalarIndex("id", FieldType.String, IndexType.PRIMARY_KEY))
                .addField(new VectorIndex("vector", 3, IndexType.HNSW,
                        MetricType.L2, new HNSWParams(64, 8)))
                .addField(new ScalarIndex("other", FieldType.String, IndexType.FILTER))
                .build();
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

    public static void testDocument(VectorDBClient client) {
//        Database db = client.createDatabase("vdb001");
        Database db = client.database("vdb001");
        List<Collection> cols3 = db.listCollections();
        System.out.println("-create collections----------------------");
        CreateCollectionParam collectionParam = CreateCollectionParam.newBuilder()
                .withName("coll")
                .withShardNum(3)
                .withReplicaNum(2)
                .withDescription("test collection0")
                .addField(new ScalarIndex("id", FieldType.String, IndexType.PRIMARY_KEY))
                .addField(new VectorIndex("vector", 3, IndexType.HNSW,
                        MetricType.L2, new HNSWParams(64, 8)))
                .addField(new ScalarIndex("other", FieldType.String, IndexType.FILTER))
                .build();
        Collection collection = db.createCollection(collectionParam);
//        Collection collection = db.collection("coll1");
        // upsert
        System.out.println("-upsert----------------------");
        Document doc1 = Document.newBuilder()
                .withId("0001")
                .addScalarField(new DocField("other", "doc1"))
                .withVector(Lists.newArrayList(0.2123, 0.23, 0.213))
                .build();
        Document doc2 = Document.newBuilder()
                .withId("0002")
                .addScalarField(new DocField("other", "doc2"))
                .withVector(Lists.newArrayList(0.4123, 0.43, 0.413))
                .build();
        Document doc3 = Document.newBuilder()
                .withId("0003").addScalarField(new DocField("other", "doc3"))
                .withVector(Lists.newArrayList(0.8123, 0.83, 0.813))
                .build();
        InsertParam insertParam = InsertParam.newBuilder()
                .addDocument(doc1)
                .addDocument(doc2)
                .addDocument(doc3)
                .build();
        collection.upsert(insertParam);
        // query
        System.out.println("-query----------------------");
        QueryParam queryParam = QueryParam.newBuilder()
                .withDocumentIds(Lists.newArrayList("0001", "0002", "0003"))
                .build();
        List<Document> qdos = collection.query(queryParam);
        for (Document doc : qdos) {
            System.out.println(doc.toString());
        }
        // search by vector
        System.out.println("-searchByVector----------------------");
        SearchByVectorParam searchByVectorParam = SearchByVectorParam.newBuilder()
                .addVector(Lists.newArrayList(0.3123, 0.43, 0.213))
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
                .withDocumentIds(Lists.newArrayList("0001", "0002"))
                .withHNSWSearchParams(new HNSWSearchParams(10))
                .withLimit(10)
                .build();
        List<List<Document>> siDocs = collection.searchById(searchByIdParam);
        for (List<Document> docs : siDocs) {
            for (Document doc : docs) {
                System.out.println(doc.toString());
            }
        }
        // search by filter
        System.out.println("-searchByFilter----------------------");
        SearchByVectorParam searchByFilterParam = SearchByVectorParam.newBuilder()
                .addVector(Lists.newArrayList(0.3123, 0.43, 0.213))
                .withFilter(new Filter("other=\"doc1\""))
                .withHNSWSearchParams(new HNSWSearchParams(10))
                .withRetrieveVector(true)
                .withLimit(10)
                .build();
        List<List<Document>> sfDocs = collection.search(searchByFilterParam);
        for (List<Document> docs : sfDocs) {
            for (Document doc : docs) {
                System.out.println(doc.toString());
            }
        }// delete
        System.out.println("-delete----------------------");
        DeleteParam deleteParam = DeleteParam.newBuilder()
                .withDocumentIds(Lists.newArrayList("0001", "0002", "0003"))
                .build();
        collection.delete(deleteParam);
        // query
        List<Document> qdos2 = collection.query(queryParam);
        for (Document doc : qdos2) {
            System.out.println(doc.toString());
        }
        client.dropDatabase("vdb001");
    }

    public static void main(String[] args) {
        // notice：插入操作成功到可用会有延迟
        ConnectParam connectParam = ConnectParam.newBuilder()
                .withUrl("http://11.141.218.228:8100")
                .withUsername("root")
                .withKey("TO3pSbeYL1eC5EfTDPi438GXSREeqa0mfqVS1eEp")
                .withTimeout(30)
                .build();
        VectorDBClient client = new VectorDBClient(connectParam);
        testDatabases(client);
        testCollection(client);
        testDocument(client);
    }
}
