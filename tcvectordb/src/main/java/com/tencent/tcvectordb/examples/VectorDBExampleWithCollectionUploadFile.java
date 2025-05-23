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

import com.sun.org.apache.xpath.internal.operations.Or;
import com.tencent.tcvectordb.client.VectorDBClient;
import com.tencent.tcvectordb.model.*;
import com.tencent.tcvectordb.model.Collection;
import com.tencent.tcvectordb.model.param.collection.*;
import com.tencent.tcvectordb.model.param.collectionView.*;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.GetImageUrlRes;
import com.tencent.tcvectordb.model.param.enums.EmbeddingModelEnum;
import com.tencent.tcvectordb.model.param.enums.OrderEnum;
import com.tencent.tcvectordb.model.param.enums.ParsingTypeEnum;
import com.tencent.tcvectordb.utils.JsonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * VectorDB Java SDK usage example
 */
public class VectorDBExampleWithCollectionUploadFile {

    private static final String DBNAME = "db-test-java-sdk";
    private static final String COLL_NAME = "coll-file_parse_java-sdk";

    public static void main(String[] args) throws Exception {

        // 创建 VectorDB Client
        VectorDBClient client = CommonService.initClient();
//         清理环境
        CommonService.anySafe(() -> client.dropDatabase(DBNAME));
        createDatabaseAndCollection(client);
        Map<String, Object> metaDataMap = new HashMap<>();
        metaDataMap.put("author", "Tencent");
        metaDataMap.put("tags", Arrays.asList("Embedding", "向量", "AI"));
//        // 使用输入流上传文档， 需指定输入流数据大小
//        File file = new File(System.getProperty("file_path"));
//        UploadFileUseInputStream(client, new FileInputStream(file), file.length(), "tcvdb.pdf", metaDataMap);

//        // 使用文件路径上传文档
        UploadFile(client, System.getProperty("file_path"), "tcvdb.pdf", metaDataMap);
//        // support markdown, pdf, pptx, docx document
//        // UploadFile(client, System.getProperty("file_path"), "腾讯云向量数据库.pdf", metaDataMap);
//        // UploadFile(client, System.getProperty("file_path"), "腾讯云向量数据库.pptx", metaDataMap);
//        // UploadFile(client, System.getProperty("file_path"), "腾讯云向量数据库.docx", metaDataMap);
//
//        // 解析加载文件需要等待时间
        Thread.sleep(1000 * 30);

        queryData(client);
        client.dropDatabase(DBNAME);
    }

    private static void createDatabaseAndCollection(VectorDBClient client) throws InterruptedException {
        // 1. 创建数据库
        System.out.println("---------------------- create Database ----------------------");
        Database db = client.createDatabase(DBNAME);

        // 2. 列出所有数据库
        System.out.println("---------------------- listDatabase ----------------------");
        List<String> database = client.listDatabase();
        for (String s : database) {
            System.out.println("\tres: " + s);
        }

        // 3. 创建 collection
        System.out.println("---------------------- createCollection ----------------------");
        CreateCollectionParam collectionParam = initCreateCollectionParam(COLL_NAME);
        client.createCollectionIfNotExists(DBNAME,  collectionParam);

        // describe collection
        System.out.println("---------------------- describeCollection ----------------------");
        Collection descCollRes = client.describeCollection(DBNAME, COLL_NAME);
        System.out.println("\tres: " + descCollRes.toString());

    }

    private static void UploadFile(VectorDBClient client, String filePath, String fileName, Map<String, Object> metaDataMap) throws Exception {
        Map<String,String> columnMap = new HashMap<>();
        columnMap.put("filename", "file_name");
        columnMap.put("text", "text");
        columnMap.put("imageList", "image_list");
        columnMap.put("chunkNum", "chunk_num");
        columnMap.put("sectionNum", "section_num");

        UploadFileParam param = UploadFileParam.newBuilder()
                .withLocalFilePath(filePath)
                .withSplitterProcess(SplitterPreprocessParams.newBuilder().withAppendKeywordsToChunkEnum(true).Build())
                // parsingProcess is used for parsing pdf file by vision model
                .withParsingProcess(ParsingProcessParam.newBuilder().withParsingType(ParsingTypeEnum.AlgorithmParsing).build())
                .withFileName(fileName)
                .withFieldMappings(columnMap)
                .withEmbeddingModel(EmbeddingModelEnum.BGE_BASE_ZH.getModelName())
                .Build();
        client.UploadFile(DBNAME, COLL_NAME, param, metaDataMap);
    }

    private static void UploadFileUseInputStream(VectorDBClient client, InputStream inputStream, Long inputStreamSize, String fileName, Map<String, Object> metaDataMap) throws Exception {
        Map<String,String> columnMap = new HashMap<>();
        columnMap.put("filename", "file_name");
        columnMap.put("text", "text");
        columnMap.put("imageList", "image_list");
        columnMap.put("chunkNum", "chunk_num");
        columnMap.put("sectionNum", "section_num");
        UploadFileParam param = UploadFileParam.newBuilder()
                .withFileInputStream(inputStream).withInputStreamDataSize(inputStreamSize)
                .withFileName(fileName)
                .withSplitterProcess(SplitterPreprocessParams.newBuilder().withAppendKeywordsToChunkEnum(true).Build())
                .withParsingProcess(ParsingProcessParam.newBuilder().withParsingType(ParsingTypeEnum.AlgorithmParsing).build())
                .withFieldMappings(columnMap)
                .withEmbeddingModel(EmbeddingModelEnum.BGE_BASE_ZH.getModelName())
                .Build();
        client.UploadFile(DBNAME,COLL_NAME, param, metaDataMap);
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

    private static void queryData(VectorDBClient client) {
        Database database = client.database(DBNAME);
        Collection collection = database.describeCollection(COLL_NAME);

        System.out.println("---------------------- query ----------------------");
        QueryParam queryParam = QueryParam.newBuilder()
                .withFilter("file_name=\"tcvdb.pdf\"")
                // limit 限制返回行数，1 到 16384 之间
                .withLimit(20)
                // 偏移
                .withOffset(0)
                // 是否返回 vector 数据
                .withRetrieveVector(false)
                .build();
        List<Document> qdos = collection.query(queryParam);
        for (Document doc : qdos) {
            System.out.println("\tres: " + doc.toString());
        }

        queryChunkBySectionNumAndChunkNum(client, qdos);

        System.out.println("---------------------- get image url ----------------------");
        GetImageUrlRes getImageUrlRes = client.GetImageUrl(DBNAME, COLL_NAME,
                GetImageUrlParam.newBuilder().setFileName("tcvdb.pdf")
                        .setDocumentIds(qdos.stream().map(doc->doc.getId()).collect(Collectors.toList()))
                        .build());
        System.out.println("get image url res:");
        System.out.println(JsonUtils.toJsonString(getImageUrlRes.getImages()));

        // search
        // 1. search 提供按照 vector 搜索的能力
        // 其他选项类似 search 接口

        System.out.println("---------------------- search ----------------------");
        SearchByVectorParam searchByVectorParam = SearchByVectorParam.newBuilder()
                .addVector(generateRandomVector(768))
                // 若使用 HNSW 索引，则需要指定参数ef，ef越大，召回率越高，但也会影响检索速度
                .withParams(new HNSWSearchParams(100))
//                .withRadius(0.5)
                // 指定 Top K 的 K 值
                .withLimit(10)
                // 过滤获取到结果
                .withFilter("file_name=\"tcvdb.pdf\"")
                .build();
        // 输出相似性检索结果，检索结果为二维数组，每一位为一组返回结果，分别对应 search 时指定的多个向量
        List<List<Document>> svDocs = client.search(DBNAME, COLL_NAME, searchByVectorParam);
        int i = 0;
        for (List<Document> docs : svDocs) {
            System.out.println("\tres: " + i);
            i++;
            for (Document doc : docs) {
                System.out.println("\tres: " + doc.toString());
            }
        }
    }

    private static void queryChunkBySectionNumAndChunkNum(VectorDBClient client, List<Document> qdos) {
        // 根据chunk_num 和 section_num 获取chunk文本
        System.out.println("---------------------- get chunk text by chunk_num ----------------------");
        Long chunkNum = (Long) qdos.get(1).getObject("chunk_num");
        Long sectionNum = (Long) qdos.get(1).getObject("section_num");
        if (chunkNum==null || sectionNum==null){
            return;
        }
        Long startChunkNum = chunkNum-2;
        if (startChunkNum < 0){
            startChunkNum = 0L;
        }
        QueryParam queryParam = QueryParam.newBuilder()
                .withFilter("file_name=\"tcvdb.pdf\"  and chunk_num >= " + startChunkNum + " and chunk_num <=" + (chunkNum+2)
                        + " and section_num=" + sectionNum)
                // limit 限制返回行数，1 到 16384 之间
                .withLimit(20)
                // 偏移
                .withOffset(0)
                // 是否返回 vector 数据
                .withRetrieveVector(false)
                .withSort(OrderRule.newBuilder().withFieldName("chunk_num").withDirection(OrderEnum.ASC).build())
                .build();
        // 输出相似性检索结果，检索结果为二维数组，每一位为一组返回结果，分别对应 search 时指定的多个向量
        List<Document> docs = client.query(DBNAME, COLL_NAME, queryParam);
        for (Document doc : docs) {
            System.out.println("\tres: " + doc.toString());
        }


    }


    private static CreateCollectionParam initCreateCollectionParam(String collName) {

        return CreateCollectionParam.newBuilder()
                .withName(collName)
                .withShardNum(1)
                .withReplicaNum(0)
                .withDescription("test collection0")
                .addField(new FilterIndex("id", FieldType.String, IndexType.PRIMARY_KEY))
                .addField(new VectorIndex("vector", 768, IndexType.HNSW,
                        MetricType.COSINE, new HNSWParams(16, 200)))
                .addField(new FilterIndex("file_name", FieldType.String, IndexType.FILTER))
                .addField(new FilterIndex("text", FieldType.String, IndexType.FILTER))
                .addField(new FilterIndex("image_list", FieldType.Array, IndexType.FILTER))
                .addField(new FilterIndex("chunk_num", FieldType.Uint64, IndexType.FILTER))
                .addField(new FilterIndex("section_num", FieldType.Uint64, IndexType.FILTER))
                .withEmbedding(Embedding.newBuilder().withModelName(EmbeddingModelEnum.BGE_BASE_ZH.getModelName()).withField("text").withVectorField("vector").build())
                .build();
    }
}
