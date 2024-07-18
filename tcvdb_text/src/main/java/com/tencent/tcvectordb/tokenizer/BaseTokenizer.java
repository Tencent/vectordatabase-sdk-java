package com.tencent.tcvectordb.tokenizer;

import com.tencent.tcvectordb.hash.BaseHash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseTokenizer {
    protected BaseHash hash;
    protected List<String> stopWords;
    protected Boolean lowerCase;
    protected String dictFilePath;

    public BaseTokenizer(BaseHash hash, List<String> stopWords, Boolean lowerCase, String dictFilePath) {
        this.hash = hash;
        this.stopWords = stopWords;
        this.lowerCase = lowerCase;
        this.dictFilePath = dictFilePath;
    }

    public BaseTokenizer() {
    }

    public abstract List<String> tokenize(String text);
    public abstract List<Integer> encode(String text);
    public abstract String decode(List<Integer> tokens);

    public Map<String, Object> getParameter(){
        Map<String, Object> param = new HashMap<>();
        param.put("hash", hash.getClass().getName());
        param.put("stopWords", stopWords);
        param.put("lowerCase", lowerCase);
        param.put("dictFilePath", dictFilePath);
        return param;
    }

    public void updateParameter(BaseHash hash, List<String> stopWords, Boolean lowerCase, String dictFilePath){
        this.hash = hash;
        this.stopWords = stopWords;
        this.lowerCase = lowerCase;
        this.dictFilePath = dictFilePath;
    }

    public boolean isStopWord(String word){
        if (stopWords.isEmpty()) return false;
        return stopWords.contains(word);
    }
}
