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
import com.tencentcloudapi.utils.JSONUtil;

import java.util.List;
import java.util.*;

/**
 * VectorDB Java SDK usage example
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class VectorDBExample {

    static final String VDB_XLZ_DEV_DATABASE = "vdb_xlz_dev_database";
    static final String VDB_XLZ_DEV_COLLECTION = "vdb_xlz_dev_collection";
    static final Random randmo = new Random();

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
        // 创建VectorDB Client
        // notice：插入操作成功到可用会有延迟
        ConnectParam connectParam = initConnecParam();
        VectorDBClient client = new VectorDBClient(connectParam);
        testDatabases(client);
        testCollection(client);
        testDocument(client);
        testDocument();
    }


    private static void testDocument() {

        ConnectParam connectParam = initConnecParam();
        VectorDBClient client = new VectorDBClient(connectParam);
        List<String> databaseList = client.listDatabase();
        if (!databaseList.contains(VDB_XLZ_DEV_DATABASE)) {
            client.createDatabase(VDB_XLZ_DEV_DATABASE);
        }

        Database database = client.database(VDB_XLZ_DEV_DATABASE);
        List<Collection> collectionList = database.listCollections();
        if (!collectionList.contains(VDB_XLZ_DEV_COLLECTION)) {
            CreateCollectionParam collectionParam = initCollectionParam();
            database.createCollection(collectionParam);
        }

        Collection collection = database.collection(VDB_XLZ_DEV_COLLECTION);
        InsertParam insertParam = initInsertParam();
        System.out.println("testDocument - insertParam: " + JSONUtil.toJSONString(insertParam));
        collection.upsert(insertParam);
        System.out.println("testDocument - inset finish");

        QueryParam queryParam = initQueryParam(insertParam);
        System.out.println("testDocument - queryParam: " + JSONUtil.toJSONString(queryParam));
        List<Document> queryRes = collection.query(queryParam);
        System.out.println("testDocument - queryRes: " + JSONUtil.toJSONString(queryRes));

        SearchByVectorParam searchParam = initSearchParam(insertParam);
        System.out.println("testDocument - searchParam: " + JSONUtil.toJSONString(searchParam));
        List<List<Document>> searchRes = collection.search(searchParam);
        System.out.println("testDocument - searchRes: " + JSONUtil.toJSONString(searchRes));

        DeleteParam deleteParam = initDeleteParam(insertParam);
        System.out.println("testDocument - deleteParam: " + JSONUtil.toJSONString(deleteParam));
        collection.delete(deleteParam);


    }

    static DeleteParam initDeleteParam(InsertParam insertParam) {
        DeleteParam.Builder builder = new DeleteParam.Builder();
        List<String> list = randomSelectDC(insertParam);
        builder.withDocumentIds(list);
        return builder.build();

    }

    private static List<String> randomSelectDC(InsertParam insertParam) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < insertParam.getDocuments().size() && i < 10; i++) {
            int index = randmo.nextInt(100);
            String s = formatId(8, index);
            if (list.contains(s)) {
                i--;
            } else {
                list.add(s);
            }
        }
        return list;
    }

    private static SearchByVectorParam initSearchParam(InsertParam insertParam) {
        List<List<Double>> vectors = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (int i = 0; i < insertParam.getDocuments().size() && i < 10; i++) {
            int index = randmo.nextInt(100);
            String s = formatId(8, index);
            if (set.contains(s)) {
                i--;
            } else {
                set.add(s);
                Document document = insertParam.getDocuments().stream().filter(dc -> dc.getId().equals(s)).findFirst().orElse(null);
                if (document != null) {
                    vectors.add(document.getVector());
                }
            }
        }
        return SearchByVectorParam.newBuilder().withVectors(vectors).build();
    }

    private static QueryParam initQueryParam(InsertParam insertParam) {
        QueryParam.Builder builder = QueryParam.newBuilder();
        List<String> list = randomSelectDC(insertParam);
        return builder.withDocumentIds(list).withRetrieveVector(false).build();
    }

    static InsertParam initInsertParam() {
        InsertParam.Builder builder = InsertParam.newBuilder();
        List<Document> list = new ArrayList<Document>();
        int insertCount;
        while ((insertCount = randmo.nextInt(10000)) < 100) {
            // ensure 100 documents
        }

        for (int i = 0; i < insertCount; i++) {
            Document.Builder documentB = Document.newBuilder();
            documentB.withId(formatId(8, i)).withVector(vectors(10))
                    .addScalarField(new DocField("sc", "sc" + i));
            list.add(documentB.build());
        }
        builder.withDocuments(list);


        return builder.build();
    }

    static List<Double> vectors(int len) {
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            list.add(Math.random());
        }
        return list;
    }

    static String formatId(int len, int value) {
        StringBuilder sb = new StringBuilder();
        String valStr = String.valueOf(value);
        for (int i = (valStr.length() - 1); i < len; i++) {
            sb.append("0");
        }
        return sb.append(valStr).toString();
    }

    static CreateCollectionParam initCollectionParam() {
        CreateCollectionParam.Builder builder = CreateCollectionParam.newBuilder();
        builder.withDescription("xzl-test").withName(VDB_XLZ_DEV_COLLECTION).withReplicaNum(1).withShardNum(1);
        IndexField field1 = new IndexField();
        field1.setIndexType(IndexType.PRIMARY_KEY);
        field1.setFieldName("id");
        field1.setFieldType(FieldType.String);
        IndexField field2 = new VectorIndex("vector", 10, IndexType.HNSW, MetricType.L2,
                new HNSWParams(64, 0));
        IndexField field3 = new ScalarIndex("sc", FieldType.String, IndexType.PRIMARY_KEY);
        builder.addField(field1).addField(field2).addField(field3);
        CreateCollectionParam build = builder.build();
        build.setDatabase(VDB_XLZ_DEV_DATABASE);
        return build;

    }

    private static ConnectParam initConnecParam() {
        ConnectParam connectParam = ConnectParam.newBuilder()
                .withUrl("http://11.141.218.228:8100")
                .withUsername("root")
                .withKey("TO3pSbeYL1eC5EfTDPi438GXSREeqa0mfqVS1eEp").withTimeout(30)
                .build();
        return connectParam;
    }


}
