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
import com.tencent.tcvectordb.model.param.entity.SearchRes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.tencent.tcvectordb.model.param.enums.EmbeddingModelEnum.BGE_BASE_ZH;

/**
 * VectorDB Java SDK usage example
 */
public class VectorDBExampleWithEmbedding {

    private static final String DBNAME = "book";
    private static final String COLL_NAME = "book_segments_2";
    private static final String COLL_NAME_ALIAS = "collection_alias_2";

    public static void main(String[] args) throws Exception {

        // 创建 VectorDB Client
        VectorDBClient client = CommonService.initClient();

        // 清理环境
        CommonService.anySafe(() -> client.dropDatabase(DBNAME));
        createDatabaseAndCollection(client);
        upsertData(client);
        queryData(client);
        updateAndDelete(client);
        deleteAndDrop(client);
        testFilter();
    }

    private static void createDatabaseAndCollection(VectorDBClient client) {
        // 1. 创建数据库
        System.out.println("---------------------- createDatabase ----------------------");
        Database db = client.createDatabase(DBNAME);

        // 2. 列出所有数据库
        System.out.println("---------------------- listDatabase ----------------------");
        List<String> database = client.listDatabase();
        for (String s : database) {
            System.out.println("\tres: " + s);
        }

        // 3. 创建 collection
        System.out.println("---------------------- createCollection ----------------------");
        CreateCollectionParam collectionParam = initCreateEmbeddingCollectionParam(COLL_NAME);
        db.createCollection(collectionParam);

        // 4. 列出所有 collection
        System.out.println("---------------------- listCollections ----------------------");
        List<Collection> cols = db.listCollections();
        for (Collection col : cols) {
            System.out.println("\tres: " + col.toString());
        }

        // 5. 设置 collection 别名
        System.out.println("---------------------- setAlias ----------------------");
        AffectRes affectRes = db.setAlias(COLL_NAME, COLL_NAME_ALIAS);
        System.out.println("\tres: " + affectRes.toString());


        // 6. describe collection
        System.out.println("---------------------- describeCollection ----------------------");
        Collection descCollRes = db.describeCollection(COLL_NAME);
        System.out.println("\tres: " + descCollRes.toString());

        // 7. delete alias
        System.out.println("---------------------- deleteAlias ----------------------");
        AffectRes affectRes1 = db.deleteAlias(COLL_NAME_ALIAS);
        System.out.println("\tres: " + affectRes1);

        // 8. describe collection
        System.out.println("---------------------- describeCollection ----------------------");
        Collection descCollRes1 = db.describeCollection(COLL_NAME);
        System.out.println("\tres: " + descCollRes1.toString());

    }


    private static void upsertData(VectorDBClient client) throws InterruptedException {
        Database database = client.database(DBNAME);
        Collection collection = database.describeCollection(COLL_NAME);
        List<Document> documentList = new ArrayList<>(Arrays.asList(
                Document.newBuilder()
                        .withId("0001")
                        .addDocField(new DocField("bookName", "西游记"))
                        .addDocField(new DocField("author", "吴承恩"))
                        .addDocField(new DocField("page", 21))
                        .addDocField(new DocField("segment", "富贵功名，前缘分定，为人切莫欺心。"))
                        .addDocField(new DocField("text", "富贵功名，前缘分定，为人切莫欺心。"))
                        .build(),
                Document.newBuilder()
                        .withId("0002")
                        .addDocField(new DocField("bookName", "西游记"))
                        .addDocField(new DocField("author", "吴承恩"))
                        .addDocField(new DocField("page", 22))
                        .addDocField(new DocField("segment",
                                "正大光明，忠良善果弥深。些些狂妄天加谴，眼前不遇待时临。"))
                        .addDocField(new DocField("text",
                                "正大光明，忠良善果弥深。些些狂妄天加谴，眼前不遇待时临。"))
                        .build(),
                Document.newBuilder()
                        .withId("0003")
                        .addDocField(new DocField("bookName", "三国演义"))
                        .addDocField(new DocField("author", "罗贯中"))
                        .addDocField(new DocField("page", 23))
                        .addDocField(new DocField("segment", "细作探知这个消息，飞报吕布。"))
                        .addDocField(new DocField("text", "细作探知这个消息，飞报吕布。"))
                        .build(),
                Document.newBuilder()
                        .withId("0004")
                        .addDocField(new DocField("bookName", "三国演义"))
                        .addDocField(new DocField("author", "罗贯中"))
                        .addDocField(new DocField("page", 24))
                        .addDocField(new DocField("segment", "富贵功名，前缘分定，为人切莫欺心。"))
                        .addDocField(new DocField("text", "富贵功名，前缘分定，为人切莫欺心。"))
                        .build(),
                Document.newBuilder()
                        .withId("0005")
                        .addDocField(new DocField("bookName", "三国演义"))
                        .addDocField(new DocField("author", "罗贯中"))
                        .addDocField(new DocField("page", 25))
                        .addDocField(new DocField("segment",
                                "布大惊，与陈宫商议。宫曰：“闻刘玄德新领徐州，可往投之。"))
                        .addDocField(new DocField("text",
                                "布大惊，与陈宫商议。宫曰：“闻刘玄德新领徐州，可往投之。"))
                        .build()));
        System.out.println("---------------------- upsert ----------------------");
        InsertParam insertParam = InsertParam.newBuilder()
                .addAllDocument(documentList)
                .withBuildIndex(true)
                .build();
        collection.upsert(insertParam);

        // notice：upsert操作可用会有延迟
        Thread.sleep(1000 * 5);
    }

    private static void queryData(VectorDBClient client) {
        Database database = client.database(DBNAME);
        Collection collection = database.describeCollection(COLL_NAME);

        // query  查询
        // 1. query 用于查询数据
        // 2. 可以通过传入主键 id 列表或 filter 实现过滤数据的目的
        // 3. 如果没有主键 id 列表和 filter 则必须传入 limit 和 offset，类似 scan 的数据扫描功能
        // 4. 如果仅需要部分 field 的数据，可以指定 output_fields 用于指定返回数据包含哪些 field，不指定默认全部返回

        System.out.println("---------------------- query ----------------------");
        List<String> documentIds = Arrays.asList("0001", "0002", "0003", "0004", "0005");
        Filter filterParam = new Filter("bookName=\"三国演义\"");
        List<String> outputFields = Arrays.asList("id", "bookName");
        QueryParam queryParam = QueryParam.newBuilder()
                .withDocumentIds(documentIds)
                // 使用 filter 过滤数据
                .withFilter(filterParam)
                // limit 限制返回行数，1 到 16384 之间
                .withLimit(2)
                // 偏移
                .withOffset(1)
                // 指定返回的 fields
                .withOutputFields(outputFields)
                // 是否返回 vector 数据
                .withRetrieveVector(false)
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
                .withDocumentIds(documentIds)
                // 若使用 HNSW 索引，则需要指定参数 ef，ef 越大，召回率越高，但也会影响检索速度
                .withParams(new HNSWSearchParams(100))
                // 指定 Top K 的 K 值
                .withLimit(2)
                // 过滤获取到结果
                .withFilter(filterParam)
                .build();
        List<List<Document>> siDocs = collection.searchById(searchByIdParam);
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
        queryParam = QueryParam.newBuilder()
                .withDocumentIds(documentIds)
                // limit 限制返回行数，0 到 16384 之间
                .withLimit(2)
                // 偏移
                .withOffset(1)
                // 指定返回的 fields
                .withOutputFields(outputFields)
                // 是否返回 vector 数据
                .withRetrieveVector(true)
                .build();
        List<Document> allRes = collection.query(queryParam);
        List<List<Double>> vectors = new ArrayList<>();
        for (Document document : allRes) {
            List<Double> vector = (List<Double>) document.getVector();
            vectors.add(vector);
        }
        List<List<Double>> test = vectors;
        SearchByVectorParam searchByVectorParam = SearchByVectorParam.newBuilder()
                .withVectors(vectors)
                // 若使用 HNSW 索引，则需要指定参数ef，ef越大，召回率越高，但也会影响检索速度
                .withParams(new HNSWSearchParams(100))
                // 指定 Top K 的 K 值
                .withLimit(2)
                // 过滤获取到结果
                .withFilter(filterParam)
                .build();
        // 输出相似性检索结果，检索结果为二维数组，每一位为一组返回结果，分别对应 search 时指定的多个向量
        List<List<Document>> svDocs = collection.search(searchByVectorParam);
        i = 0;
        for (List<Document> docs : svDocs) {
            System.out.println("\tres: " + i++);
            for (Document doc : docs) {
                System.out.println("\tres: " + doc.toString());
            }
        }

        // searchByEmbeddingItems 返回类型为 SearchRes，接口查询过程中 embedding 可能会出现截断
        // 如发生截断将会返回响应 warn 信息，如需确认是否截断可以使用 SearchRes#getWarning" 获取警告信息，
        // 查询结果可以通过 SearchRes#getDocuments
        System.out.println("---------------------- searchByEmbeddingItems ----------------------");
        SearchByEmbeddingItemsParam searchByEmbeddingItemsParam = SearchByEmbeddingItemsParam.newBuilder()
                .withEmbeddingItems(Arrays.asList("闻刘玄德新领徐州", "细作探知这个消息"))
                .withParams(new HNSWSearchParams(100))
                .withLimit(5)
                .build();
        SearchRes searchRes = collection.searchByEmbeddingItems(searchByEmbeddingItemsParam);
        i = 0;
        for (List<Document> docs : searchRes.getDocuments()) {
            System.out.println("\tres: " + i++);
            for (Document doc : docs) {
                System.out.println("\tres: " + doc.toString());
            }
        }
    }

    private static void updateAndDelete(VectorDBClient client) throws InterruptedException {
        Database database = client.database(DBNAME);
        Collection collection = database.describeCollection(COLL_NAME);


        // update
        // 1. update 提供基于 [主键查询] 和 [Filter 过滤] 的部分字段更新或者非索引字段新增

        // filter 限制仅会更新 id = "0003"
        System.out.println("---------------------- update ----------------------");
        Filter filterParam = new Filter("bookName=\"三国演义\"");
        List<String> documentIds = Arrays.asList("0001", "0003");
        UpdateParam updateParam = UpdateParam
                .newBuilder()
                .addAllDocumentId(documentIds)
                .withFilter(filterParam)
                .build();
        Document updateDoc = Document
                .newBuilder()
                .addDocField(new DocField("page", 100))
                // 支持添加新的内容
                .addDocField(new DocField("extend", "extendContent"))
                .build();
        collection.update(updateParam, updateDoc);

        // delete
        // 1. delete 提供基于[ 主键查询]和[Filter 过滤]的数据删除能力
        // 2. 删除功能会受限于 collection 的索引类型，部分索引类型不支持删除操作

        // filter 限制只会删除 id = "00001" 成功
        System.out.println("---------------------- delete ----------------------");
        filterParam = new Filter("bookName=\"西游记\"");
        DeleteParam build = DeleteParam
                .newBuilder()
                .addAllDocumentId(documentIds)
                .withFilter(filterParam)
                .build();
        collection.delete(build);

        // notice：delete操作可用会有延迟
        Thread.sleep(1000 * 5);

        // rebuild index
        System.out.println("---------------------- rebuild index ----------------------");
        RebuildIndexParam rebuildIndexParam = RebuildIndexParam
                .newBuilder()
                .withDropBeforeRebuild(false)
                .withThrottle(1)
                .build();
        collection.rebuildIndex(rebuildIndexParam);
        Thread.sleep(5 * 1000);


        // truncate 会清除整个 Collection 的数据，包括索引
        System.out.println("---------------------- truncate collection ----------------------");
        AffectRes affectRes = database.truncateCollections(COLL_NAME);
        System.out.println("\tres: " + affectRes.toString());

        Thread.sleep(5 * 1000);
    }

    private static void deleteAndDrop(VectorDBClient client) {
        Database database = client.database(DBNAME);

        // 删除 collection
        System.out.println("---------------------- dropCollection ----------------------");
        database.dropCollection(COLL_NAME);

        // 删除 database
        System.out.println("---------------------- dropDatabase ----------------------");
        client.dropDatabase(DBNAME);
    }


    private static void clear(VectorDBClient client) {
//        List<String> databases = client.listDatabase();
//        for (String database : databases) {
//            client.dropDatabase(database);
//        }
        client.dropDatabase(DBNAME);
    }


    /**
     * 初始化创建 Collection 参数
     * 通过调用 addField 方法设计索引（不是设计 Collection 的结构）
     * <ol>
     *     <li>【重要的事】向量对应的文本字段不要建立索引，会浪费较大的内存，并且没有任何作用。</li>
     *     <li>【必须的索引】：主键id、向量字段 vector 这两个字段目前是固定且必须的，参考下面的例子；</li>
     *     <li>【其他索引】：检索时需作为条件查询的字段，比如要按书籍的作者进行过滤，这个时候author字段就需要建立索引，
     *     否则无法在查询的时候对 author 字段进行过滤，不需要过滤的字段无需加索引，会浪费内存；</li>
     *     <li>向量数据库支持动态 Schema，写入数据时可以写入任何字段，无需提前定义，类似MongoDB.</li>
     *     <li>例子中创建一个书籍片段的索引，例如书籍片段的信息包括 {id, vector, segment, bookName, author, page},
     *     id 为主键需要全局唯一，segment 为文本片段, vector 字段需要建立向量索引，假如我们在查询的时候要查询指定书籍
     *     名称的内容，这个时候需要对 bookName 建立索引，其他字段没有条件查询的需要，无需建立索引。/li>
     *     <li>创建带 Embedding 的 collection 需要保证设置的 vector 索引的维度和 Embedding 所用模型生成向量维度一致，模型及维度关系
     *     见下方表格
     *     </li>
     * </ol>
     * <table border>
     * <caption>模型列表</caption>
     *     <tr>
     *         <th>model</th>
     *         <th>dimension</th>
     *     </tr>
     *     <tr>
     *         <td>bge-base-zh</td>
     *         <td>768</td>
     *     </tr>
     *     <tr>
     *         <td>m3e-base</td>
     *         <td>768</td>
     *     </tr>
     *     <tr>
     *         <td>text2vec-large-chinese</td>
     *         <td>1024</td>
     *     </tr>
     *     <tr>
     *         <td>e5-large-v2</td>
     *         <td>1024</td>
     *     </tr>
     *     <tr>
     *         <td>multilingual-e5-base</td>
     *         <td>768</td>
     *     </tr>
     * </table>
     *
     * @param collName
     * @return
     */
    private static CreateCollectionParam initCreateEmbeddingCollectionParam(String collName) {
        return CreateCollectionParam.newBuilder()
                .withName(collName)
                .withShardNum(1)
                .withReplicaNum(0)
                .withDescription("test embedding collection0")
                .addField(new FilterIndex("id", FieldType.String, IndexType.PRIMARY_KEY))
                .addField(new VectorIndex("vector", BGE_BASE_ZH.getDimension(), IndexType.HNSW,
                        MetricType.COSINE, new HNSWParams(16, 200)))
                .addField(new FilterIndex("bookName", FieldType.String, IndexType.FILTER))
                .addField(new FilterIndex("author", FieldType.String, IndexType.FILTER))
                .withEmbedding(
                        Embedding
                                .newBuilder()
                                .withModel(BGE_BASE_ZH)
                                .withField("text")
                                .withVectorField("vector")
                                .build())
                .build();
    }

    /**
     * 测试 Filter
     */
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


}
