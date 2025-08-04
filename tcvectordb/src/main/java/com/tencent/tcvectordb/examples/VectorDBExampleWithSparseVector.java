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
import com.tencent.tcvdbtext.encoder.SparseVectorBm25Encoder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

import static com.tencent.tcvectordb.model.param.enums.EmbeddingModelEnum.BGE_BASE_ZH;

/**
 * VectorDB Java SDK usage example
 */
public class VectorDBExampleWithSparseVector {

    private static final String DBNAME = "db-test-sparse-vec";
    private static final String COLL_NAME = "coll-sparse-vec";
    private static final String COLL_NAME_ALIAS = "collection_alias_sparse";

    public static void main(String[] args) throws InterruptedException {
        // 创建VectorDB Client
        VectorDBClient client = CommonService.initClient();

        // 清理环境
        CommonService.anySafe(() -> client.dropDatabase(DBNAME));

        // 测试
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
//        Database db = client.database(DBNAME);

        // 3. 创建 collection
        System.out.println("---------------------- createCollection ----------------------");
        CreateCollectionParam collectionParam = initCreateCollectionParam(COLL_NAME);
        client.createCollection(DBNAME, collectionParam);

        System.out.println("---------------------- describeCollection ----------------------");
        Collection descCollRes = client.describeCollection(DBNAME, COLL_NAME);
        System.out.println("\tres: " + descCollRes.toString());

    }

    private static List<Double> generateRandomVector(int dim){
        Random random = new Random();
        List<Double> vectors = new ArrayList<>();

        for (int i = 0; i < dim; i++) {
            double randomDouble = 0 + random.nextDouble() * (1.0 - 0.0);
            vectors.add(randomDouble);
        }
        return vectors;
    }

    private static void upsertData(VectorDBClient client) throws InterruptedException {
        Database database = client.database(DBNAME);
        Collection collection = client.describeCollection(DBNAME, COLL_NAME);
        SparseVectorBm25Encoder encoder = SparseVectorBm25Encoder.getBm25Encoder("zh");
        List<String> texts = Arrays.asList(
                "腾讯云向量数据库（Tencent Cloud VectorDB）是一款全托管的自研企业级分布式数据库服务",
                "腾讯云向量数据库可以和大语言模型 LLM 配合使用"
        );

        List<List<Pair<Long, Float>>> sparseVectors = encoder.encodeTexts(texts);
        List<Document> documentList = new ArrayList<>(Arrays.asList(
                Document.newBuilder()
                        .withId("0001")
                        .withVector(generateRandomVector(768))
                        .withSparseVector(sparseVectors.get(0))
                        .build(),
                Document.newBuilder()
                        .withId("0002")
                        .withVector(generateRandomVector(768))
                        .withSparseVector(sparseVectors.get(1))
                        .build()));
        System.out.println("---------------------- upsert ----------------------");
        InsertParam insertParam = InsertParam.newBuilder()
                .addAllDocument(documentList)
                .withBuildIndex(true)
                .build();
        client.upsert(DBNAME, COLL_NAME, insertParam);

        // notice：upsert操作可用会有延迟
        Thread.sleep(1000 * 3);
    }

    private static void queryData(VectorDBClient client) {

        // search稀疏向量搜索和向量搜索混合
        System.out.println("---------------------- hybridSearch ----------------------");
        SparseVectorBm25Encoder encoder = SparseVectorBm25Encoder.getBm25Encoder("zh");
        HybridSearchParam hybridSearchParam = HybridSearchParam.newBuilder()
                .withAnn(AnnOption.newBuilder().withFieldName("vector")
                        .withData(generateRandomVector(768))
                        .build())
                .withMatch(MatchOption.newBuilder().withFieldName("sparse_vector")
                        .withData(encoder.encodeQueries(Arrays.asList("向量数据库")))
                        .build())
                // 指定 Top K 的 K 值
                .withRerank(new WeightRerankParam(Arrays.asList("vector","sparse_vector"), Arrays.asList(1, 1)))
                .withLimit(10)
                // 过滤获取到结果
//                .withFilter(filterParam)
                .withRetrieveVector(true)
                .build();
        List<Document> siDocs = client.hybridSearch(DBNAME, COLL_NAME, hybridSearchParam).getDocuments();
        int i = 0;
        for (Object docs : siDocs) {
            System.out.println("\tres: " + (i++) + docs.toString());
//            for (Document doc : (List<Document>)docs) {
//                System.out.println("\tres: " + doc.toString());
//            }
        }
    }

    private static void deleteAndDrop(VectorDBClient client) {

        // 删除 collection
        System.out.println("---------------------- dropCollection ----------------------");
        client.dropCollection(DBNAME, COLL_NAME);

        // 删除 database
        System.out.println("---------------------- dropDatabase ----------------------");
        client.dropDatabase(DBNAME);
    }


    /**
     * 初始化创建 Collection 参数
     * 通过调用 addField 方法设计索引（不是设计 Collection 的结构）
     * <ol>
     *     <li>【重要的事】向量对应的文本字段不要建立索引，会浪费较大的内存，并且没有任何作用。</li>
     *     <li>【必须的索引】：主键id、向量字段 vector、稀疏向量sparse_vector 这两个字段目前是固定且必须的，参考下面的例子；</li>
     *     </li>
     * </ol>
     *
     * @param collName
     * @return
     */
    private static CreateCollectionParam initCreateCollectionParam(String collName) {
        return CreateCollectionParam.newBuilder()
                .withName(collName)
                .withShardNum(1)
                .withReplicaNum(1)
                .withDescription("test sparse collection0")
                .addField(new FilterIndex("id", FieldType.String, IndexType.PRIMARY_KEY))
                .addField(new VectorIndex("vector", BGE_BASE_ZH.getDimension(), IndexType.HNSW,
                        MetricType.IP, new HNSWParams(16, 200)))
                .addField(new SparseVectorIndex("sparse_vector", IndexType.INVERTED, MetricType.IP))
                .build();
    }

}

