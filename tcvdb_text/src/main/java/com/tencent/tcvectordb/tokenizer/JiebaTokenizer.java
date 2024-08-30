package com.tencent.tcvectordb.tokenizer;

import com.google.common.collect.Lists;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.WordDictionary;
import com.tencent.tcvectordb.hash.BaseHash;
import com.tencent.tcvectordb.hash.Mm3BaseHash;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JiebaTokenizer extends BaseTokenizer{

    private JiebaSegmenter segmenter;

    public JiebaTokenizer(BaseHash hash, Boolean enableStopWords, Set<String> stopWords, Boolean lowerCase, String dictFilePath) {
        super(hash, enableStopWords, stopWords, lowerCase, dictFilePath);
        if (!dictFilePath.isEmpty()) {
            WordDictionary.getInstance().init(Paths.get(dictFilePath));
        }
        this.segmenter = new JiebaSegmenter();

    }
    public JiebaTokenizer(){
        super();
        this.hash = new Mm3BaseHash();
        this.segmenter = new JiebaSegmenter();
        this.stopWords = StopWords.getStopWordsFromFile("data/stopwords.txt");
    }

    public JiebaTokenizer(String dictFilePath){
        super();
        this.hash = new Mm3BaseHash();
        this.segmenter = new JiebaSegmenter();
        this.stopWords = StopWords.getStopWordsFromFile("data/stopwords.txt");
        WordDictionary.getInstance().loadUserDict(Paths.get(dictFilePath));
    }

    public void setDict(String dicFile) {
        if (!dicFile.isEmpty()) {
            WordDictionary.getInstance().loadUserDict(Paths.get(dicFile));
        }
    }


    @Override
    public List<String> tokenize(String sentence) {
        if(sentence.isEmpty()){
            return Lists.newArrayList();
        }
        if (this.lowerCase!=null && this.lowerCase){
            sentence = sentence.toLowerCase();
        }
        List<String> words;
        words = segmenter.sentenceProcess(sentence);
        words = words.stream().filter(word -> {
            if(word.equals(" ") || word.equals("　")) {
                return false;
            }else if (this.enableStopWords!=null && this.enableStopWords &&
                    this.stopWords!=null && this.stopWords.contains(word)) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());


        return words;
    }

    @Override
    public List<Long> encode(String text) {
        return this.tokenize(text).stream().map(word -> this.hash.hash(word)).collect(Collectors.toList());
    }

    @Override
    public String decode(List<Integer> tokens) {
        return null;
    }

    public void updateParameter(BaseHash hash, Set<String> stopWords, Boolean lowerCase, String dictFilePath){
        this.hash = hash;
        this.stopWords = stopWords;
        this.lowerCase = lowerCase;
        this.dictFilePath = dictFilePath;
        this.loadDict(dictFilePath);
    }

    @Override
    public void loadDict(String dictFile) {
//        WordDictionary.getInstance().freqs.clear();
//        WordDictionary.getInstance().loadDict();
        WordDictionary.getInstance().loadUserDict(Paths.get(dictFile));
    }

    //生成builder模式
    public static class Builder{
        private BaseHash hash;
        private Set<String> stopWords;
        private Boolean lowerCase;
        private String dictFilePath;
        private Boolean enableStopWords;
        public Builder withHash(BaseHash hash){
            this.hash = hash;
            return this;
        }
        public Builder withStopWords(Set<String> stopWords){
            this.stopWords = stopWords;
            return this;
        }
        public Builder withLowerCase(Boolean lowerCase){
            this.lowerCase = lowerCase;
            return this;
        }
        public Builder withDictFilePath(String dictFilePath){
            this.dictFilePath = dictFilePath;
            return this;
        }
        public Builder withEnableStopWords(Boolean enableStopWords){
            this.enableStopWords = enableStopWords;
            return this;
        }
        public JiebaTokenizer build(){
            return new JiebaTokenizer(hash, enableStopWords, stopWords, lowerCase, dictFilePath);
        }
    }

}
