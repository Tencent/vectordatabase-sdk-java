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
import com.tencentcloudapi.service.param.InsertParamInner;
import com.tencentcloudapi.utils.JSONUtil;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

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

        // document test
        if (args != null && args.length > 0) {
            testDocument();
            return;
        }
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

    static final String VDB_XLZ_DEV_DATABASE = "vdb_xlz_dev_database";
    static final String VDB_XLZ_DEV_COLLECTION = "vdb_xlz_dev_collection";
    static final Random randmo = new Random();


    private static void testDocument() {
        ConnectParam connectParam = ConnectParam.newBuilder()
	.withUrl("http://11.141.218.228:8100")
	.withUsername("root")
	.withKey("TO3pSbeYL1eC5EfTDPi438GXSREeqa0mfqVS1eEp").withTimeout(30)
	.build();
        VectorDBClient client = new VectorDBClient(connectParam);
        List<String> databaseList = client.listDatabase();
        if (!databaseList.contains(VDB_XLZ_DEV_DATABASE)) {
            client.createDatabase(VDB_XLZ_DEV_DATABASE);
        }

        Database database = client.database(VDB_XLZ_DEV_DATABASE);
        List<Collection> collectionList = database.listCollections();
        if (!collectionList.contains(VDB_XLZ_DEV_COLLECTION)) {
            CreateCollectionParam collectionParam = collectionParam();
            database.createCollection(collectionParam);
        }

        Collection collection = database.collection(VDB_XLZ_DEV_COLLECTION);
        InsertParam insertParam = initInsertParam();
        System.out.println("insertParam: " + JSONUtil.toJSONString(insertParam));
        collection.upsert(insertParam);
        System.out.println("inset finish");

        QueryParam queryParam = initQueryParam(insertParam);
        System.out.println("queryParam: " + JSONUtil.toJSONString(queryParam));
        List<Document> queryRes = collection.query(queryParam);
        System.out.println("queryRes: " + JSONUtil.toJSONString(queryRes));

        SearchByVectorParam searchParam = initSearchParam(insertParam);
        System.out.println("searchParam: " + JSONUtil.toJSONString(searchParam));
        List<List<Document>> searchRes = collection.search(searchParam);
        System.out.println("searchRes: " + JSONUtil.toJSONString(searchRes));

        DeleteParam deleteParam = initDeleteParam(insertParam);
        System.out.println("deleteParam: " + JSONUtil.toJSONString(deleteParam));
        collection.delete(deleteParam);


    }

    static DeleteParam initDeleteParam(InsertParam insertParam) {
        DeleteParam.Builder builder = new DeleteParam.Builder();
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
        builder.withDocumentIds(list);
        return builder.build();

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
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < insertParam.getDocuments().size() && i < 10; i++) {
            int index = randmo.nextInt(100);
            String s = formatId(8, index);
            if (list.contains(s)) {
                i--;
            } else {
                list.add(s);
            }
        }
        return builder.withDocumentIds(list).withRetrieveVector(false).build();
    }

    static InsertParam initInsertParam() {
        InsertParam.Builder builder = InsertParam.newBuilder();


        List<Document> list = new ArrayList<Document>();
        int insertCount = randmo.nextInt(100);

        for (int i = 0; i < insertCount; i++) {
            Document.Builder documentB = Document.newBuilder();
            documentB.withId(formatId(8, i)).withVector(vectors(10));
            list.add(documentB.build());
        }
        builder.withDocuments(list);


        return builder.build();
    }

    static List<Double> vectors(int len) {
        List<Double> list = new ArrayList<Double>();
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

    static CreateCollectionParam collectionParam() {
        CreateCollectionParam.Builder builder = CreateCollectionParam.newBuilder();
        builder.withDescription("xzl-test").withName(VDB_XLZ_DEV_COLLECTION).withReplicaNum(1).withShardNum(1);
        IndexField field1 = new IndexField();
        field1.setIndexType(IndexType.PRIMARY_KEY);
        field1.setFieldName("id");
        field1.setFieldType(FieldType.String);
        IndexField field2 = new VectorIndex("vector", 10, IndexType.HNSW, MetricType.L2,
                new HNSWParams(64, 0));
        IndexField field3 = new ScalarIndex("id", FieldType.String, IndexType.PRIMARY_KEY);
        builder.addField(field1).addField(field2).addField(field3);
        CreateCollectionParam build = builder.build();
        build.setDatabase(VDB_XLZ_DEV_DATABASE);
        return build;

    }

}
