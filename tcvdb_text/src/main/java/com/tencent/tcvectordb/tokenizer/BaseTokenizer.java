package com.tencent.tcvectordb.tokenizer;

import com.tencent.tcvectordb.hash.BaseHash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseTokenizer {
    protected BaseHash hash;
    protected Set<String> stopWords;
    protected Boolean lowerCase;
    protected String dictFilePath;

    protected Boolean enableStopWords;


    public BaseTokenizer(BaseHash hash, Boolean enableStopWords, Set<String> stopWords, Boolean lowerCase, String dictFilePath) {
        this.hash = hash;
        this.stopWords = stopWords;
        this.lowerCase = lowerCase;
        this.dictFilePath = dictFilePath;
        this.enableStopWords = enableStopWords;
    }

    public BaseTokenizer() {
    }

    public Boolean getEnableStopWords() {
        return enableStopWords;
    }

    public void setEnableStopWords(Boolean enableStopWords) {
        this.enableStopWords = enableStopWords;
    }

    public abstract List<String> tokenize(String text);
    public abstract List<Long> encode(String text);
    public abstract String decode(List<Integer> tokens);

    public Map<String, Object> getParameter(){
        Map<String, Object> param = new HashMap<>();
        param.put("hash", hash.getClass().getName());
        param.put("stopWords", stopWords);
        param.put("lowerCase", lowerCase);
        param.put("dictFilePath", dictFilePath);
        param.put("enableStopWords", enableStopWords);
        return param;
    }

    public void updateParameter(BaseHash hash, Set<String> stopWords, Boolean lowerCase, String dictFilePath){
        this.hash = hash;
        this.stopWords = stopWords;
        this.lowerCase = lowerCase;
        this.dictFilePath = dictFilePath;
    }

    public boolean isStopWord(String word){
        if (stopWords.isEmpty()) return false;
        return stopWords.contains(word);
    }
    public abstract void loadDict(String dictFile);
}
