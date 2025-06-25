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

package com.tencent.tcvectordb.examples;

import com.tencent.tcvectordb.client.VectorDBClient;
import com.tencent.tcvectordb.model.Collection;
import com.tencent.tcvectordb.model.Database;
import com.tencent.tcvectordb.model.DocField;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.collection.*;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.entity.BaseRes;
import com.tencent.tcvectordb.utils.BinaryUtils;
import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * VectorDB Java SDK usage example
 */
public class VectorDBWithBinaryVectorExample {

    private static final String DBNAME = "book_binary";
    private static final String COLL_NAME = "book__binary_collection";

    public static void main(String[] args) throws InterruptedException {

//         创建 VectorDB Client
        VectorDBClient client = CommonService.initClient();


        // 清理环境
        CommonService.anySafe(() -> client.dropDatabase(DBNAME));
        createDatabaseAndCollection(client);
        upsertData(client);
        queryData(client);
        deleteAndDrop(client);
        client.close();
    }


    private static void createDatabaseAndCollection(VectorDBClient client) {
        // 1. 创建数据库
        System.out.println("---------------------- createDatabase ----------------------");
        Database db = client.createDatabase(DBNAME);

        // 2. 列出所有数据库
        System.out.println("---------------------- listCollections ----------------------");
        List<String> database = client.listDatabase();
        for (String s : database) {
            System.out.println("\tres: " + s);
        }

        // 3. 创建 collection
        System.out.println("---------------------- createCollection ----------------------");
        CreateCollectionParam collectionParam = initCreateCollectionParam(COLL_NAME);
        db.createCollection(collectionParam);
        System.out.println(COLL_NAME + " exists: "+ db.IsExistsCollection(COLL_NAME));

    }


    private static void upsertData(VectorDBClient client) throws InterruptedException {
        Database database = client.database(DBNAME);
        Collection collection = database.describeCollection(COLL_NAME);
//        List<JSONObject> documentList = Arrays.asList(
//                new JSONObject("{\"id\":\"0013\",\"vector\":[0.2123, 0.21, 0.213],\"bookName\":\"三国演义\",\"author\":\"吴承恩\",\"page\":21,\"segment\":\"富贵功名，前缘分定，为人切莫欺心。\"}"),
//                new JSONObject("{\"id\":\"0014\",\"vector\":[0.2123, 0.21, 0.213],\"bookName\":\"三国演义\",\"author\":\"吴承恩\",\"page\":21,\"segment\":\"富贵功名，前缘分定，为人切莫欺心。\"}")
//        );
        List<Document> documentList = new ArrayList<>(Arrays.asList(
                Document.newBuilder()
                        .withId("0001")
                        .withVector(BinaryUtils.binaryToUint8(Arrays.asList(1, 1, 1, 0, 0, 1, 0, 1,  0, 0, 0, 0, 0, 0, 0, 0)))
                        .addDocField(new DocField("bookName", "西游记"))
                        .addDocField(new DocField("author", "吴承恩"))
                        .build(),
                Document.newBuilder()
                        .withId("0002")
                        .withVector(BinaryUtils.binaryToUint8(Arrays.asList(1, 1, 0, 0, 0, 1, 0, 1,  0, 0, 0, 0, 0, 0, 0, 0)))
                        .addDocField(new DocField("bookName", "西游记"))
                        .addDocField(new DocField("author", "吴承恩"))
                        .build(),
                Document.newBuilder()
                        .withId("0003")
                        .withVector(BinaryUtils.binaryToUint8(Arrays.asList(0, 1, 0, 0, 0, 1, 0, 1,  0, 0, 0, 0, 0, 0, 0, 0)))
                        .addDocField(new DocField("bookName", "三国演义"))
                        .addDocField(new DocField("author", "罗贯中"))
                        .build(),
                Document.newBuilder()
                        .withId("0004")
                        .withVector(BinaryUtils.binaryToUint8(Arrays.asList(0, 1, 0, 0, 0, 1, 0, 1,  0, 0, 0, 0, 0, 1, 0, 0)))
                        .addDocField(new DocField("bookName", "三国演义"))
                        .addDocField(new DocField("author", "罗贯中"))
                        .build(),
                Document.newBuilder()
                        .withId("0005")
                        .withVector(BinaryUtils.binaryToUint8(Arrays.asList(0, 1, 0, 0, 0, 1, 0, 1,  0, 0, 0, 0, 0, 1, 1, 0)))
                        .addDocField(new DocField("bookName", "三国演义"))
                        .addDocField(new DocField("author", "罗贯中"))
                        .build()));
        System.out.println("---------------------- upsert ----------------------");
        InsertParam insertParam = InsertParam.newBuilder().withDocuments(documentList).build();

//        collection.upsert(insertParam);
        AffectRes affectRes = client.upsert(DBNAME,COLL_NAME, insertParam);
        System.out.println(JsonUtils.toJsonString(affectRes));
        // notice：upsert 操作可用会有延迟
        Thread.sleep(1000 * 5);
    }

    private static void queryData(VectorDBClient client) {
        Database database = client.database(DBNAME);
        Collection collection = database.describeCollection(COLL_NAME);

        System.out.println("---------------------- query ----------------------");
        List<String> documentIds = Arrays.asList("0001", "0002", "0003", "0004", "0005");
        List<String> outputFields = Arrays.asList("id", "bookName");
        QueryParam queryParam = QueryParam.newBuilder()
                .withDocumentIds(Arrays.asList("0001", "0002", "0003", "0004", "0005"))
                // limit 限制返回行数，1 到 16384 之间
                 .withLimit(5)
                .withFilter("bookName=\"三国演义\"")
                // 偏移
                 .withOffset(0)
                // 指定返回的 fields
                .addAllOutputFields("id", "bookName")
                // 是否返回 vector 数据
                .withRetrieveVector(true)
                .build();
        List<Document> qdos = collection.query(queryParam);
        for (Document doc : qdos) {
            System.out.println("\tres: " + doc.toString());
        }


        // searchById
        // 1. searchById 提供按 id 搜索的能力
        // 2. 支持通过 filter 过滤数据
        // 3. 如果仅需要部分 field 的数据，可以指定 output_fields 用于指定返回数据包含哪些 field，不指定默认全部返回
        // 4. limit 用于限制每个单元搜索条件的条数，如 vector 传入三组向量，limit 为 3，则 limit 限制的是每组向量返回 top 3 的相似度向量

        System.out.println("---------------------- searchById ----------------------");
        SearchByIdParam searchByIdParam = SearchByIdParam.newBuilder()
                .withDocumentIds(Arrays.asList("0001"))
                // 若使用 HNSW 索引，则需要指定参数 ef，ef 越大，召回率越高，但也会影响检索速度
                .withParams(new HNSWSearchParams(100))
                // 指定 Top K 的 K 值
                .withLimit(2)
                .build();
        List<List<Document>> siDocs = client.searchById(DBNAME, COLL_NAME, searchByIdParam);
        int i = 0;
        for (List<Document> docs : siDocs) {
            System.out.println("\tres: " + i++);
            for (Document doc : docs) {
                System.out.println("\tres: " + doc.toString());
            }
        }


        // search
        // 1. search 提供按照 vector 搜索的能力
        // 其他选项类似 search 接口
        System.out.println("---------------------- search ----------------------");
        SearchByVectorParam searchByVectorParam = SearchByVectorParam.newBuilder()
                .addVector(BinaryUtils.binaryToUint8(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0,  1, 1, 0, 1, 1, 1, 0, 1)))
                // 若使用 HNSW 索引，则需要指定参数ef，ef越大，召回率越高，但也会影响检索速度
                .withParams(new HNSWSearchParams(100))
                // 指定 Top K 的 K 值
                .withLimit(10)
                .build();
        // 输出相似性检索结果，检索结果为二维数组，每一位为一组返回结果，分别对应 search 时指定的多个向量
        List<List<Document>> svDocs = client.search(DBNAME, COLL_NAME, searchByVectorParam);
        i = 0;
        for (List<Document> docs : svDocs) {
            System.out.println("\tres: " + i);
            i++;
            for (Document doc : docs) {
                System.out.println("\tres: " + doc.toString());
            }
        }
    }

    private static void deleteAndDrop(VectorDBClient client) {
        Database database = client.database(DBNAME);

        // 删除 collection
        System.out.println("---------------------- truncate collection ----------------------");
        client.dropCollection(DBNAME, COLL_NAME);

        // 删除 database
        System.out.println("---------------------- drop database ----------------------");
        client.dropDatabase(DBNAME);
    }

    private static CreateCollectionParam initCreateCollectionParam(String collName) {

        return CreateCollectionParam.newBuilder()
                .withName(collName)
                .withShardNum(1)
                .withReplicaNum(1)
                .withDescription("test binary collection")
                .addField(new FilterIndex("id", FieldType.String, IndexType.PRIMARY_KEY))
                .addField(new VectorIndex("vector", 16, FieldType.BinaryVector, IndexType.BIN_FLAT,
                        MetricType.HAMMING))
                .withFilterIndexConfig(FilterIndexConfig.newBuilder()
                        .withFilterAll(true)
                        .withFieldWithoutFilterIndex(Arrays.asList("test1", "test2"))
                        .withMaxStrLen(64)
                        .build())
                .build();
    }
}
