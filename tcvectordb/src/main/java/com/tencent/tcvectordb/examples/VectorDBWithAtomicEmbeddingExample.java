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
                .withData(Arrays.asList("什么是腾讯云向量数据库"))
                .withModelParam(ModelParam.newBuilder()
                        .withRetrieveDenseVector(true)
                        .withRetrieveSparseVector(true)
                        .build())
                .build();
        AtomicEmbeddingRes atomicEmbeddingRes = client.atomicEmbedding(param);
        System.out.println(atomicEmbeddingRes.toString());
        client.close();
    }
}
