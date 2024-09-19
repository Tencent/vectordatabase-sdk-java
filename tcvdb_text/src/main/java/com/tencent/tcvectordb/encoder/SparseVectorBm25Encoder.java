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
package com.tencent.tcvectordb.encoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.tokenizer.BaseTokenizer;
import com.tencent.tcvectordb.tokenizer.JiebaTokenizer;

import com.tencent.tcvectordb.util.JsonUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SparseVectorBm25Encoder implements BaseSparseEncoder{
    @JsonIgnore
    private BaseTokenizer tokenizer;

    private Double b;
    private Double k1;

    private Map<String, Integer> tokenFreq;
    private Integer docCount;
    private Double averageDocLength;
    private Boolean enableStopWords;
    private Boolean lowerCase;

    public SparseVectorBm25Encoder() {
        this.tokenizer = new JiebaTokenizer();
        this.b = 0.75;
        this.k1 = 1.2;
    }

    public BaseTokenizer getTokenizer() {
        return tokenizer;
    }

    public void setTokenizer(BaseTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public Double getB() {
        return b;
    }

    public Double getK1() {
        return k1;
    }

    public Map<String, Integer> getTokenFreq() {
        return tokenFreq;
    }

    public Integer getDocCount() {
        return docCount;
    }

    public Double getAverageDocLength() {
        return averageDocLength;
    }

    public void setB(Double b) {
        this.b = b;
    }

    public void setK1(Double k1) {
        this.k1 = k1;
    }

    public void setTokenFreq(Map<String, Integer> tokenFreq) {
        this.tokenFreq = tokenFreq;
    }

    public void setDocCount(Integer docCount) {
        this.docCount = docCount;
    }

    public void setAverageDocLength(Double averageDocLength) {
        this.averageDocLength = averageDocLength;
    }

    public Boolean getEnableStopWords() {
        return enableStopWords;
    }

    public Boolean getLowerCase() {
        return lowerCase;
    }

    public void setEnableStopWords(Boolean enableStopWords) {
        this.enableStopWords = enableStopWords;
        this.tokenizer.setEnableStopWords(enableStopWords);
    }

    public void setLowerCase(Boolean lowerCase) {
        this.lowerCase = lowerCase;
        this.tokenizer.setLowerCase(lowerCase);
    }

    /**
     * get a BM25Encoder with default OKAPI BM25 Model.
     * @param language: model name. example: "zh" or "en"
     * @return BM25Encoder with default OKAPI BM25 Model.
     */
    public static SparseVectorBm25Encoder getBm25Encoder(String language) {
        String path;
        if (language.equals("zh")){
            path = "data/bm25_zh_default.json";
        }else if (language.equals("en")){
            path = "data/bm25_en_default.json";
        }else {
            throw new IllegalArgumentException("language must be zh or en");
        }
        SparseVectorBm25Encoder sparseVectorBm25Encoder = new SparseVectorBm25Encoder(new JiebaTokenizer(), 0.75, 1.2);
        sparseVectorBm25Encoder.setParams(path);
        return sparseVectorBm25Encoder;
    }

    /**
     * get a BM25Encoder with default OKAPI BM25 Model.
     * @return BM25Encoder with default OKAPI BM25 Model of zh.
     */
    public static SparseVectorBm25Encoder getDefaultBm25Encoder() {
        return getBm25Encoder("zh");
    }

    public SparseVectorBm25Encoder(BaseTokenizer tokenizer, Double b, Double k1) {
        this.tokenizer = tokenizer;
        this.b = b;
        this.k1 = k1;
    }

    private List<Pair<Long, Integer>> getTokenTF(String text) {
        List<Long> tokens = this.tokenizer.encode(text);
        Map<Long, Integer> tokenFreq = new HashMap<>();
        for (Long token : tokens) {
            if (tokenFreq.containsKey(token)) {
                tokenFreq.put(token, tokenFreq.get(token) + 1);
            } else {
                tokenFreq.put(token, 1);
            }
        }
        return tokenFreq.entrySet().stream().map(token->Pair.of(token.getKey(), token.getValue())).collect(Collectors.toList());
    }

    /**
     * Convert the given text into its corresponding sparse vector representation.
     * Steps:
     *  1、Tokenization: Use a tokenization tool, such as Jieba, that supports both Chinese and English languages.
     *  2、Hashing: Convert the words obtained in step 1 into unique IDs, where each word corresponds to a unique ID.
     *      For example, after hashing ["向量", "数据库"], we get [118762, 231429].
     *  3、Calculate the relevance of each word to the document: Calculate the relevance of each word using methods
     *      like calculating term frequency (tf) in BM25 or exploring approaches used by Pinecone. For example, after
     *      calculating the relevance of ["向量", "数据库"], we get [0.7612, 0.9564].
     *  4、Obtain the sparse vector: Based on the calculations from steps 2 and 3, we can obtain the sparse vector.
     *      For example, after steps 2 and 3, we can get {118762: 0.7612, 231429: 0.9564}.
     * @param texts: List<String> origin texts
     * @return List<List<Pair<Long, Float>>>: sparse vectors of origin texts
     */
    @Override
    public List<List<Pair<Long, Float>>> encodeTexts(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            throw new IllegalArgumentException("texts is empty");
        }
        if (this.tokenFreq == null || this.docCount == null || this.averageDocLength == null) {
            throw new IllegalArgumentException("BM25 must be fit before encoding documents");
        }
        List<List<Pair<Long, Float>>> sparseVectors = new ArrayList<>();
        for (String text : texts) {
            List<Pair<Long, Integer>> tokensPairs = this.getTokenTF(text);
            Integer tfSum = tokensPairs.stream().map(Pair::getRight).
                    reduce(0, Integer::sum);
            List<Pair<Long, Float>> sparseVector = new ArrayList<>();
            for (Pair<Long, Integer> token : tokensPairs) {
                Integer freq = token.getValue();
                double score = (freq+0.0) / (this.k1*(1 - this.b + this.b * (tfSum / this.averageDocLength)) + freq);
                sparseVector.add(Pair.of(token.getKey(), (float)score));
            }
            sparseVectors.add(sparseVector);
        }
        return sparseVectors;
    }

    /**
     * Convert the given query into its corresponding sparse vector representation.
     * Steps:
     *  1、Tokenization: Use a tokenization tool like Jieba that supports both Chinese and English languages.
     *  2、Hashing: Convert the words obtained in step 1 into unique IDs, where each word corresponds to a unique ID.
     *  3、Calculate the relevance of each word to the document: For each word, calculate its relevance to the query.
     *      You can refer to the calculation of "idf" in BM25 or explore methods used by Pinecone.
     *  4、Obtain the sparse vector.
     *
     * @param texts: List<String> query texts
     * @return List<List<Pair<Long, Float>>>: sparse vectors of query texts
     */
    @Override
    public List<List<Pair<Long, Float>>> encodeQueries(List<String> texts) {
        if (this.tokenFreq == null || this.docCount == null || this.averageDocLength == null) {
            throw new IllegalArgumentException("BM25 must be fit before encoding documents");
        }
        List<List<Pair<Long, Float>>> sparseVectors = new ArrayList<>();
        for (String text : texts) {
            List<Pair<Long, Integer>> tokensPairs = this.getTokenTF(text);
            List<Integer> df = tokensPairs.stream().
                    map(key->this.tokenFreq.getOrDefault(key.getKey().toString(), 1)).collect(Collectors.toList());

            List<Double> idfs = df.stream().map(idf->Math.log((this.docCount +1) / (idf + 0.5))).collect(Collectors.toList());
            Double idfSum = idfs.stream().reduce(0.0, Double::sum);
            List<Pair<Long, Float>> sparseVector = new ArrayList<>();
            for (int i = 0; i < tokensPairs.size(); i++) {
                sparseVector.add(Pair.of(tokensPairs.get(i).getKey(), (float)(idfs.get(i) / idfSum)));
            }
            sparseVectors.add(sparseVector);
        }
        return sparseVectors;
    }

    /**
     * Based on the given text corpus, calculate and adjust parameters such as term frequency and document count
     * (parameters used in step 3 of encode_texts and encode_queries).
     *
     * @param texts： List<String> text used to fit corpus
     */
    @Override
    public void fitCorpus(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            throw new IllegalArgumentException("texts is empty");
        }
        Map<String, Integer> tokenFreq = new HashMap<>();
        int docNum = 0;
        int sumDocLen = 0;
        List<Integer> docLengths = new ArrayList<>();
        for (String text : texts) {
            List<Pair<Long,Integer>> tokens = this.getTokenTF(text);
            docNum += 1;
            sumDocLen += tokens.stream().map(Pair::getRight).reduce(0, Integer::sum);
            for (Pair<Long, Integer> token : tokens) {
                if (tokenFreq.containsKey(token.getKey().toString())) {
                    tokenFreq.put(token.getKey().toString(), tokenFreq.get(token.getKey().toString()) + 1);
                } else {
                    tokenFreq.put(token.getKey().toString(), 1);
                }
            }
            docLengths.add(tokens.size());
        }
        if (this.tokenFreq == null || this.docCount == null || this.averageDocLength == null) {
            this.tokenFreq = tokenFreq;
            this.docCount = docNum;
            this.averageDocLength = docLengths.stream().reduce(0, Integer::sum) / (double) docLengths.size();
        }else {
            this.docCount += docNum;
            this.averageDocLength = (this.averageDocLength * this.docCount + sumDocLen) / (this.docCount + docNum);
            for (String token : tokenFreq.keySet()) {
                if (this.tokenFreq.containsKey(token)) {
                    this.tokenFreq.put(token, this.tokenFreq.get(token) + tokenFreq.get(token));
                } else {
                    this.tokenFreq.put(token, tokenFreq.get(token));
                }
            }
        }

    }

    /**
     * Download the parameters to the given path.
     * @param paramsFilePath : String specific file path to download the parameters
     */
    @Override
    public void downloadParams(String paramsFilePath) {
        try {
            FileWriter writer = new FileWriter(paramsFilePath);
//            System.out.println(JsonUtils.toJsonString(this));
            writer.write(JsonUtils.toJsonString(this));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setParams(String paramsFile) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(paramsFile);
        try {
            StringBuilder fileContent = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // 处理读取到的每一行内容
                fileContent.append(line);
            }

            Bm25Parameter bm25Parameter =  JsonUtils.parseObject(fileContent.toString(), Bm25Parameter.class);
            this.tokenFreq = bm25Parameter.getTokenFreq();
            this.docCount = bm25Parameter.getDocCount();
            this.averageDocLength = bm25Parameter.getAverageDocLength();
            this.b = bm25Parameter.getB();
            this.k1 = bm25Parameter.getK1();
            this.enableStopWords = bm25Parameter.getStopWords();
            this.setEnableStopWords(this.enableStopWords);
            this.setLowerCase(bm25Parameter.getLowerCase());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load the dictionary file used by the tokenizer.
     * @param dictFile: the file path to load the dict, txt format, words are separated by newline or space.
     */
    @Override
    public void setDict(String dictFile) {
        this.tokenizer.loadDict(dictFile);
    }
    // build模式
    public static class Builder {
        private BaseTokenizer tokenizer;
        private Double b;
        private Double k1;
        public Builder withTokenizer(BaseTokenizer tokenizer) {
            this.tokenizer = tokenizer;
            return this;
        }
        public Builder withB(Double b) {
            this.b = b;
            return this;
        }

        public Builder withK1(Double k1) {
            this.k1 = k1;
            return this;
        }
        public SparseVectorBm25Encoder build() {
            return new SparseVectorBm25Encoder(tokenizer, b, k1);
        }
    }
}
