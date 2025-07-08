/*
 *Copyright (c) 2024, Tencent. All rights reserved.
 *
 *Redistribution and use in source and binary forms, with or without
 *modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of elasticfaiss nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 *THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 *BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 *THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tencent.tcvdbtext;

import com.tencent.tcvdbtext.encoder.SparseVectorBm25Encoder;
import com.tencent.tcvdbtext.tokenizer.JiebaTokenizer;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class example {
    public static void main(String[] args) {
        quickStart();
        fitStart();
        userDict();
        cutAll();
        stopWord();
    }
    public static void quickStart() {
        SparseVectorBm25Encoder encoder = SparseVectorBm25Encoder.getBm25Encoder("zh");
        List<String> texts = Arrays.asList("腾讯云向量数据库（Tencent Cloud VectorDB）是一款全托管的自研企业级分布式数据库服务，专用于存储、索引、检索、管理由深度神经网络或其他机器学习模型生成的大量多维嵌入向量。",
                "作为专门为处理输入向量查询而设计的数据库，它支持多种索引类型和相似度计算方法，单索引支持10亿级向量规模，高达百万级 QPS 及毫秒级查询延迟。",
                   "不仅能为大模型提供外部知识库，提高大模型回答的准确性，还可广泛应用于推荐系统、NLP 服务、计算机视觉、智能客服等 AI 领域。");
        System.out.println("cut all:" + encoder.getTokenizer().getCutAll());
        System.out.println("encode texts: "+ encoder.encodeTexts(texts));
        encoder.setCutAll(true);
        System.out.println("cut all:" + encoder.getTokenizer().getCutAll());
        System.out.println("encode texts: "+ encoder.encodeTexts(texts));

        System.out.println("encode multiple quires: "+ encoder.encodeQueries(Arrays.asList("什么是腾讯云向量数据库？", "腾讯云向量数据库有什么优势？", "腾讯云向量数据库能做些什么？")));
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
                "src/main/resources/data/user_dict/userdict_example.txt";
        System.out.println(tokenizer.tokenize("腾讯云向量数据库（Tencent Cloud VectorDB）是一款全托管的自研企业级分布式数据库服务，专用于存储、索引、检索、管理由深度神经网络或其他机器学习模型生成的大量多维嵌入向量。"));

        tokenizer.loadDict(path);
        System.out.println(tokenizer.tokenize("腾讯云向量数据库（Tencent Cloud VectorDB）是一款全托管的自研企业级分布式数据库服务，专用于存储、索引、检索、管理由深度神经网络或其他机器学习模型生成的大量多维嵌入向量。"));

        path = projectAbsolutePath.replace("target/classes", "") +
                "src/main/resources/data/user_dict/user_dict_1.txt";
        tokenizer.loadDict(path);
        System.out.println(tokenizer.tokenize("腾讯云向量数据库（Tencent Cloud VectorDB）是一款全托管的自研企业级分布式数据库服务，专用于存储、索引、检索、管理由深度神经网络或其他机器学习模型生成的大量多维嵌入向量。"));

    }


    public static void cutAll(){
        SparseVectorBm25Encoder encoder = SparseVectorBm25Encoder.getBm25Encoder("zh");
        System.out.println("cut all : " + encoder.getTokenizer().getCutAll());
        System.out.println(encoder.getTokenizer().tokenize("腾讯云向量数据库（Tencent Cloud VectorDB）是一款全托管的自研企业级分布式数据库服务，专用于存储、索引、检索、管理由深度神经网络或其他机器学习模型生成的大量多维嵌入向量。"));

        encoder.setCutAll(true);
        System.out.println("cut all: " + encoder.getTokenizer().getCutAll());
        System.out.println(encoder.getTokenizer().tokenize("腾讯云向量数据库（Tencent Cloud VectorDB）是一款全托管的自研企业级分布式数据库服务，专用于存储、索引、检索、管理由深度神经网络或其他机器学习模型生成的大量多维嵌入向量。"));
    }

    public static void stopWord(){
        SparseVectorBm25Encoder encoder = SparseVectorBm25Encoder.getBm25Encoder("zh");
        System.out.println(encoder.getTokenizer().tokenize("什么是腾讯云向量数据库。"));

        encoder.setEnableStopWords(false);
        System.out.println(encoder.getTokenizer().tokenize("什么是腾讯云向量数据库。"));
        String projectPath = example.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        // 创建一个 File 对象来表示工程路径
        File projectDirectory = new File(projectPath);
        String projectAbsolutePath = projectDirectory.getAbsolutePath();
        String path = projectAbsolutePath.replace("target/classes", "") +
                "src/main/resources/data/user_stopwords.txt";
        // 支持其他编码格式，默认UTF-8
        // encoder.setStopWords(path, StandardCharsets.UTF_8);
        encoder.setEnableStopWords(true);
        System.out.println(encoder.getTokenizer().tokenize("什么是腾讯云向量数据库。"));

    }
}
