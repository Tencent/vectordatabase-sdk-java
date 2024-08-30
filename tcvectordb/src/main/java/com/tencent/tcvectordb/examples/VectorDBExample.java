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
import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.*;
/**
 * VectorDB Java SDK usage example
 */
public class VectorDBExample {

    private static final String DBNAME = "book";
    private static final String COLL_NAME = "book_segments_9";
    private static final String COLL_NAME_ALIAS = "collection_alias_1";

    public static void main(String[] args) throws InterruptedException {

//         创建 VectorDB Client
        VectorDBClient client = CommonService.initClient();

        // 清理环境
        CommonService.anySafe(() -> client.dropDatabase(DBNAME));
        createDatabaseAndCollection(client);
//        upsertData(client);
//        queryData(client);
//        updateAndDelete(client);
//        deleteAndDrop(client);
//        testFilter();
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
        db.createCollection(collectionParam);

        // 4. 列出所有 collection
//        Database db = client.database(DBNAME);
        System.out.println("---------------------- listCollections ----------------------");
        List<Collection> cols = db.listCollections();
        for (Collection col : cols) {
            System.out.println("\tres: " + col.toString());
        }

//        // 5. 设置 collection 别名
//        System.out.println("---------------------- setAlias ----------------------");
//        AffectRes affectRes = db.setAlias(COLL_NAME, COLL_NAME_ALIAS);
//        System.out.println("\tres: " + affectRes.toString());
//
//
//        // 6. describe collection
//        System.out.println("---------------------- describeCollection ----------------------");
//        Collection descCollRes = db.describeCollection(COLL_NAME);
//        System.out.println("\tres: " + descCollRes.toString());
//
//        // 7. delete alias
//        System.out.println("---------------------- deleteAlias ----------------------");
//        AffectRes affectRes1 = db.deleteAlias(COLL_NAME_ALIAS);
//        System.out.println("\tres: " + affectRes1);
//
//        // 8. describe collection
//        System.out.println("---------------------- describeCollection ----------------------");
//        Collection descCollRes1 = db.describeCollection(COLL_NAME);
//        System.out.println("\tres: " + descCollRes1.toString());

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
                        .withVectorByList(Arrays.asList(0.2123, 0.21, 0.213))
                        .addDocField(new DocField("bookName", "西游记"))
                        .addDocField(new DocField("author", "吴承恩"))
                        .addDocField(new DocField("page", 21))
                        .addDocField(new DocField("segment", "富贵功名，前缘分定，为人切莫欺心。"))
                        .addDocField(new DocField("array_test", Arrays.asList("1","2","3")))
                        .build(),
                Document.newBuilder()
                        .withId("0002")
                        .withVectorByList(Arrays.asList(0.2123, 0.22, 0.213))
                        .addDocField(new DocField("bookName", "西游记"))
                        .addDocField(new DocField("author", "吴承恩"))
                        .addDocField(new DocField("page", 22))
                        .addDocField(new DocField("segment",
                                "正大光明，忠良善果弥深。些些狂妄天加谴，眼前不遇待时临。"))
                        .addDocField(new DocField("array_test", Arrays.asList("4","5","6")))
                        .build(),
                Document.newBuilder()
                        .withId("0003")
                        .withVectorByList(Arrays.asList(0.2123, 0.23, 0.213))
                        .addDocField(new DocField("bookName", "三国演义"))
                        .addDocField(new DocField("author", "罗贯中"))
                        .addDocField(new DocField("page", 23))
                        .addDocField(new DocField("segment", "细作探知这个消息，飞报吕布。"))
                        .addDocField(new DocField("array_test", Arrays.asList("7","8","9")))
                        .build(),
                Document.newBuilder()
                        .withId("0004")
                        .withVectorByList(Arrays.asList(0.2123, 0.24, 0.213))
                        .addDocField(new DocField("bookName", "三国演义"))
                        .addDocField(new DocField("author", "罗贯中"))
                        .addDocField(new DocField("page", 24))
                        .addDocField(new DocField("segment", "富贵功名，前缘分定，为人切莫欺心。"))
                        .addDocField(new DocField("array_test", Arrays.asList("10","11","12")))
                        .build(),
                Document.newBuilder()
                        .withId("0005")
                        .withVectorByList(Arrays.asList(0.2123, 0.25, 0.213))
                        .addDocField(new DocField("bookName", "三国演义"))
                        .addDocField(new DocField("author", "罗贯中"))
                        .addDocField(new DocField("page", 25))
                        .addDocField(new DocField("segment",
                                "布大惊，与陈宫商议。宫曰：“闻刘玄德新领徐州，可往投之。"))
                        .build()));
        System.out.println("---------------------- upsert ----------------------");
        InsertParam insertParam = InsertParam.newBuilder().withDocuments(documentList).build();

//        documentList 是JSONObject列表或者document列表
//        InsertParam insertParam = InsertParam.newBuilder().withDocumentsData(documentData).build();

        collection.upsert(insertParam);
//        可以直接使用client进行操作
//        client.upsert(DBNAME,COLL_NAME, insertParam);

        // notice：upsert 操作可用会有延迟
        Thread.sleep(1000 * 5);
    }

    private static void queryData(VectorDBClient client) {
        Database database = client.database(DBNAME);
        Collection collection = database.describeCollection(COLL_NAME);

        System.out.println("---------------------- query ----------------------");
        List<String> documentIds = Arrays.asList("0001", "0002", "0003", "0004", "0005");
        Filter filterParam = new Filter("bookName=\"三国演义\"")
                .and(Filter.exclude("array_test", Arrays.asList("7")));
        List<String> outputFields = Arrays.asList("id", "bookName");
        QueryParam queryParam = QueryParam.newBuilder()
                .withDocumentIds(documentIds)
                // 使用 filter 过滤数据
                .withFilter(filterParam)
                // limit 限制返回行数，1 到 16384 之间
                 .withLimit(3)
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
        SearchByVectorParam searchByVectorParam = SearchByVectorParam.newBuilder()
                .addVector(Arrays.asList(0.2123, 0.23, 0.213))
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
            System.out.println("\tres: " + i);
            i++;
            for (Document doc : docs) {
                System.out.println("\tres: " + doc.toString());
            }
        }
    }

    private static void updateAndDelete(VectorDBClient client) throws InterruptedException {
        Database database = client.database(DBNAME);
        Collection collection = database.describeCollection(COLL_NAME);

        System.out.println("---------------------- update ----------------------");
        // update
        // 1. update 提供基于 [主键查询] 和 [Filter 过滤] 的部分字段更新或者非索引字段新增

        // filter 限制仅会更新 id = "0003"
        Filter filterParam = new Filter("bookName=\"三国演义\"");
        List<String> documentIds = Arrays.asList("0001", "0003");
        UpdateParam updateParam = UpdateParam
                .newBuilder()
                .addAllDocumentId(documentIds)
                .withFilter(filterParam)
                .build();
//        JSONObject data = new JSONObject("{\"page\":100, \"extend\":\"extendContent_1\",\"array_test\":[\"extendContent\",\"extendContent1\"]}");
        Document updateDoc = Document
                .newBuilder()
                .addDocField(new DocField("page", 100))
                // 支持添加新的内容
                .addDocField(new DocField("extend", "extendContent"))
                .addDocField(new DocField("array_test", Arrays.asList("extendContent", "extendContent1")))
                .build();
        AffectRes affectRes = collection.update(updateParam, updateDoc);
        System.out.println(affectRes.toString());
        System.out.println("---------------------- delete ----------------------");
        // delete
        // 1. delete 提供基于[ 主键查询]和[Filter 过滤]的数据删除能力
        // 2. 删除功能会受限于 collection 的索引类型，部分索引类型不支持删除操作

        // filter 限制只会删除 id = "00001" 成功
        filterParam = new Filter("bookName=\"西游记\"");
        DeleteParam build = DeleteParam
                .newBuilder()
                .addAllDocumentId(documentIds)
                .withFilter(filterParam)
                .build();
        collection.delete(build);


        // rebuild index
        System.out.println("---------------------- rebuildIndex ----------------------");

        RebuildIndexParam rebuildIndexParam = RebuildIndexParam
                .newBuilder()
                .withDropBeforeRebuild(false)
                .withThrottle(1)
                .build();
        collection.rebuildIndex(rebuildIndexParam);

        Thread.sleep(1000 * 5);

        // query
        System.out.println("----------------------  query ----------------------");
        documentIds = Arrays.asList("0001", "0002", "0003", "0004", "0005");
//        List<String> outputFields = Arrays.asList("id", "bookName", "page", "extend");
        QueryParam queryParam = QueryParam.newBuilder()
                .withDocumentIds(documentIds)
                // 使用 filter 过滤数据
//                .withOutputFields(outputFields)
                // 是否返回 vector 数据
                .withRetrieveVector(false)
                .build();
        List<Document> qdos = collection.query(queryParam);
        for (Document doc : qdos) {
            System.out.println("\tres: " + doc.toString());
        }


        // truncate 会清除整个 Collection 的数据，包括索引
        System.out.println("---------------------- truncate collection ----------------------");
        AffectRes affectRes1 = database.truncateCollections(COLL_NAME);
        System.out.println("\tres: " + JsonUtils.toJsonString(affectRes1));

        // notice：delete操作可用会有延迟
        Thread.sleep(1000 * 5);
    }

    private static void deleteAndDrop(VectorDBClient client) {
        Database database = client.database(DBNAME);

        // 删除 collection
        System.out.println("---------------------- truncate collection ----------------------");
        database.dropCollection(COLL_NAME);

        // 删除 database
        System.out.println("---------------------- truncate collection ----------------------");
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
     *     <li><例子中创建一个书籍片段的索引，例如书籍片段的信息包括 {id, vector, segment, bookName, author, page},
     *     id 为主键需要全局唯一，segment 为文本片段, vector 字段需要建立向量索引，假如我们在查询的时候要查询指定书籍
     *     名称的内容，这个时候需要对 bookName 建立索引，其他字段没有条件查询的需要，无需建立索引。/li>
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
                .withDescription("test collection0")
                .addField(new FilterIndex("id", FieldType.String, IndexType.PRIMARY_KEY))
                .addField(new VectorIndex("vector", 8, IndexType.HNSW,
                        MetricType.COSINE, new HNSWParams(16, 200)))
                .addField(new FilterIndex("random", FieldType.Uint64, IndexType.FILTER))
                .addField(new FilterIndex("field_str", FieldType.String, IndexType.FILTER))
                .build();
    }

    /**
     * 测试 Filter
     */
    public static void testFilter() {
        System.out.println("---------------------- testFilter ----------------------");
        System.out.println("\tres: " + new Filter("author=\"jerry\"")
                .and("a=1")
                .or("r=\"or\"")
                .orNot("rn=2")
                .andNot("an=\"andNot\"").and(Filter.include("key", Arrays.asList("1","2","3")))
                .getCond());
        System.out.println("\tres: " + Filter.in("key", Arrays.asList("v1", "v2", "v3")));
        System.out.println("\tres: " + Filter.in("key", Arrays.asList(1, 2, 3)));
        System.out.println(Document.newBuilder()
                .withId("0003")
                .withVectorByList(Arrays.asList(0.2123, 0.23, 0.213))
                .addDocField(new DocField("bookName", "三国演义"))
                .addDocField(new DocField("author", "罗贯中"))
                .addDocField(new DocField("page", 23))
                .addDocField(new DocField("segment", "细作探知这个消息，飞报吕布。"))
                .addDocField(new DocField("array_test", Arrays.asList("7","8","9")))
                .build().toString());
    }


}
