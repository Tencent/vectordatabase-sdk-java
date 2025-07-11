package com.tencent.tcvectordb.examples;

import com.tencent.tcvectordb.client.VectorDBClient;
import com.tencent.tcvectordb.model.param.dml.AtomicEmbeddingParam;
import com.tencent.tcvectordb.model.param.dml.ModelParam;
import com.tencent.tcvectordb.model.param.entity.AtomicEmbeddingRes;

import java.util.Arrays;
import java.util.Map;

public class VectorDBWithAtomicEmbeddingExample {
    public static void main(String[] args) {
        // 创建 VectorDB Client
        VectorDBClient client = CommonService.initClient();

        AtomicEmbeddingParam param = AtomicEmbeddingParam.newBuilder()
                .withModel("bge-m3")
                .withDataType("text")
                .withData(Arrays.asList("腾讯云向量数据库（Tencent Cloud VectorDB）是一款全托管的自研企业级分布式数据库服务，专用于存储、索引、检索、管理由深度神经网络或其他机器学习模型生成的大量多维嵌入向量。",
                        "作为专门为处理输入向量查询而设计的数据库，它支持多种索引类型和相似度计算方法，单索引支持10亿级向量规模，高达百万级 QPS 及毫秒级查询延迟。"))
                .withModelParam(ModelParam.newBuilder()
                        .withRetrieveDenseVector(false)
                        .withRetrieveSparseVector(true)
                        .build())
                .build();
        AtomicEmbeddingRes atomicEmbeddingRes = client.atomicEmbedding(param);
        System.out.println(atomicEmbeddingRes.toString());

    }
}
