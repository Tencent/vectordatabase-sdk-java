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

package com.tencent.tcvdbtext.tokenizer;

import com.google.common.collect.Lists;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.WordDictionary;
import com.tencent.tcvdbtext.hash.BaseHash;
import com.tencent.tcvdbtext.hash.Mm3BaseHash;

import java.nio.file.Paths;
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

    public JiebaTokenizer(BaseHash hash, Boolean enableStopWords, Set<String> stopWords, Boolean lowerCase, Boolean cutAll, String dictFilePath) {
        super(hash, enableStopWords, stopWords, lowerCase, cutAll, dictFilePath);
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
    public void setStopWords(String stopWordsFile) {
        if (!stopWordsFile.isEmpty()) {
            this.stopWords = StopWords.getStopWordsFromFile(stopWordsFile);
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
        if (this.cutAll!=null && this.cutAll) {
            words = segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX).stream().map(word -> word.word).collect(Collectors.toList());
        }else{
            words = segmenter.process(sentence, JiebaSegmenter.SegMode.SEARCH).stream().map(word -> word.word).collect(Collectors.toList());
        }

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
        List<String> tokenize = this.tokenize(text);
//        System.out.println(tokenize);
        return tokenize.stream().map(word -> this.hash.hash(word)).collect(Collectors.toList());
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

    @Override
    public void setLowerCase(Boolean lowerCase) {
        this.lowerCase = lowerCase;
    }

    //生成builder模式
    public static class Builder{
        private BaseHash hash;
        private Set<String> stopWords;
        private Boolean lowerCase;
        private String dictFilePath;
        private Boolean enableStopWords;
        private Boolean cutAll;
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
        public Builder withCutAll(Boolean cutAll){
            this.cutAll = cutAll;
            return this;
        }
        public JiebaTokenizer build(){
            return new JiebaTokenizer(hash, enableStopWords, stopWords, lowerCase, cutAll, dictFilePath);
        }
    }

}
