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

import com.tencent.tcvdbtext.encoder.Bm25Parameter;
import com.tencent.tcvdbtext.encoder.SparseVectorBm25Encoder;
import com.tencent.tcvectordb.client.VectorDBClient;
import com.tencent.tcvectordb.model.Collection;
import com.tencent.tcvectordb.model.Database;
import com.tencent.tcvectordb.model.DocField;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.collection.*;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.entity.BaseRes;
import com.tencent.tcvectordb.model.param.entity.FullTextSearchRes;
import com.tencent.tcvectordb.model.param.enums.EmbeddingModelEnum;
import com.tencent.tcvectordb.utils.JsonUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * VectorDB Java SDK usage example
 */
public class VectorDBWithFullTextSearchExample {

    private static final String DBNAME = "java_sdk_full_search_test_db";
    private static final String COLL_NAME = "java_sdk_full_search_coll";

    private static final SparseVectorBm25Encoder bm25Encoder = SparseVectorBm25Encoder.getDefaultBm25Encoder();;

    public static void main(String[] args) throws InterruptedException {

//         创建 VectorDB Client
        VectorDBClient client = CommonService.initClient();

        // 清理环境
        CommonService.anySafe(() -> client.dropDatabase(DBNAME));
        createDatabaseAndCollection(client);
        upsertData(client);
        queryData(client);
        searchData(client);
        rebuild(client);
        deleteAndDrop(client);

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
        client.createCollectionIfNotExists(DBNAME, collectionParam);
        System.out.println(COLL_NAME + " exists: "+ client.IsExistsCollection(DBNAME, COLL_NAME));

    }


    private static void upsertData(VectorDBClient client) throws InterruptedException {
        List<String> texts = Arrays.asList(
                "腾讯云向量数据库（Tencent Cloud VectorDB）是一款全托管的自研企业级分布式数据库服务，专用于存储、索引、检索、管理由深度神经网络或其他机器学习模型生成的大量多维嵌入向量。",
                "作为专门为处理输入向量查询而设计的数据库，它支持多种索引类型和相似度计算方法，单索引支持10亿级向量规模，高达百万级 QPS 及毫秒级查询延迟。",
                "不仅能为大模型提供外部知识库，提高大模型回答的准确性，还可广泛应用于推荐系统、NLP 服务、计算机视觉、智能客服等 AI 领域。",
                "腾讯云向量数据库（Tencent Cloud VectorDB）作为一种专门存储和检索向量数据的服务提供给用户， 在高性能、高可用、大规模、低成本、简单易用、稳定可靠等方面体现出显著优势。 ",
                "腾讯云向量数据库可以和大语言模型 LLM 配合使用。企业的私域数据在经过文本分割、向量化后，可以存储在腾讯云向量数据库中，构建起企业专属的外部知识库，从而在后续的检索任务中，为大模型提供提示信息，辅助大模型生成更加准确的答案。");
        List<List<Pair<Long, Float>>> sparseVectors = bm25Encoder.encodeTexts(texts);

        List<Document> documentList = new ArrayList<>(Arrays.asList(
                Document.newBuilder()
                        .withId("0001")
                        .addDocField(new DocField("text", texts.get(0)))
                        .withSparseVector(sparseVectors.get(0))
                        .build(),
                Document.newBuilder()
                        .withId("0002")
                        .withSparseVector(sparseVectors.get(1))
                        .addDocField(new DocField("text", texts.get(1)))
                        .build(),
                Document.newBuilder()
                        .withId("0003")
                        .withSparseVector(sparseVectors.get(2))
                        .addDocField(new DocField("text", texts.get(2)))
                        .build(),
                Document.newBuilder()
                        .withId("0004")
                        .withSparseVector(sparseVectors.get(3))
                        .addDocField(new DocField("text", texts.get(3)))
                        .build(),
                Document.newBuilder()
                        .withId("0005")
                        .withSparseVector(sparseVectors.get(4))
                        .addDocField(new DocField("text", texts.get(4)))
                        .build()));
        System.out.println("---------------------- upsert ----------------------");
        InsertParam insertParam = InsertParam.newBuilder().withDocuments(documentList).build();

//        collection.upsert(insertParam);
        AffectRes affectRes = client.upsert(DBNAME,COLL_NAME, insertParam);
        System.out.println(JsonUtils.toJsonString(affectRes));
        // notice：upsert 操作可用会有延迟
        Thread.sleep(1000 * 5);
    }

    private static void searchData(VectorDBClient client) {
        System.out.println("---------------------- full text search ----------------------");
        FullTextSearchParam fullTextSearchParam = FullTextSearchParam.newBuilder()
                .withMatch(MatchParam.newBuilder().withFieldName("sparse_vector")
                        .withData(bm25Encoder.encodeQueries(Arrays.asList("什么是腾讯云向量数据库")))
//                        .withCutoffFrequency(0.05)
//                        .withTerminateAfter(1)
                        .build())
                .withLimit(3)
                .withRetrieveVector(false)
//                .withOutputFields(Arrays.asList("id"))
                .build();
        FullTextSearchRes res= client.fullTextSearch(DBNAME, COLL_NAME, fullTextSearchParam);
        int i = 0;
        for (Object docs : res.getDocuments()) {
            System.out.println("\tres: " + (i++) + docs.toString());
        }

    }

    private static void queryData(VectorDBClient client){
        System.out.println("---------------------- query ----------------------");
        QueryParam queryParam = QueryParam.newBuilder()
                .withDocumentIds(Arrays.asList("0001", "0002", "0003", "0004", "0005"))
                // limit 限制返回行数，1 到 16384 之间
                .withLimit(5)
                // 偏移
                .withOffset(0)
                // 指定返回的 fields
//                .addAllOutputFields("id", "bookName")
                // 是否返回 vector 数据
                .withRetrieveVector(false)
                .build();
        List<Document> qdos = client.query(DBNAME, COLL_NAME, queryParam);
        for (Document doc : qdos) {
            System.out.println("\tres: " + doc.toString());
        }
    }

    private static void rebuild(VectorDBClient client) {
        System.out.println("---------------------- rebuild collection ----------------------");
        BaseRes res = client.rebuildIndex(DBNAME, COLL_NAME, RebuildIndexParam.newBuilder().
                withFieldName("sparse_vector").
                withDropBeforeRebuild(true).
//                withThrottle(0).
                build());
        System.out.println("rebuild response: " + JsonUtils.toJsonString(res));
    }

    private static void deleteAndDrop(VectorDBClient client) {
        Database database = client.database(DBNAME);

        // 删除 collection
        System.out.println("---------------------- truncate collection ----------------------");
        database.dropCollection(COLL_NAME);

        // 删除 database
        System.out.println("---------------------- delete database ----------------------");
        client.dropDatabase(DBNAME);
    }

    private static CreateCollectionParam initCreateCollectionParam(String collName) {

        return CreateCollectionParam.newBuilder()
                .withName(collName)
                .withShardNum(1)
                .withReplicaNum(1)
                .withDescription("test full text search collection")
                .addField(new FilterIndex("id", FieldType.String, IndexType.PRIMARY_KEY))
                .addField(new SparseVectorIndex("sparse_vector", IndexType.INVERTED, MetricType.IP))
                .withFilterIndexConfig(FilterIndexConfig.newBuilder().withFilterAll(true).build())
                .build();
    }
}
