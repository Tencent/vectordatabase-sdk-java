package com.tencentcloudapi;

import com.google.common.collect.Lists;
import com.tencentcloudapi.client.VectorDBClient;
import com.tencentcloudapi.model.Collection;
import com.tencentcloudapi.model.Database;
import com.tencentcloudapi.model.DocField;
import com.tencentcloudapi.model.Document;
import com.tencentcloudapi.model.param.collection.*;
import com.tencentcloudapi.model.param.database.ConnectParam;
import com.tencentcloudapi.model.param.dml.InsertParam;
import com.tencentcloudapi.service.param.InsertParamInner;

import java.util.ArrayList;
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
        client.createDatabase("db001");
        System.out.println(client.listDatabase());
        // drop database
        System.out.println("-drop db----------------------");
        client.dropDatabase("db001");
        System.out.println(client.listDatabase());
    }

    public static void testCollection(VectorDBClient client) {
//        client.dropDatabase("db001");
        Database db = client.createDatabase("db001");
//        Database db = client.database("db001");
        // create collection
        System.out.println("-create collections----------------------");
        CreateCollectionParam collectionParam = CreateCollectionParam.newBuilder()
                .withName("col1")
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
        Collection coll = db.describeCollection("col1");
        System.out.println(coll.toString());
        // drop collection
        System.out.println("-drop collection----------------------");
        db.dropCollection("col1");
        // list collections
        System.out.println("-list collections----------------------");
        List<Collection> cols2 = db.listCollections();
        for (Collection col : cols2) {
            System.out.println(col.toString());
        }
        System.out.println("-drop db----------------------");
        client.dropDatabase("db001");
    }

    public static void testDocument(VectorDBClient client) {
//        Database db = client.createDatabase("db001");
        Database db = client.database("db001");
        System.out.println("-create collections----------------------");
//        CreateCollectionParam collectionParam = CreateCollectionParam.newBuilder()
//                .withName("col1")
//                .withShardNum(3)
//                .withReplicaNum(2)
//                .withDescription("test collection0")
//                .addField(new ScalarIndex("id", FieldType.String, IndexType.PRIMARY_KEY))
//                .addField(new VectorIndex("vector", 3, IndexType.HNSW,
//                        MetricType.L2, new HNSWParams(64, 8)))
//                .addField(new ScalarIndex("other", FieldType.String, IndexType.FILTER))
//                .build();
//        Collection collection = db.createCollection(collectionParam);
        Collection collection = db.collection("col1");
        // upsert
        System.out.println("-upsert----------------------");
        Document doc1 = Document.newBuilder()
                .withId("0001")
                .addScalarField(new DocField("other", "doc1"))
                .withVector(Lists.newArrayList(0.2123, 0.23, 0.213))
                .build();
        Document doc2 = Document.newBuilder()
                .withId("0002")
                .addScalarField(new DocField("other", "doc1"))
                .withVector(Lists.newArrayList(0.2123, 0.23, 0.213))
                .build();
        Document doc3 = Document.newBuilder()
                .withId("0003").addScalarField(new DocField("other", "doc1"))
                .withVector(Lists.newArrayList(0.2123, 0.23, 0.213))
                .build();
        InsertParam insertParam = InsertParam.newBuilder()
                .addDocument(doc1)
                .addDocument(doc2)
                .addDocument(doc3)
                .build();
        InsertParamInner inner = new InsertParamInner("db001", "col1", insertParam.getDocuments());
        System.out.println(inner.toString());
//        collection.upsert(insertParam);
        // query
        System.out.println("-query----------------------");
        // search
        System.out.println("-search----------------------");
        // searchById
        System.out.println("-searchById----------------------");
        // delete
        System.out.println("-delete----------------------");
    }

    public static void main(String[] args) {
        // 创建VectorDB Client
        ConnectParam connectParam = ConnectParam.newBuilder()
                .withUrl("http://11.141.218.172:8100")
                .withUsername("root")
                .withKey("TO3pSbeYL1eC5EfTDPi438GXSREeqa0mfqVS1eEp")
                .withTimeout(30)
                .build();
        VectorDBClient client = new VectorDBClient(connectParam);
//        testDatabases(client);
//        testCollection(client);
        testDocument(client);
    }
}
