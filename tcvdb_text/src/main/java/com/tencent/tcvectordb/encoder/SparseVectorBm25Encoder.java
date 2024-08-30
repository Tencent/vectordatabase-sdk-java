package com.tencent.tcvectordb.encoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.tokenizer.BaseTokenizer;
import com.tencent.tcvectordb.tokenizer.JiebaTokenizer;

import com.tencent.tcvectordb.util.JsonUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public SparseVectorBm25Encoder() {
        this.tokenizer = new JiebaTokenizer();
        this.b = 0.75;
        this.k1 = 1.2;
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

    public void setEnableStopWords(Boolean enableStopWords) {
        this.enableStopWords = enableStopWords;
        this.tokenizer.setEnableStopWords(enableStopWords);
    }

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

    @Override
    public Bm25Parameter downloadParams(String paramsFilePath) {
        return null;
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

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
