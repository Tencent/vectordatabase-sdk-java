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
public class VectorDBWithHNSWQuantizationExample {

    private static final String DBNAME = "java_sdk_collection_hnsw_quantization";
    private static final String COLL_NAME_FLOAT16 = "sdk_collection_float16";
    private static final String COLL_NAME_BFLOAT16 = "sdk_collection_bfloat16";
    public static void main(String[] args) throws InterruptedException {

//         创建 VectorDB Client
        VectorDBClient client = CommonService.initClient();

        createDatabaseAndCollection(client);
        upsertData(client,  COLL_NAME_FLOAT16);
        queryData(client, COLL_NAME_FLOAT16);
        upsertData(client,  COLL_NAME_BFLOAT16);
        queryData(client, COLL_NAME_BFLOAT16);
        modifyVectorIndex(client);
        deleteAndDrop(client);

    }


    private static void createDatabaseAndCollection(VectorDBClient client) {
        // 1. 创建数据库
        System.out.println("---------------------- createDatabase ----------------------");
        Database db = client.createDatabaseIfNotExists(DBNAME);

        // 2. 列出所有数据库
        System.out.println("---------------------- listCollections ----------------------");
        List<String> database = client.listDatabase();
        for (String s : database) {
            System.out.println("\tres: " + s);
        }

        // 3. 创建 collection
        System.out.println("---------------------- create Float16 Collection ----------------------");
        CreateCollectionParam collectionParam = initCreateCollectionParamFloat16(COLL_NAME_FLOAT16);
        client.createCollectionIfNotExists(DBNAME, collectionParam);
        System.out.println(COLL_NAME_FLOAT16 + " collection: "+ client.describeCollection(DBNAME,COLL_NAME_FLOAT16));

        System.out.println("---------------------- create BFloat16 Collection ----------------------");
        CreateCollectionParam collectionBFloat16Param = initCreateCollectionParamBFloat16(COLL_NAME_BFLOAT16);
        client.createCollectionIfNotExists(DBNAME, collectionBFloat16Param);
        System.out.println(COLL_NAME_BFLOAT16 + " collection: "+ client.describeCollection(DBNAME,COLL_NAME_BFLOAT16));

    }

    private static CreateCollectionParam initCreateCollectionParamFloat16(String collNameFloat16) {
        return CreateCollectionParam.newBuilder()
                .withName(collNameFloat16)
                .withShardNum(1)
                .withReplicaNum(1)
                .withDescription("test float16 collection")
                .addField(new FilterIndex("id", FieldType.String, IndexType.PRIMARY_KEY))
                .addField(new VectorIndex("vector", 32, FieldType.Float16Vector, IndexType.HNSW,
                        MetricType.IP, new HNSWParams(16, 200)))
                .addField(new FilterIndex("author", FieldType.String, IndexType.FILTER))
                .build();
    }

    private static CreateCollectionParam initCreateCollectionParamBFloat16(String collNameBFloat16) {
        return CreateCollectionParam.newBuilder()
                .withName(collNameBFloat16)
                .withShardNum(1)
                .withReplicaNum(1)
                .withDescription("test bfloat16 collection")
                .addField(new FilterIndex("id", FieldType.String, IndexType.PRIMARY_KEY))
                .addField(new VectorIndex("vector", 32, FieldType.BFloat16Vector, IndexType.HNSW,
                        MetricType.IP, new HNSWParams(16, 200)))
                .addField(new FilterIndex("author", FieldType.String, IndexType.FILTER))
                .build();
    }

    /**
     * modifyVectorIndex test
     * @param client
     * @throws InterruptedException
     */
    private static void modifyVectorIndex(VectorDBClient client) throws InterruptedException{
        System.out.println("--------modify vector index collection: COLL_NAME_BFLOAT16 -------");
        Collection collection = client.describeCollection(DBNAME, COLL_NAME_BFLOAT16);
        System.out.println("before");
        System.out.println(JsonUtils.toJsonString(collection));
        BaseRes baseRes = client.modifyVectorIndex(DBNAME, COLL_NAME_BFLOAT16, ModifyVectorIndexParam.newBuilder()
                .withVectorIndex(new VectorIndex(MetricType.IP, FieldType.Float16Vector, new HNSWParams(16, 200)))
                .withRebuildRules(RebuildIndexParam.newBuilder().withDropBeforeRebuild(true).withThrottle(1).build())
                .build());
        System.out.println("modify res: "+ JsonUtils.toJsonString(baseRes));
        Collection collectionAfter = client.describeCollection(DBNAME, COLL_NAME_BFLOAT16);
        System.out.println("after");
        System.out.println(JsonUtils.toJsonString(collectionAfter));

        System.out.println("--------modify vector index collection: COLL_NAME_BFLOAT16 -------");
        collection = client.describeCollection(DBNAME, COLL_NAME_FLOAT16);
        System.out.println("before");
        System.out.println(JsonUtils.toJsonString(collection));
        baseRes = client.modifyVectorIndex(DBNAME, COLL_NAME_BFLOAT16, ModifyVectorIndexParam.newBuilder()
                .withVectorIndex(new VectorIndex(MetricType.IP, FieldType.BFloat16Vector, new HNSWParams(16, 200)))
                .withRebuildRules(RebuildIndexParam.newBuilder().withDropBeforeRebuild(true).withThrottle(1).build())
                .build());
        System.out.println("modify res: "+ JsonUtils.toJsonString(baseRes));
        collectionAfter = client.describeCollection(DBNAME, COLL_NAME_BFLOAT16);
        System.out.println("after");
        System.out.println(JsonUtils.toJsonString(collectionAfter));

    }

    private static void upsertData(VectorDBClient client, String collectionName) throws InterruptedException {
        Database database = client.database(DBNAME);
        List<Document> documentList = new ArrayList<>(Arrays.asList(
                Document.newBuilder()
                        .withId("0001")
                        .withVector(CommonService.generateRandomVector(32))
                        .addDocField(new DocField("author", "吴承恩"))
                        .build(),
                Document.newBuilder()
                        .withId("0002")
                        .withVector(CommonService.generateRandomVector(32))
                        .addDocField(new DocField("author", "吴承恩"))
                        .build(),
                Document.newBuilder()
                        .withId("0003")
                        .withVector(CommonService.generateRandomVector(32))
                        .addDocField(new DocField("author", "吴承恩"))
                        .build(),
                Document.newBuilder()
                        .withId("0004")
                        .withVector(CommonService.generateRandomVector(32))
                        .addDocField(new DocField("author", "罗贯中"))
                        .build(),
                Document.newBuilder()
                        .withId("0005")
                        .withVector(CommonService.generateRandomVector(32))
                        .addDocField(new DocField("author", "罗贯中"))
                        .build()));
        System.out.println("---------------------- upsert ----------------------");
        InsertParam insertParam = InsertParam.newBuilder().withDocuments(documentList).build();

//        collection.upsert(insertParam);
        AffectRes affectRes = client.upsert(DBNAME, collectionName, insertParam);
        System.out.println(JsonUtils.toJsonString(affectRes));

        affectRes = client.upsert(DBNAME, collectionName, insertParam);
        System.out.println(JsonUtils.toJsonString(affectRes));
    }

    private static void queryData(VectorDBClient client, String collectionName) {
        Database database = client.database(DBNAME);
//        Collection collection = database.describeCollection(collectionName);

        System.out.println("---------------------- query ----------------------");
        QueryParam queryParam = QueryParam.newBuilder()
                .withDocumentIds(Arrays.asList("0001", "0002", "0003", "0004", "0005"))
                // limit 限制返回行数，1 到 16384 之间
                 .withLimit(5)
                .withFilter("author=\"吴承恩\"")
                // 偏移
                 .withOffset(0)
                // 指定返回的 fields
                .addAllOutputFields("id", "author")
                // 是否返回 vector 数据
                .withRetrieveVector(true)
                .build();
        List<Document> qdos = client.query(DBNAME,collectionName, queryParam);
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
        List<List<Document>> siDocs = client.searchById(DBNAME, collectionName, searchByIdParam);
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
                .addVector(CommonService.generateRandomVector(32))
                // 若使用 HNSW 索引，则需要指定参数ef，ef越大，召回率越高，但也会影响检索速度
                .withParams(new HNSWSearchParams(100))
                // 指定 Top K 的 K 值
                .withLimit(10)
                .build();
        // 输出相似性检索结果，检索结果为二维数组，每一位为一组返回结果，分别对应 search 时指定的多个向量
        List<List<Document>> svDocs = client.search(DBNAME, collectionName, searchByVectorParam);
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
        // 删除 database
        System.out.println("---------------------- drop database ----------------------");
        client.dropDatabase(DBNAME);
    }
}
