/*
 * Copyright (C) 2025 Tencent Cloud.
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
import com.tencent.tcvectordb.model.param.entity.HybridSearchRes;
import com.tencent.tcvectordb.model.param.entity.SearchRes;
import com.tencent.tcvectordb.utils.JsonUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * VectorDB Java SDK usage example
 */
public class VectorDBWithIVFRABITQExample {

    private static final String DBNAME = "java_sdk_collection_ivf_rabitq";
    private static final String COLL_NAME = "sdk_collection_ivf_rabitq";
    public static void main(String[] args) throws InterruptedException {

//         创建 VectorDB Client
        VectorDBClient client = CommonService.initClient();

        createDatabaseAndCollection(client);
        upsertData(client);
        // queryData(client);
        modifyVectorIndex(client);
        deleteAndDrop(client);
        client.close();
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
        System.out.println("---------------------- createCollection ----------------------");
        CreateCollectionParam collectionParam = initCreateCollectionParam();
        client.createCollectionIfNotExists(DBNAME, collectionParam);
        System.out.println("---------------------- describe ----------------------");
        System.out.println("\r res: "+ client.describeCollection(DBNAME,COLL_NAME));
    }

    private static CreateCollectionParam initCreateCollectionParam() {
        return CreateCollectionParam.newBuilder()
                .withName(COLL_NAME)
                .withShardNum(1)
                .withReplicaNum(1)
                .withDescription("this is the collection description")
                .addField(new FilterIndex("id", FieldType.String, IndexType.PRIMARY_KEY))
                .addField(new VectorIndex("vector", 128, FieldType.Vector, IndexType.IVF_RABITQ,
                        MetricType.IP, new IVFRABITQParams(200, 1)))
                .addField(new SparseVectorIndex("sparse_vector", IndexType.INVERTED, MetricType.IP))
                .build();
    }

    private static void modifyVectorIndex(VectorDBClient client) throws InterruptedException{
        System.out.println("--------modify vector index collection:  -------");
        Collection collection = client.describeCollection(DBNAME, COLL_NAME);
        System.out.println("before");
        System.out.println("\r res :" + JsonUtils.toJsonString(collection));
        BaseRes baseRes = client.modifyVectorIndex(DBNAME, COLL_NAME, ModifyVectorIndexParam.newBuilder()
                .withVectorIndex(new VectorIndex("vector",MetricType.IP, FieldType.Vector,
                        IndexType.HNSW, new HNSWParams(32, 200)))
                .withRebuildRules(RebuildIndexParam.newBuilder().withDropBeforeRebuild(true).withThrottle(1).build())
                .build());
        System.out.println("modify res: "+ JsonUtils.toJsonString(baseRes));
        Collection collectionAfter = client.describeCollection(DBNAME, COLL_NAME);
        System.out.println("after");
        System.out.println("\r res : " + JsonUtils.toJsonString(collectionAfter));
    }

    private static void upsertData(VectorDBClient client) throws InterruptedException {
        SparseVectorBm25Encoder encoder = SparseVectorBm25Encoder.getBm25Encoder("zh");
        List<String> texts = Arrays.asList(
                "吴承恩",
                "张三",
                "李四",
                "王五",
                "孙六"
        );

        List<List<Pair<Long, Float>>> sparseVectors = encoder.encodeTexts(texts);

        List<Document> documentList = new ArrayList<>(Arrays.asList(
                Document.newBuilder()
                        .withId("0001")
                        .withVector(CommonService.generateRandomVector(128))
                        .addDocField(new DocField("author", "吴承恩"))
                        .withSparseVector(sparseVectors.get(0))
                        .build(),
                Document.newBuilder()
                        .withId("0002")
                        .withVector(CommonService.generateRandomVector(128))
                        .addDocField(new DocField("author", "张三"))
                        .withSparseVector(sparseVectors.get(1))
                        .build(),
                Document.newBuilder()
                        .withId("0003")
                        .withVector(CommonService.generateRandomVector(128))
                        .addDocField(new DocField("author", "李四"))
                        .withSparseVector(sparseVectors.get(2))
                        .build(),
                Document.newBuilder()
                        .withId("0004")
                        .withVector(CommonService.generateRandomVector(128))
                        .addDocField(new DocField("author", "王五"))
                        .withSparseVector(sparseVectors.get(3))
                        .build(),
                Document.newBuilder()
                        .withId("0005")
                        .withVector(CommonService.generateRandomVector(128))
                        .addDocField(new DocField("author", "孙六"))
                        .withSparseVector(sparseVectors.get(4))
                        .build()));
        System.out.println("---------------------- upsert ----------------------");
        InsertParam insertParam = InsertParam.newBuilder().withDocuments(documentList).withBuildIndex(false).build();

        AffectRes affectRes = client.upsert(DBNAME, COLL_NAME, insertParam);
        System.out.println("\r res :" + JsonUtils.toJsonString(affectRes));
    }

    private static void queryData(VectorDBClient client) {
        System.out.println("---------------------- searchById ----------------------");

        HybridSearchParam searchByIdParam = HybridSearchParam.newBuilder()
                .withAnn(Arrays.asList(AnnOption.newBuilder()
                        .withFieldName("vector")
                        .withData(Arrays.asList(0.22, 0.33, 0.44))
                        .withParam(new IVFRABITQSearchParams(200))
                        .withLimit(10)
                        .build()))
                .withMatch(Arrays.asList(MatchOption.newBuilder()
                                .withFieldName("sparse_vector")
//                                .withData(Arrays.asList(Arrays.asList(Arrays.asList(0, 1.0f)))) todo
                                .withLimit(10)
                                .withCutoffFrequency(0.1)
                                .withTerminateAfter(4000)
                                .build()))
                .withRerank(new WeightRerankParam(Arrays.asList("vector", "sparse_vector"), Arrays.asList(0.5, 0.5)))
                .withLimit(3)
                .withRetrieveVector(false)
                .build();
        HybridSearchRes hybridSearchRes = client.hybridSearch(DBNAME, COLL_NAME, searchByIdParam);
        System.out.println(hybridSearchRes.toString());
        List<Document> siDocs = hybridSearchRes.getDocuments();
        int i = 0;
        for (Object docs : siDocs) {
            System.out.println("\tres: " + (i++) + docs.toString());
        }
    }

    private static void deleteAndDrop(VectorDBClient client) {
        // 删除 database
        System.out.println("---------------------- drop database ----------------------");
        client.dropDatabase(DBNAME);
    }
}
