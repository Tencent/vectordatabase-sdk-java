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
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.Collection;
import com.tencent.tcvectordb.model.Database;
import com.tencent.tcvectordb.model.DocField;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.collection.*;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.utils.JSONUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.tencent.tcvectordb.model.param.enums.EmbeddingModelEnum.M3E_BASE;

/**
 * VectorDB Java SDK usage example
 */
public class VectorDBExample {

    static final String VDB_XLZ_DEV_DATABASE = "vdb_xlz_dev_database";
    static final String VDB_XLZ_DEV_COLLECTION = "vdb_xlz_dev_collection";
    static final Random randmo = new Random();

    public static void testDatabases(VectorDBClient client) {
        System.out.println("- clear before test ----------------------");
        String dbname = "vdb001";

        anySafe(() -> client.dropDatabase(dbname));
        // List databases
        System.out.println("-list db----------------------");
        List<String> dbs = client.listDatabase();
        System.out.println("\tres: " + dbs);
        // create database
        System.out.println("-create db----------------------");
        client.createDatabase("vdb001");
        System.out.println("\tres: " + client.listDatabase());
        // drop database
        System.out.println("-drop db----------------------");
        client.dropDatabase("vdb001");
        System.out.println("\tres: " + client.listDatabase());
    }


    public static void testCollection(VectorDBClient client) {
        String dbname = "vdb001";
        String collName = "collection";

        System.out.println("- clear before test ----------------------");
        anySafe(() -> client.dropDatabase(dbname));
        Database db = client.createDatabase(dbname);

        // list collections
        System.out.println("-list collections----------------------");
        List<Collection> cols3 = db.listCollections();
        for (Collection col : cols3) {
            System.out.println("\tres: " + col.toString());
        }
        // create collection
        System.out.println("-create collections----------------------");
        CreateCollectionParam collectionParam = initCreateCollectionParam(collName);
        db.createCollection(collectionParam);


        // list collections
        System.out.println("-list collections----------------------");
        List<Collection> cols = db.listCollections();
        for (Collection col : cols) {
            System.out.println("\tres: " + col.toString());
        }

        // describe collection
        System.out.println("-describe collection----------------------");
        Collection coll = db.describeCollection(collName);
        System.out.println("\tres: " + coll.toString());

        // flush collection
        System.out.println("- flush collection----------------------");
        AffectRes affectRes = db.truncateCollections(collName);
        System.out.println("\tres: " + affectRes);

        // drop collection
        System.out.println("-drop collection----------------------");
        db.dropCollection(collName);

        // list collections
        System.out.println("-list collections----------------------");
        List<Collection> cols2 = db.listCollections();
        for (Collection col : cols2) {
            System.out.println("\tres: " + col.toString());
        }

        System.out.println("-drop db----------------------");
        client.dropDatabase("vdb001");
    }

    public static void testCollectionEmbedding(VectorDBClient client) {
        String dbname = "vdb001";
        String collName = "collection";

        System.out.println("- clear before test ----------------------");
        anySafe(() -> client.dropDatabase(dbname));
        Database db = client.createDatabase(dbname);

        // list collections
        System.out.println("-list collections----------------------");
        List<Collection> cols3 = db.listCollections();
        for (Collection col : cols3) {
            System.out.println("\tres: " + col.toString());
        }

        // create embedding collection
        System.out.println("- create embedding collections----------------------");
        CreateCollectionParam embeddingCollectionParam = initCreateEmbeddingCollectionParam(collName);
        db.createCollection(embeddingCollectionParam);

        // list collections
        System.out.println("-list collections----------------------");
        List<Collection> cols = db.listCollections();
        for (Collection col : cols) {
            System.out.println("\tres: " + col.toString());
        }
        // describe collection
        System.out.println("-describe collection----------------------");
        Collection coll = db.describeCollection(collName);
        System.out.println("\tres: " + coll.toString());

        // truncate collection
        System.out.println("- flush collection----------------------");
        AffectRes affectRes = db.truncateCollections(collName);
        System.out.println("\tres: " + affectRes);


        // drop collection
        System.out.println("-drop collection----------------------");
        db.dropCollection(collName);
        // list collections
        System.out.println("-list collections----------------------");
        List<Collection> cols2 = db.listCollections();
        for (Collection col : cols2) {
            System.out.println("\tres: " + col.toString());
        }

        System.out.println("-drop db----------------------");
        client.dropDatabase("vdb001");
    }

    public static void testDocument(VectorDBClient client) throws InterruptedException {

        String dbname = "vdb001";
        String collName = "collection";

        anySafe(() -> client.dropDatabase(dbname));

        Database db = client.createDatabase(dbname);
        // Database db = client.database("vdb001");
        System.out.println("-create collections----------------------");
        CreateCollectionParam collectionParam = initCreateCollectionParam(collName);
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
        Thread.sleep(1000 * 5);
        // query
        System.out.println("-query----------------------");
        QueryParam queryParam = QueryParam.newBuilder()
                .withDocumentIds(Arrays.asList("0001", "0002", "0003"))
                .build();
        List<Document> qdos = collection.query(queryParam);
        for (Document doc : qdos) {
            System.out.println("\tres: " + doc.toString());
        }
        // search by vector
        System.out.println("-searchByVector----------------------");
        SearchByVectorParam searchByVectorParam = SearchByVectorParam.newBuilder()
                .addVector(Arrays.asList(0.3123, 0.43, 0.213))
                .withParams(new HNSWSearchParams(10))
                .withLimit(10)
                .build();
        List<List<Document>> svDocs = collection.search(searchByVectorParam);
        for (List<Document> docs : svDocs) {
            for (Document doc : docs) {
                System.out.println("\tres: " + doc.toString());
            }
        }
        // search by id
        System.out.println("-searchById----------------------");
        SearchByIdParam searchByIdParam = SearchByIdParam.newBuilder()
                .withDocumentIds(Arrays.asList("0001", "0002"))
                .withParams(new HNSWSearchParams(10))
                .withLimit(10)
                .build();
        List<List<Document>> siDocs = collection.searchById(searchByIdParam);
        int i = 0;
        for (List<Document> docs : siDocs) {
            System.out.println("\tres: " + i++);
            for (Document doc : docs) {
                System.out.println("\tres: " + doc.toString());
            }
        }
        // search by filter
        System.out.println("-searchByFilter----------------------");
        SearchByVectorParam searchByFilterParam = SearchByVectorParam.newBuilder()
                .addVector(Arrays.asList(0.3123, 0.43, 0.213))
                .withFilter(new Filter("otherStr=\"doc1\"").or("otherInt=3"))
                .withParams(new HNSWSearchParams(10))
                .withRetrieveVector(true)
                .withLimit(10)
                .build();
        List<List<Document>> sfDocs = collection.search(searchByFilterParam);
        for (List<Document> docs : sfDocs) {
            for (Document doc : docs) {
                System.out.println("\tres: " + doc.toString());
            }
        }
        // delete
        System.out.println("-delete----------------------");
        DeleteParam deleteParam = DeleteParam.newBuilder()
                .withDocumentIds(Arrays.asList("0001", "0002", "0003"))
                .build();
        collection.delete(deleteParam);
        // notice：delete操作可用会有延迟
        Thread.sleep(1000 * 5);
        // query
        List<Document> qdos2 = collection.query(queryParam);
        for (Document doc : qdos2) {
            System.out.println("\tres: " + doc.toString());
        }
        client.dropDatabase("vdb001");
    }

    public static void testDocumentEmbedding(VectorDBClient client) throws InterruptedException {

        String dbname = "vdb001";
        String collName = "collection";
        String collNameAlias = "collection_alias";

        System.out.println("- clear before test ----------------------");
        anySafe(() -> client.dropDatabase(dbname));
        Database db = client.createDatabase(dbname);

        // Database db = client.database("vdb001");
        System.out.println("-create collections----------------------");
        CreateCollectionParam collectionParam = initCreateEmbeddingCollectionParam(collName);
        Collection collection = db.createCollection(collectionParam);

        // describe collection before set alias
        System.out.println("- describe collection before set alias ----------------------");
        Collection descCollRes01 = db.describeCollection(collName);
        System.out.println("\tres: " + descCollRes01.toString());

        // set alias
        System.out.println("- set collection alias ----------------------");
        AffectRes affectRes1 = db.setCollectionAlias(collName, collNameAlias);
        System.out.println("\tres: " + affectRes1);

        System.out.println("- describe collection after set collection alias ----------------------");
        Collection descCollRes02 = db.describeCollection(collName);
        System.out.println("\tres: " + descCollRes02.toString());

        // delete alias
        System.out.println("- delete collection alias ----------------------");
        AffectRes affectRes2 = db.deleteCollectionAlias(collNameAlias);
        System.out.println("\tres: " + affectRes2);


        System.out.println("- describe collection after delete collection alias ----------------------");
        Collection descCollRes03 = db.describeCollection(collName);
        System.out.println("\tres: " + descCollRes03.toString());

        // Collection collection = db.collection("coll1");
        // upsert
        System.out.println("-upsert----------------------");
        List<Document> documents = initDocuments(true, 10, M3E_BASE.getDimension(), 0);
        InsertParam insertParam = InsertParam.newBuilder()
                .addAllDocument(documents)
                .build();
        collection.upsert(insertParam);

        // notice：upsert操作可用会有延迟
        Thread.sleep(1000 * 5);
        // query
        System.out.println("-query----------------------");
        QueryParam queryParam = QueryParam.newBuilder()
                .withDocumentIds(Arrays.asList("00001", "00002", "00003"))
                .withRetrieveVector(true)
                .build();
        List<Document> qdos = collection.query(queryParam);
        for (Document doc : qdos) {
            System.out.println("\tres: " + doc.toString());
        }

        System.out.println("- describe collection ----------------------");
        Collection descCollRes04 = db.describeCollection(collName);
        System.out.println("\tres: " + descCollRes04.toString());

        // search by vector
        System.out.println("-searchByVector----------------------");
        SearchByVectorParam searchByVectorParam = SearchByVectorParam.newBuilder()
                .addVector(qdos
                        .stream()
                        .filter(dc -> dc.getId().equals("00001"))
                        .findFirst()
                        .orElse(Document.newBuilder().build())
                        .getVector())
                .withParams(new HNSWSearchParams(16))
                .withLimit(2)
                .build();
        List<List<Document>> svDocs = collection.search(searchByVectorParam);
        for (List<Document> docs : svDocs) {
            for (Document doc : docs) {
                System.out.println("\tres: " + doc.toString());
            }
        }
        // search by id
        System.out.println("-searchById----------------------");
        SearchByIdParam searchByIdParam = SearchByIdParam.newBuilder()
                .withDocumentIds(Arrays.asList("00001", "00002", "00009"))
                .withParams(new HNSWSearchParams(16))
                // each row will return "limit" rows that contains their self
                // example: limit = 3, will return 9 rows(must have enough rows)
                .withLimit(3)
                .build();
        List<List<Document>> siDocs = collection.searchById(searchByIdParam);
        for (List<Document> docs : siDocs) {
            for (Document doc : docs) {
                System.out.println("\tres: " + doc.toString());
            }
        }


        System.out.println("- searchById before update ----------------------");
        SearchByIdParam searchByIdParam1 = SearchByIdParam.newBuilder()
                .withDocumentIds(Arrays.asList("00001"))
                .withParams(new HNSWSearchParams(16))
                .withLimit(3)
                .build();
        List<List<Document>> siDocs1 = collection.searchById(searchByIdParam1);
        for (List<Document> docs : siDocs1) {
            for (Document doc : docs) {
                System.out.println("\tres: " + doc.toString());
            }
        }
        // update
        List<Document> documents1 = siDocs.get(0);
        documents1.sort(Comparator.comparing(Document::getScore));
        UpdateParam updateParam = UpdateParam.newBuilder().addDocumentId(documents1.get(1).getId()).build();
        System.out.println("- update " + updateParam.getDocumentIds() + "  ----------------------");
        Document build = Document
                .newBuilder()
                .addFilterField(new DocField("text", randomText(2)))
                .addFilterField(new DocField("authorParam", "update-author"))
                .build();
        collection.update(updateParam, build);
        Thread.sleep(5 * 1000);

        System.out.println("- searchById after update ----------------------");
        SearchByIdParam searchByIdParam2 = SearchByIdParam.newBuilder()
                .withDocumentIds(Arrays.asList("00001"))
                .withParams(new HNSWSearchParams(16))
                .withLimit(3)
                .build();
        List<List<Document>> siDocs2 = collection.searchById(searchByIdParam2);
        for (List<Document> docs : siDocs2) {
            for (Document doc : docs) {
                System.out.println("\tres: " + doc.toString());
            }
        }

        // search by embeddingItems
        System.out.println("- searchByEmbeddingItems ----------------------");
        SearchByEmbeddingItemsParam searchByEmbeddingItemsParam = SearchByEmbeddingItemsParam.newBuilder()
                .withEmbeddingItems(
                        extractToEmbeddingTextList(
                                documents,
                                new HashSet<String>() {{
                                    add("00001");
                                }},
                                "text"
                        )
                ).withParams(new HNSWSearchParams(16))
                .withLimit(5)
                .build();
        List<List<Document>> seDocs = collection.searchByEmbeddingItems(searchByEmbeddingItemsParam);
        for (List<Document> docs : seDocs) {
            for (Document doc : docs) {
                System.out.println("\tres: " + doc.toString());
            }
        }
        // search by filter, only return "00009", other rows will be filtered
        System.out.println("-searchByFilter----------------------");
        SearchByIdParam searchByFilterParam = SearchByIdParam.newBuilder()
                .withDocumentIds(Arrays.asList("00001", "00009"))
                .withFilter(new Filter("otherStr=\"other_filter\""))
                .withParams(new HNSWSearchParams(16))
                .withRetrieveVector(false)
                .withLimit(10)
                .build();
        List<List<Document>> sfDocs = collection.searchById(searchByFilterParam);
        for (List<Document> docs : sfDocs) {
            for (Document doc : docs) {
                System.out.println("\tres: " + doc.toString());
            }
        }
        // delete, only "00009" delete success, "00001" and "00002" will be excluded by filter
        System.out.println("- delete ----------------------");
        DeleteParam deleteParam = DeleteParam.newBuilder()
                .withDocumentIds(Arrays.asList("00001", "00002", "00009"))
                .withFilter(new Filter("otherStr=\"other_filter\""))
                .build();
        collection.delete(deleteParam);

        System.out.println("- describe collection after delete ----------------------");
        Collection descCollRes05 = db.describeCollection(collName);
        System.out.println("\tres: " + descCollRes05.toString());

        // notice：delete操作可用会有延迟
        Thread.sleep(1000 * 5);


        // query "00001", "00002", "00009", only return "00001", "00002", because "00009" is deleted
        // return specified field by outputFields
        System.out.println("- query after delete ----------------------");
        QueryParam queryParam01 = QueryParam.newBuilder()
                .withDocumentIds(Arrays.asList("00001", "00002", "00009"))
                .withOutputFields(Arrays.asList("id", "text", "authorParam"))
                .build();
        List<Document> qdos2 = collection.query(queryParam01);
        for (Document doc : qdos2) {
            System.out.println("\tres: " + doc.toString());
        }

        // rebuild index
        System.out.println("- rebuild index ----------------------");
        RebuildIndexParam rebuildIndexParam = RebuildIndexParam
                .newBuilder()
                .withDropBeforeRebuild(false)
                .withThrottle(1)
                .build();
        collection.rebuildIndex(rebuildIndexParam);
        Thread.sleep(5 * 1000);

        System.out.println("- describe collection after rebuild index----------------------");
        Collection descCollRes06 = db.describeCollection(collName);
        System.out.println("\tres: " + descCollRes06.toString());

        System.out.println("- truncate collection ----------------------");
        AffectRes affectRes = db.truncateCollections(collName);
        System.out.println("\tres: " + affectRes.toString());

        Thread.sleep(5 * 1000);

        System.out.println("- list collection after truncate collection ----------------------");
        List<Collection> collections = db.listCollections();
        for (Collection coll : collections) {
            System.out.println("\tres: " + coll.toString());

        }

        System.out.println("- clear ----------------------");
        client.dropDatabase("vdb001");
    }

    public static void main(String[] args) throws InterruptedException {
        // 创建VectorDB Client
        ConnectParam connectParam = initConnectParam();
        VectorDBClient client = new VectorDBClient(connectParam);
        // Database相关示例
        System.out.println("------------------------- testDatabases start -------------------------");
        testDatabases(client);
        System.out.println("------------------------- testDatabases end -------------------------");


        // Collection相关示例
        System.out.println("------------------------- testCollection start -------------------------");
        testCollection(client);
        System.out.println("------------------------- testCollection end -------------------------");

        System.out.println("------------------------- testCollectionEmbedding start -------------------------");
        testCollectionEmbedding(client);
        System.out.println("------------------------- testCollectionEmbedding end -------------------------");


        // Document相关示例
        System.out.println("------------------------- testDocument start -------------------------");
        testDocument(client);
        System.out.println("------------------------- testDocument end -------------------------");

        System.out.println("------------------------- testDocumentEmbedding start -------------------------");
        testDocumentEmbedding(client);
        System.out.println("------------------------- testDocumentEmbedding end -------------------------");


        // Filter示例
        System.out.println("------------------------- testFilter start -------------------------");
        testFilter();
        System.out.println("------------------------- testFilter end -------------------------");
    }

    public static void testFilter() {
        System.out.println("\tres: " + new Filter("author=\"jerry\"")
                .and("a=1")
                .or("r=\"or\"")
                .orNot("rn=2")
                .andNot("an=\"andNot\"")
                .getCond());
        System.out.println("\tres: " + Filter.in("key", Arrays.asList("v1", "v2", "v3")));
        System.out.println("\tres: " + Filter.in("key", Arrays.asList(1, 2, 3)));
    }

    private static void testDocument() {

        ConnectParam connectParam = initConnectParam();
        VectorDBClient client = new VectorDBClient(connectParam);
        List<String> databaseList = client.listDatabase();
        if (!databaseList.contains(VDB_XLZ_DEV_DATABASE)) {
            client.createDatabase(VDB_XLZ_DEV_DATABASE);
        }

        Database database = client.database(VDB_XLZ_DEV_DATABASE);
        List<Collection> collectionList = database.listCollections();
        if (!collectionList.stream().anyMatch(coll -> coll.getCollection().equals(VDB_XLZ_DEV_COLLECTION))) {
            CreateCollectionParam collectionParam = initCollectionParam();
            database.createCollection(collectionParam);
        }

        Collection collection = database.collection(VDB_XLZ_DEV_COLLECTION);
        InsertParam insertParam = initInsertParam();
        System.out.println("testDocument - insertParam: " + JSONUtil.toJSONString(insertParam));
        collection.upsert(insertParam);
        System.out.println("testDocument - insertParam: " + JSONUtil.toJSONString(insertParam));
        collection.upsert(insertParam);
        System.out.println("testDocument - insert finish");

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException ignore) {
            throw new RuntimeException(ignore);
        }
        QueryParam queryParam = initQueryParam(insertParam);
        System.out.println("testDocument - queryParam: " + JSONUtil.toJSONString(queryParam));
        List<Document> queryRes = collection.query(queryParam);
        System.out.println("testDocument - queryRes: " + toJsonString(queryRes));

        SearchByVectorParam searchParam = initSearchParam(insertParam);
        System.out.println("testDocument - searchParam: " + JSONUtil.toJSONString(searchParam));
        List<List<Document>> searchRes = collection.search(searchParam);
        System.out.println("testDocument - searchRes: " + toJsonString(searchRes));

        DeleteParam deleteParam = initDeleteParam(insertParam);
        System.out.println("testDocument - deleteParam: " + JSONUtil.toJSONString(deleteParam));
        collection.delete(deleteParam);

        System.out.println("testDocument - drop database");
        client.dropDatabase(VDB_XLZ_DEV_DATABASE);

    }

    static <T> String toJsonString(java.util.List<T> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            if (t instanceof java.util.List) {
                String subStr = toJsonString((java.util.List) t);
                sb.append(subStr);
            } else {
                sb.append(t.toString());
            }
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        return sb.append("]").toString();

    }


    static DeleteParam initDeleteParam(InsertParam insertParam) {
        DeleteParam.Builder builder = DeleteParam.newBuilder();
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
                Document document = insertParam.getDocuments().stream().filter(
                        dc -> dc.getId().equals(s)).findFirst().orElse(null);
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
        while ((insertCount = randmo.nextInt(1000)) < 100) {
            // ensure 100 documents
            // batch upsert size must between 1 and 1000
        }

        for (int i = 0; i < insertCount; i++) {
            Document.Builder documentB = Document.newBuilder();
            documentB.withId(formatId(8, i)).withVector(vectors(10))
                    .addFilterField(new DocField("sc", "sc" + i));
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
        builder.withDescription("xzl-test").withName(VDB_XLZ_DEV_COLLECTION).withReplicaNum(2).withShardNum(2);
        IndexField field1 = new IndexField();
        field1.setIndexType(IndexType.PRIMARY_KEY);
        field1.setFieldName("id");
        field1.setFieldType(FieldType.String);
        IndexField field2 = new VectorIndex("vector", 10, IndexType.HNSW, MetricType.L2,
                new HNSWParams(64, 64));
        IndexField field3 = new FilterIndex("sc", FieldType.String, IndexType.FILTER);
        builder.addField(field1).addField(field2).addField(field3);
        CreateCollectionParam build = builder.build();
        build.setDatabase(VDB_XLZ_DEV_DATABASE);
        return build;

    }

    private static ConnectParam initConnectParam() {
        System.out.println("\tres: " + System.getProperty("vdb_url"));
        return ConnectParam.newBuilder()
                .withUrl(System.getProperty("vdb_url"))
                .withUsername("root")
                .withKey(System.getProperty("vdb_key"))
                .withTimeout(30)
                .build();
    }

    private static void anySafe(Runnable runnable) {
        try {
            runnable.run();
        } catch (VectorDBException e) {
            System.err.println(e);
        }
    }

    private static CreateCollectionParam initCreateCollectionParam(String collName) {
        return CreateCollectionParam.newBuilder()
                .withName(collName)
                .withShardNum(3)
                .withReplicaNum(2)
                .withDescription("test collection0")
                .addField(new FilterIndex("id", FieldType.String, IndexType.PRIMARY_KEY))
                .addField(new VectorIndex("vector", 3, IndexType.HNSW,
                        MetricType.L2, new HNSWParams(16, 200)))
                .addField(new FilterIndex("otherStr", FieldType.String, IndexType.FILTER))
                .addField(new FilterIndex("otherInt", FieldType.Uint64, IndexType.FILTER))
                .build();
    }

    private static CreateCollectionParam initCreateEmbeddingCollectionParam(String collName) {
        return CreateCollectionParam.newBuilder()
                .withName(collName)
                .withShardNum(3)
                .withReplicaNum(2)
                .withDescription("test collection0")
                .addField(new FilterIndex("id", FieldType.String, IndexType.PRIMARY_KEY))
                .addField(new VectorIndex("vector", 768, IndexType.HNSW,
                        MetricType.L2, new HNSWParams(16, 200)))
                .addField(new FilterIndex("otherStr", FieldType.String, IndexType.FILTER))
                .addField(new FilterIndex("otherInt", FieldType.Uint64, IndexType.FILTER))
                .withEmbedding(
                        Embedding
                                .newBuilder()
                                .withModel(M3E_BASE)
                                .withField("text")
                                .withVectorField("vector")
                                .build()
                ).build();
    }

    public static String randomText(int length) {
        StringBuilder res = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            StringBuilder subRes = new StringBuilder();

            int wordLen = random.nextInt(14) + 2;
            for (int j = 0; j < wordLen; j++) {
                int randomInt = random.nextInt(26);
                subRes.append((char) (97 + randomInt));
            }

            res.append(subRes).append(" ");
        }

        return res.toString();
    }

    public static List<Document> initDocuments(boolean embeddingMode, int count, int dimension, int idStart) {
        List<Document> res = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            // id index
            String idParam = String.format("%05d", i + idStart);

            // vector
            List<Double> vectorParam = new ArrayList<>();

            if (!embeddingMode) {
                for (int j = 0; j < dimension; j++) {
                    vectorParam.add(random.nextDouble());
                }
            }

            // other index
            String otherStr = "other";
            if ((i % 10) == 9) {
                otherStr = "other_filter";
            }

            // embedding
            String text = "";
            if (embeddingMode) {
                text = randomText(random.nextInt(1) + 5);
            }

            // extend content
            String extendContext1 = "extend_context1";
            String authorParam = "author " + randomText(1);
            String sectionParam = "1.1." + (i + idStart);

            Document.Builder builder = Document.newBuilder()
                    .withId(idParam)
                    .addFilterField(new DocField("otherStr", otherStr))
                    .addFilterField(new DocField("otherInt", i + idStart))
                    .addFilterField(new DocField("extendContext1", extendContext1))
                    .addFilterField(new DocField("authorParam", authorParam))
                    .addFilterField(new DocField("sectionParam", sectionParam));

            if (embeddingMode) {
                builder.addFilterField(new DocField("text", text));
            } else {
                builder.withVector(vectorParam);
            }

            res.add(builder.build());
        }

        return res;
    }

    @NotNull
    private static List<String> extractToEmbeddingTextList(List<Document> documents,
                                                           Set<String> saveDoc,
                                                           String saveDocFieldName) {
        return documents
                .stream()
                .filter(dc -> saveDoc.contains(dc.getId()))
                .map(dc -> dc.getOtherFilterFields()
                        .stream()
                        .filter(off -> off.getName().equals(saveDocFieldName))
                        .map(docField -> docField.getValue().toString())
                        .collect(Collectors.toList())).flatMap(List::stream)
                .collect(Collectors.toList());
    }

}
