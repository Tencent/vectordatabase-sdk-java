package com.tencent.tcvectordb.tokenizer;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;
import com.tencent.tcvectordb.hash.BaseHash;
import com.tencent.tcvectordb.hash.Mm3BaseHash;

import java.nio.file.Paths;
import java.util.List;

public class JiebaTokenizer extends BaseTokenizer{

    private JiebaSegmenter segmenter;

    public JiebaTokenizer(BaseHash hash, List<String> stopWords, Boolean lowerCase, String dictFilePath) {
        super(hash, stopWords, lowerCase, dictFilePath);
        if (!dictFilePath.isEmpty()) {
            WordDictionary.getInstance().init(Paths.get(dictFilePath));
        }
        this.segmenter = new JiebaSegmenter();

    }
    public JiebaTokenizer(){
        super();
        this.hash = new Mm3BaseHash();
        this.segmenter = new JiebaSegmenter();
    }

    public void setDict(String dicFile) {
        if (!dicFile.isEmpty()) {
            WordDictionary.getInstance().init(Paths.get(dicFile));
        }
    }


    @Override
    public List<String> tokenize(String sentence) {
        if(sentence.isEmpty()){
            return List.of();
        }
        if (this.lowerCase){
            sentence = sentence.toLowerCase();
        }
        List<String> words;
        words = segmenter.sentenceProcess(sentence);
        words = words.stream().filter(word -> {
            if(word.equals(" ") || word.equals("　")) {
                return false;
            }else if (this.stopWords.contains(word)) {
                return false;
            }
            return true;
        }).toList();
        return words;
    }

    @Override
    public List<Integer> encode(String text) {
        return this.tokenize(text).stream().map(word -> this.hash.hash(word)).toList();
    }

    @Override
    public String decode(List<Integer> tokens) {
        return null;
    }

    public void updateParameter(BaseHash hash, List<String> stopWords, Boolean lowerCase, String dictFilePath){
        this.hash = hash;
        this.stopWords = stopWords;
        this.lowerCase = lowerCase;
        this.dictFilePath = dictFilePath;
    }
}
