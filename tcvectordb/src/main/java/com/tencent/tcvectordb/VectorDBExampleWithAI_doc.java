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
import com.tencent.tcvectordb.model.AIDatabase;
import com.tencent.tcvectordb.model.CollectionView;
import com.tencent.tcvectordb.model.DocumentSet;
import com.tencent.tcvectordb.model.param.collection.FieldType;
import com.tencent.tcvectordb.model.param.collection.FilterIndex;
import com.tencent.tcvectordb.model.param.collection.IndexType;
import com.tencent.tcvectordb.model.param.collectionView.*;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.entity.SearchContentInfo;
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

    private static final String DBNAME = "db_test-ai";
    private static final String COLL_NAME = "coll-ai-files";
    private static final String COLL_NAME_ALIAS = "alias-coll-ai-files";

    public static void main(String[] args) throws Exception {
        // 创建 VectorDB Client
        VectorDBClient client = CommonService.initClient();

        // 清理环境
        CommonService.anySafe(() -> client.dropAIDatabase(DBNAME));
        createAiDatabaseAndCollectionView(client);
        Map<String, Object> metaDataMap = new HashMap<>();
        metaDataMap.put("author", "Tencent");
        metaDataMap.put("tags", Arrays.asList("Embedding", "向量", "AI"));
        loadAndSplitText(client, "/data/home/yihaoan/腾讯云向量数据库.md", "腾讯云向量数据库.md", metaDataMap);
        // support markdown, pdf, pptx, docx document
        // loadAndSplitText(client, System.getProperty("file_path"), "腾讯云向量数据库.pdf", metaDataMap);
        // loadAndSplitText(client, System.getProperty("file_path"), "腾讯云向量数据库.pptx", metaDataMap);
        // loadAndSplitText(client, System.getProperty("file_path"), "腾讯云向量数据库.docx", metaDataMap);

        // 解析加载文件需要等待时间
        Thread.sleep(1000 * 10);

        queryData(client);
        GetFile(client, "腾讯云向量数据库.md");
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
        return ConnectParam.newBuilder()
                .withUrl("http://21.0.179.98:8100")
                .withUsername("root")
                .withKey("4ewdu8whi0wUTMPpRRIaK8K9EAHb4BA8OS8Twd9W")
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

    private static void createAiDatabaseAndCollectionView(VectorDBClient client) throws InterruptedException {
        // 1. 创建数据库
        System.out.println("---------------------- create AI Database ----------------------");
        AIDatabase db = client.createAIDatabase(DBNAME);

        // 2. 列出所有数据库
        System.out.println("---------------------- listDatabase ----------------------");
        List<String> database = client.listDatabase();
        for (String s : database) {
            System.out.println("\tres: " + s);
        }

        // 3. 创建 collectionView
        System.out.println("---------------------- createCollectionView ----------------------");
        CreateCollectionViewParam collectionViewParam = initCreateCollectionViewParam(COLL_NAME);
        db.createCollectionView(collectionViewParam);

        // 4. 列出所有 collectionView
        System.out.println("---------------------- listCollectionView ----------------------");
        List<CollectionView> cols = db.listCollectionView();
        for (CollectionView col : cols) {
            System.out.println("\tres: " + col.toString());
        }

        // 5. 设置 collectionView 别名
        System.out.println("---------------------- setAIAlias ----------------------");
        AffectRes affectRes = db.setAIAlias(COLL_NAME, COLL_NAME_ALIAS);
        System.out.println("\tres: " + affectRes.toString());
        Thread.sleep(5 * 1000);

        // 6. describe collectionView
        System.out.println("---------------------- describeCollectionView ----------------------");
        CollectionView descCollRes = db.describeCollectionView(COLL_NAME);
        System.out.println("\tres: " + descCollRes.toString());


        // 7. delete alias
        System.out.println("---------------------- deleteAIAlias ----------------------");
        AffectRes affectRes1 = db.deleteAIAlias(COLL_NAME_ALIAS);
        System.out.println("\tres: " + affectRes1);

        // 8. describe collectionView
        System.out.println("---------------------- describeCollectionView ----------------------");
        CollectionView descCollRes1 = db.describeCollectionView(COLL_NAME);
        System.out.println("\tres: " + descCollRes1.toString());

    }

    private static void loadAndSplitText(VectorDBClient client, String filePath, String documentSetName, Map<String, Object> metaDataMap) throws Exception {
        AIDatabase database = client.aiDatabase(DBNAME);
        CollectionView collection = database.describeCollectionView(COLL_NAME);
        LoadAndSplitTextParam param = LoadAndSplitTextParam.newBuilder()
                .withLocalFilePath(filePath).withDocumentSetName(documentSetName)
                .withSplitterProcess(SplitterPreprocessParams.newBuilder().withAppendKeywordsToChunkEnum(true).Build())
                .Build();
        collection.loadAndSplitText(param, metaDataMap);
    }

    private static void GetFile(VectorDBClient client, String fileName) {
        AIDatabase database = client.aiDatabase(DBNAME);
        CollectionView collection = database.describeCollectionView(COLL_NAME);
        System.out.println(collection.getFile(fileName, "").toString());
    }


    private static void queryData(VectorDBClient client) {
        AIDatabase database = client.aiDatabase(DBNAME);
        CollectionView collectionView = database.describeCollectionView(COLL_NAME);

        // query  查询
        // 1. query 用于查询数据
        // 2. 可以通过传入 filter 实现过滤数据的目的
        // 3. 如果需要指定部分 documentSetName，可以传入 documentSetNames
        // 4. 如果仅需要部分 field 的数据，可以指定 outputFields 用于指定返回数据包含哪些 field，不指定默认全部返回
        System.out.println("---------------------- query ----------------------");
        CollectionViewQueryParam queryParam = CollectionViewQueryParam.newBuilder().
                withLimit(2).
                withFilter(new Filter(Filter.in("author", Arrays.asList("Tencent", "tencent")))
                        .and(Filter.include("tags", Arrays.asList("AI", "Embedding")))).
                withDocumentSetNames(Arrays.asList("腾讯云向量数据库.md"))
//                .withOutputFields(Arrays.asList("textPrefix", "author", "tags"))
                .build();
        List<DocumentSet> qdos = collectionView.query(queryParam);
        for (DocumentSet doc : qdos) {
            System.out.println("\tres: " + doc.toString());
        }

        System.out.println("---------------------- get chunks ----------------------");
        System.out.println("get chunks res :");
        System.out.println(JsonUtils.toJsonString(collectionView.getChunks(null, "腾讯云向量数据库.md", 60, 0)));

        // search
        // 1. search 用于检索数据
        // 2. content 为必填参数，使用该参数值检索数据与之符合的数据
        // 3. SearchOption 配置搜索参数，开启 rerank 参数只有 collectionView 开启词向量精排才能使用
        // 4. 如果需要指定部分 documentSetName，可以传入 documentSetNames ，检索数据会在传入的 documentSetName 对应的文件中搜索
        // 5. 可以通过传入 filter 实现指定在符合条件的文件中检索
        System.out.println("---------------------- search ----------------------");

        SearchOption option = SearchOption.newBuilder().withChunkExpand(Arrays.asList(1, 1))
                .withRerank(new RerankOption(true, 3))
                .build();
        SearchByContentsParam searchByContentsParam = SearchByContentsParam.newBuilder()
                .withContent("什么是向量")
                .withSearchContentOption(option)
                .withFilter(new Filter(Filter.in("author", Arrays.asList("Tencent", "tencent")))
                        .and(Filter.include("tags", Arrays.asList("AI", "Embedding"))).getCond())
                .withDocumentSetName(Arrays.asList("腾讯云向量数据库.md"))
                .build();
//        System.out.println(qdos.get(0).search(searchByContentsParam).toString());
        List<SearchContentInfo> searchRes = collectionView.search(searchByContentsParam);
        int i = 0;
        for (SearchContentInfo doc : searchRes) {
            System.out.println("\tres" + (i++) + ": " + doc.toString());
        }
    }

    private static void updateAndDelete(VectorDBClient client) throws InterruptedException {
        AIDatabase database = client.aiDatabase(DBNAME);
        CollectionView collectionView = database.describeCollectionView(COLL_NAME);
        // update
        // 1. update 提供基于 documentSetName、documentSetId 和 Filter 过滤的部分字段更新或者非索引字段新增
        // 实际更新会受到过滤条件的限制
        System.out.println("---------------------- update ----------------------");
        Filter filterParam = new Filter("author=\"Tencent\"");
        CollectionViewConditionParam updateParam = CollectionViewConditionParam
                .newBuilder()
                .withDocumentSetNames(Arrays.asList("腾讯云向量数据库.md"))
                .withFilter(filterParam)
                .build();
        Map<String, Object> updateFieldValues = new HashMap<>();
        updateFieldValues.put("page", 100);
        updateFieldValues.put("author", "tencent");
        updateFieldValues.put("array_test", Arrays.asList("1", "2", "5"));
        collectionView.update(updateParam, updateFieldValues);

        System.out.println(collectionView.query(10).get(0).toString());

        // delete
        // 1. delete 提供基于 documentSetName、documentSetId 和 Filter 过滤的数据删除能力
        // 实际删除会受到过滤条件的限制
        System.out.println("---------------------- delete ----------------------");
        Filter filterParam1 = new Filter("author=\"tencent\"");
        CollectionViewConditionParam build = CollectionViewConditionParam
                .newBuilder()
                .withDocumentSetNames(Arrays.asList("腾讯云向量数据库.md"))
                .withFilter(filterParam1)
                .build();
        AffectRes affectRes = collectionView.deleteDocumentSets(build);
        System.out.println("\tres: " + affectRes.toString());
        System.out.println(collectionView.query().size());

        // truncate collectionView
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
                .withEmbedding(EmbeddingParams.newBuilder().withEnableWordEmbedding(true).withLanguage(LanguageType.ZH).Build())
                .withAverageFileSize(204800)
                .withExpectedFileNum(10240)
                .addField(new FilterIndex("author", FieldType.String, IndexType.FILTER))
                .addField(new FilterIndex("tags", FieldType.Array, IndexType.FILTER))
                .withSplitterPreprocess(SplitterPreprocessParams.newBuilder().
                        withAppendKeywordsToChunkEnum(true).
                        withAppendTitleToChunkEnum(false).Build())
                .build();
    }
}
