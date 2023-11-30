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
import com.tencent.tcvectordb.model.*;
import com.tencent.tcvectordb.model.param.collection.*;
import com.tencent.tcvectordb.model.param.collectionView.CreateCollectionViewParam;
import com.tencent.tcvectordb.model.param.collectionView.EmbeddingParams;
import com.tencent.tcvectordb.model.param.collectionView.LanuageType;
import com.tencent.tcvectordb.model.param.collectionView.LoadAndSplitTextParam;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.utils.JsonUtils;

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
        metaDataMap.put("book_name", "向量数据库");
        metaDataMap.put("book_id", 1234);
        metaDataMap.put("author-array", Arrays.asList("1","2","3"));
        loadAndSplitText(client, "/Users/anyihao/tmp/test23.md", "file2", metaDataMap);
        // 解析加载文件需要等待时间
        Thread.sleep(1000 * 10);

        queryData(client);
        GetFile(client, "file2");
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
        String vdb_url = "http://9.135.180.240:8100";
        String vdb_key = "r81OtTBXUIoJIp1AukZHkxvqRDTNixtIHPC5c9hT";
        return ConnectParam.newBuilder()
                .withUrl(vdb_url)
                .withUsername("root")
                .withKey(vdb_key)
                .withTimeout(100)
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
        AIDatabase db = client.createAIDatabase(DBNAME);

        // 2. 列出所有数据库
        System.out.println("---------------------- listDatabase ----------------------");
        List<String> database = client.listDatabase();
        for (String s : database) {
            System.out.println("\tres: " + s);
        }

//        AIDatabase db = client.aiDatabase(DBNAME);

        // 3. 创建 collection
        System.out.println("---------------------- createCollectionView ----------------------");
        CreateCollectionViewParam collectionParam = initCreateCollectionViewParam(COLL_NAME);
        db.createCollectionView(collectionParam);

        // 4. 列出所有 collection
        System.out.println("---------------------- listCollectionView ----------------------");
        List<CollectionView> cols = db.listCollectionView();
        for (CollectionView col : cols) {
            System.out.println("\tres: " + col.toString());
        }

        // 5. 设置 collection 别名
        System.out.println("---------------------- setAIAlias ----------------------");
        AffectRes affectRes = db.setAIAlias(COLL_NAME, COLL_NAME_ALIAS);
        System.out.println("\tres: " + affectRes.toString());
        Thread.sleep(5*1000);

        // 6. describe collection
        System.out.println("---------------------- describeCollectionView ----------------------");
        CollectionView descCollRes = db.describeCollectionView(COLL_NAME);
        System.out.println("\tres: " + descCollRes.toString());


        // 7. delete alias
        System.out.println("---------------------- deleteAIAlias ----------------------");
        AffectRes affectRes1 = db.deleteAIAlias(COLL_NAME_ALIAS);
        System.out.println("\tres: " + affectRes1);

        // 8. describe collection
        System.out.println("---------------------- describeCollectionView ----------------------");
        CollectionView descCollRes1 = db.describeCollectionView(COLL_NAME);
        System.out.println("\tres: " + descCollRes1.toString());

    }

    private static void loadAndSplitText(VectorDBClient client, String filePath, String documentSetName, Map<String, Object> metaDataMap) throws Exception {
        AIDatabase database = client.aiDatabase(DBNAME);
        CollectionView collection = database.describeCollectionView(COLL_NAME);
        LoadAndSplitTextParam param = LoadAndSplitTextParam.newBuilder()
                .withLocalFilePath(filePath).withDocumentSetName(documentSetName).Build();
        collection.loadAndSplitText(param, metaDataMap);
    }

    private static void GetFile(VectorDBClient client, String fileName) {
        AIDatabase database = client.aiDatabase(DBNAME);
        CollectionView collection = database.describeCollectionView(COLL_NAME);
        System.out.println(collection.getFile(fileName, ""));
    }


    private static void queryData(VectorDBClient client) {
        AIDatabase database = client.aiDatabase(DBNAME);
        CollectionView collection = database.describeCollectionView(COLL_NAME);

        // query  查询
        // 1. query 用于查询数据
        // 2. 可以通过传入主键 id 列表或 filter 实现过滤数据的目的
        // 3. 如果没有主键 id 列表和 filter 则必须传入 limit 和 offset，类似 scan 的数据扫描功能
        // 4. 如果仅需要部分 field 的数据，可以指定 output_fields 用于指定返回数据包含哪些 field，不指定默认全部返回
        System.out.println("---------------------- query ----------------------");
        Filter filterParam = new Filter("_indexed_status=2");
        CollectionViewQueryParam queryParam = CollectionViewQueryParam.newBuilder().
                withLimit(10).
                withFilter(filterParam).
                withOutputFields(Arrays.asList("book_id", "author-array")).
                build();
        List<DocumentSet> qdos = collection.query(queryParam);
        for (DocumentSet doc : qdos) {
            System.out.println("\tres: " + doc.toString());
        }

        // search
        System.out.println("---------------------- search ----------------------");

        SearchContenOption option = SearchContenOption.newBuilder().withChunkExpand(Arrays.asList(1,1))
//                .withRerank(new RerankOption(true, 3))
                .build();
        SearchByContentsParam searchByContentsParam = SearchByContentsParam.newBuilder()
                .withContent("什么是向量")
                .withSearchContentOption(option)
                .build();
        System.out.println(qdos.get(0).search(searchByContentsParam).toString());
//        List<Document> searchRes = collection.search(searchByContentsParam);
//        int i = 0;
//        for (Document doc : searchRes) {
//            System.out.println("\tres" +(i++)+": "+ doc.toString());
//        }
    }

    private static void updateAndDelete(VectorDBClient client) throws InterruptedException {
        AIDatabase database = client.aiDatabase(DBNAME);
        CollectionView collection = database.describeCollectionView(COLL_NAME);
        // update
        // 1. update 提供基于 [主键查询] 和 [Filter 过滤] 的部分字段更新或者非索引字段新增

        // filter 限制仅会更新 条件符合的记录
        System.out.println("---------------------- update ----------------------");
        Filter filterParam = new Filter("documentSetName=\"file2\"");
        CollectionViewConditionParam updateParam = CollectionViewConditionParam
                .newBuilder()
                .withDocumnetSetNames(Arrays.asList("file2"))
                .withFilter(filterParam)
                .build();
        Map<String, Object> updateFieldValues = new HashMap<>();
        updateFieldValues.put("page", 100);
        updateFieldValues.put("extend", "extendContent");
        updateFieldValues.put("array_test", Arrays.asList("1", "2","5"));
        collection.update(updateParam, updateFieldValues);

        System.out.println(collection.query(10).get(0).toString());

        // delete
        // 1. delete 提供基于[ 主键查询]和[Filter 过滤]的数据删除能力
        // 2. 删除功能会受限于 collection 的索引类型，部分索引类型不支持删除操作

        //     filter 限制只会删除命中的记录
        System.out.println("---------------------- delete ----------------------");
        Filter filterParam1 = new Filter("_file_name=\"file2\"");
        CollectionViewConditionParam build = CollectionViewConditionParam
                .newBuilder()
                .withDocumnetSetNames(Arrays.asList("file2"))
                .withFilter(filterParam1)
                .build();
        AffectRes affectRes = collection.deleteDocumentSets(build);
        System.out.println("\tres: " + affectRes.toString());
        System.out.println(collection.query().size());

        // truncate collection
        System.out.println("---------------------- truncate ----------------------");
        database.truncateCollectionView(COLL_NAME);
    }

    private static void deleteAndDrop(VectorDBClient client) {
        AIDatabase database = client.aiDatabase(DBNAME);

        // 删除 collection
        System.out.println("---------------------- dropCollection ----------------------");
        database.dropCollectionView(COLL_NAME);

        // 删除 database
        System.out.println("---------------------- dropDatabase ----------------------");
        client.dropAIDatabase(DBNAME);
    }


    private static void clear(VectorDBClient client) {
        client.dropAIDatabase(DBNAME);
    }


    private static CreateCollectionViewParam initCreateCollectionViewParam(String collName) {
        return CreateCollectionViewParam.newBuilder()
                .withName(collName)
                .withDescription("test create ai collection")
                .withEmbedding(EmbeddingParams.newBuilder().withEnableWordEmbedding(true).withLanguage(LanuageType.ZH).Build())
                .addField(new FilterIndex("bookName", FieldType.String, IndexType.FILTER))
                .addField(new FilterIndex("author", FieldType.String, IndexType.FILTER))
                .addField(new FilterArrayIndex("array_test", FieldElementType.String, IndexType.FILTER))
                .build();
    }
}
