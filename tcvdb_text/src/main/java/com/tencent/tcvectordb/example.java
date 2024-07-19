package com.tencent.tcvectordb;

import com.tencent.tcvectordb.encoder.SparseVectorBm25Encoder;
import com.tencent.tcvectordb.tokenizer.JiebaTokenizer;

import java.io.File;
import java.util.Arrays;

public class example {
    public static void main(String[] args) {
//        userDict();
        quickStart();
//        fitStart();
    }
    public static void quickStart() {
        SparseVectorBm25Encoder encoder = SparseVectorBm25Encoder.getBm25Encoder("zh");
        System.out.println(encoder.encodeQueries(Arrays.asList("什么是腾讯云向量数据库？", "腾讯云向量数据库有什么优势？", "腾讯云向量数据库能做些什么？")));
    }

    public static void fitStart() {
        SparseVectorBm25Encoder encoder = new SparseVectorBm25Encoder();
        encoder.fitCorpus(Arrays.asList("腾讯云向量数据库（Tencent Cloud VectorDB）是一款全托管的自研企业级分布式数据库服务，专用于存储、索引、检索、管理由深度神经网络或其他机器学习模型生成的大量多维嵌入向量。",
                "作为专门为处理输入向量查询而设计的数据库，它支持多种索引类型和相似度计算方法，单索引支持10亿级向量规模，高达百万级 QPS 及毫秒级查询延迟。",
                "不仅能为大模型提供外部知识库，提高大模型回答的准确性，还可广泛应用于推荐系统、NLP 服务、计算机视觉、智能客服等 AI 领域。",
                "腾讯云向量数据库（Tencent Cloud VectorDB）作为一种专门存储和检索向量数据的服务提供给用户， 在高性能、高可用、大规模、低成本、简单易用、稳定可靠等方面体现出显著优势。 ",
                "腾讯云向量数据库可以和大语言模型 LLM 配合使用。企业的私域数据在经过文本分割、向量化后，可以存储在腾讯云向量数据库中，构建起企业专属的外部知识库，从而在后续的检索任务中，为大模型提供提示信息，辅助大模型生成更加准确的答案。",
                "腾讯云数据库托管机房分布在全球多个位置，这些位置节点称为地域（Region），每个地域又由多个可用区（Zone）构成。每个地域（Region）都是一个独立的地理区域。每个地域内都有多个相互隔离的位置，称为可用区（Zone）。每个可用区都是独立的，但同一地域下的可用区通过低时延的内网链路相连。腾讯云支持用户在不同位置分配云资源，建议用户在设计系统时考虑将资源放置在不同可用区以屏蔽单点故障导致的服务不可用状态。"));
        System.out.println(encoder.encodeQueries(Arrays.asList("什么是腾讯云向量数据库？", "腾讯云向量数据库有什么优势？", "腾讯云向量数据库能做些什么？")));
    }
    public static void userDict(){
        JiebaTokenizer tokenizer = new JiebaTokenizer();
        String projectPath = example.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        // 创建一个 File 对象来表示工程路径
        File projectDirectory = new File(projectPath);

        // 获取工程路径的绝对路径
        String projectAbsolutePath = projectDirectory.getAbsolutePath();
        String path = projectAbsolutePath.replace("target/classes", "") +
                "src/main/resources/data/userdict_example.txt";
        System.out.println(tokenizer.tokenize("腾讯云向量数据库（Tencent Cloud VectorDB）是一款全托管的自研企业级分布式数据库服务，专用于存储、索引、检索、管理由深度神经网络或其他机器学习模型生成的大量多维嵌入向量。"));
        tokenizer.loadDict(path);
        System.out.println(tokenizer.tokenize("腾讯云向量数据库（Tencent Cloud VectorDB）是一款全托管的自研企业级分布式数据库服务，专用于存储、索引、检索、管理由深度神经网络或其他机器学习模型生成的大量多维嵌入向量。"));
    }
}