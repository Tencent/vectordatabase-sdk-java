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

package src.main.java.com.tencent.tcvectordb;

import com.tencent.tcvectordb.client.VectorDBClient;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.Database;
import com.tencent.tcvectordb.model.DocField;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.collection.AICollection;
import com.tencent.tcvectordb.model.param.collection.*;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.enums.AppendKeywordsToChunkEnum;
import com.tencent.tcvectordb.model.param.enums.AppendTitleToChunkEnum;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.utils.JSONUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VectorDB Java SDK usage example
 */
public class VectorDBExampleWithAI_doc {

    private static final String DBNAME = "ai_db_doc_ayh_test";
    private static final String COLL_NAME = "doc_collection_1";
    private static final String COLL_NAME_ALIAS = "doc_collection_alias_2";

    public static void example() throws Exception {
        // 创建VectorDB Client
        ConnectParam connectParam = initConnectParam();
        VectorDBClient client = new VectorDBClient(connectParam, ReadConsistencyEnum.EVENTUAL_CONSISTENCY);

        // 测试前清理环境
        System.out.println("---------------------- clear before test ----------------------");
        anySafe(() -> clear(client));
        createDatabaseAndCollection(client);
        Map<String, Object> metaDataMap = new HashMap<>();
        metaDataMap.put("bookName", "向量数据库库12");
        metaDataMap.put("bookId", "123456");
        uploadFile(client, "/data/home/yihaoan/projects/test/test22.md", metaDataMap);
        // 解析加载文件需要等待时间
        Thread.sleep(1000 * 10);

        queryData(client);
        GetFile(client, "test22.md");
        updateAndDelete(client);
        deleteAndDrop(client);
    }


    /**
     * init connect parameter
     *
     * @return {@link ConnectParam}
     */
    private static ConnectParam initConnectParam() {
        System.out.println("\tvdb_url: " + System.getProperty("vdb_url"));
        System.out.println("\tvdb_key: " + System.getProperty("vdb_key"));
        String vdb_url = "http://lb-rz3tigrs-971c4fayxj2hsidv.clb.ap-guangzhou.tencentclb.com:20000";
        String vdb_key = "e72DMhqC8zLcoLZWQ5LArJ7gDxok25ewseMcmP1s";
        return ConnectParam.newBuilder()
                .withUrl(vdb_url)
                .withUsername("root")
                .withKey(vdb_key)
                .withTimeout(30)
                .build();
    }

    /**
     * 执行 {@link Runnable} 捕获所有异常
     *
     * @param runnable {@link Runnable}
     */
    private static void anySafe(Runnable runnable) {
        try {
            runnable.run();
        } catch (VectorDBException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    private static void createDatabaseAndCollection(VectorDBClient client) throws InterruptedException {
        // 1. 创建数据库
        System.out.println("---------------------- create AI Database ----------------------");
        Database db = client.createAIDatabase(DBNAME);

        // 2. 列出所有数据库
        System.out.println("---------------------- listDatabase ----------------------");
        List<String> database = client.listDatabase();
        for (String s : database) {
            System.out.println("\tres: " + s);
        }

//        Database db = client.database(DBNAME);

        // 3. 创建 collection
        System.out.println("---------------------- createAICollection ----------------------");
        CreateAICollectionParam collectionParam = initCreateAICollectionParam(COLL_NAME);
        db.createAICollection(collectionParam);

        // 4. 列出所有 collection
        System.out.println("---------------------- listAICollections ----------------------");
        List<AICollection> cols = db.listAICollections();
        for (AICollection col : cols) {
            System.out.println("\tres: " + col.toString());
        }

        // 5. 设置 collection 别名
        System.out.println("---------------------- setAIAlias ----------------------");
        AffectRes affectRes = db.setAIAlias(COLL_NAME, COLL_NAME_ALIAS);
        System.out.println("\tres: " + affectRes.toString());
        Thread.sleep(5*1000);

        // 6. describe collection
        System.out.println("---------------------- describeAICollection ----------------------");
        AICollection descCollRes = db.describeAICollection(COLL_NAME);
        System.out.println("\tres: " + descCollRes.toString());


        // 7. delete alias
        System.out.println("---------------------- deleteAIAlias ----------------------");
        AffectRes affectRes1 = db.deleteAIAlias(COLL_NAME_ALIAS);
        System.out.println("\tres: " + affectRes1);

        // 8. describe collection
        System.out.println("---------------------- describeAICollection ----------------------");
        AICollection descCollRes1 = db.describeAICollection(COLL_NAME);
        System.out.println("\tres: " + descCollRes1.toString());

    }

    private static void uploadFile(VectorDBClient client, String filePath, Map<String, Object> metaDataMap) throws Exception {
        Database database = client.database(DBNAME);
        AICollection collection = database.describeAICollection(COLL_NAME);
        collection.upload(filePath, metaDataMap);
    }

    private static void GetFile(VectorDBClient client, String fileName) {
        Database database = client.database(DBNAME);
        AICollection collection = database.describeAICollection(COLL_NAME);
        System.out.println(collection.getFile(fileName, ""));
    }


    private static void queryData(VectorDBClient client) {
        Database database = client.database(DBNAME);
        AICollection collection = database.describeAICollection(COLL_NAME);

        // query  查询
        // 1. query 用于查询数据
        // 2. 可以通过传入主键 id 列表或 filter 实现过滤数据的目的
        // 3. 如果没有主键 id 列表和 filter 则必须传入 limit 和 offset，类似 scan 的数据扫描功能
        // 4. 如果仅需要部分 field 的数据，可以指定 output_fields 用于指定返回数据包含哪些 field，不指定默认全部返回
        System.out.println("---------------------- query ----------------------");
        Filter filterParam = new Filter("_indexed_status=2");
//        List<String> documentIds = Arrays.asList("1165953225640378368", "1165953228927545344");
        QueryParam queryParam = QueryParam.newBuilder()
//                .withDocumentIds(documentIds)
                // 使用 filter 过滤数据
                .withFilter(filterParam)
                // limit 限制返回行数，1 到 16384 之间
                .withLimit(100)
                // 是否返回 vector 数据
                .withRetrieveVector(false)
                .build();
        List<Document> qdos = collection.query(queryParam);
        for (Document doc : qdos) {
            System.out.println("\tres: " + JSONUtil.toJSONString(doc));
        }

        // search
        System.out.println("---------------------- search ----------------------");
//        SearchContenOption option = SearchContenOption.newBuilder().withChunkExpand(Arrays.asList(1,1)).build();
        SearchByContentsParam searchByContentsParam = SearchByContentsParam.newBuilder()
                .withContent("什么是 AI 中的向量表示")
                .build();
        List<Document> searchRes = collection.search(searchByContentsParam);
        int i = 0;
        for (Document doc : searchRes) {
            System.out.println("\tres" +(i++)+": "+ JSONUtil.toJSONString(doc));
        }
    }

    private static void updateAndDelete(VectorDBClient client) throws InterruptedException {
        Database database = client.database(DBNAME);
        AICollection collection = database.describeAICollection(COLL_NAME);
        // update
        // 1. update 提供基于 [主键查询] 和 [Filter 过滤] 的部分字段更新或者非索引字段新增

        // filter 限制仅会更新 条件符合的记录
        System.out.println("---------------------- update ----------------------");
        Filter filterParam = new Filter("_file_name=\"test21.md\"");
        List<String> documentIds = Arrays.asList("1166304506301120512", "1166305232221896704");
        UpdateParam updateParam = UpdateParam
                .newBuilder()
//                .addAllDocumentId(documentIds)
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

        //     filter 限制只会删除命中的记录
        System.out.println("---------------------- delete ----------------------");
        Filter filterParam1 = new Filter("_file_name=\"test21.md\"");
        DeleteParam build = DeleteParam
                .newBuilder()
//                .addAllDocumentId(documentIds)
                .withFilter(filterParam1)
                .build();
        AffectRes affectRes = collection.delete(build);
        System.out.println("\tres: " + affectRes.toString());

        // truncate collection
        System.out.println("---------------------- truncate ----------------------");
        database.truncateAICollections(COLL_NAME);
    }

    private static void deleteAndDrop(VectorDBClient client) {
        Database database = client.database(DBNAME);

        // 删除 collection
        System.out.println("---------------------- dropCollection ----------------------");
        database.dropAICollection(COLL_NAME);

        // 删除 database
        System.out.println("---------------------- dropDatabase ----------------------");
        client.dropAIDatabase(DBNAME);
    }


    private static void clear(VectorDBClient client) {
        client.dropAIDatabase(DBNAME);
    }


    private static CreateAICollectionParam initCreateAICollectionParam(String collName) {
        return CreateAICollectionParam.newBuilder()
                .withName(collName)
                .withDescription("test create ai collection")
                .withExpectedFileNum(1000000)
                .withLanguage(LanuageType.ZH)
                .withAverageFileSize(1024000)
                .addField(new FilterIndex("bookName", FieldType.String, IndexType.FILTER))
                .addField(new FilterIndex("author", FieldType.String, IndexType.FILTER))
                .build();
    }
}
