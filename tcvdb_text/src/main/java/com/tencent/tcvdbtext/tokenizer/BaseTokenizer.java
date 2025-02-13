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

import com.tencent.tcvdbtext.hash.BaseHash;

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

    protected Boolean cutAll;


    public BaseTokenizer(BaseHash hash, Boolean enableStopWords, Set<String> stopWords, Boolean lowerCase, String dictFilePath) {
        this.hash = hash;
        this.stopWords = stopWords;
        this.lowerCase = lowerCase;
        this.dictFilePath = dictFilePath;
        this.enableStopWords = enableStopWords;
    }

    public BaseTokenizer(BaseHash hash, Boolean enableStopWords, Set<String> stopWords, Boolean lowerCase,Boolean cutAll, String dictFilePath) {
        this.hash = hash;
        this.stopWords = stopWords;
        this.lowerCase = lowerCase;
        this.dictFilePath = dictFilePath;
        this.enableStopWords = enableStopWords;
        this.cutAll = cutAll;
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
        param.put("cutAll", cutAll);
        return param;
    }

    public void updateParameter(BaseHash hash, Set<String> stopWords, Boolean enableStopWords, Boolean lowerCase, String dictFilePath){
        this.hash = hash;
        this.stopWords = stopWords;
        this.lowerCase = lowerCase;
        this.dictFilePath = dictFilePath;
        this.enableStopWords = enableStopWords;
    }

    public void updateParameter(BaseHash hash, Set<String> stopWords, Boolean enableStopWords, Boolean lowerCase,Boolean cutAll,  String dictFilePath){
        this.hash = hash;
        this.stopWords = stopWords;
        this.lowerCase = lowerCase;
        this.dictFilePath = dictFilePath;
        this.enableStopWords = enableStopWords;
        this.cutAll = cutAll;
    }

    public boolean isStopWord(String word){
        if (stopWords.isEmpty()) return false;
        return stopWords.contains(word);
    }

    public Boolean getCutAll() {
        return cutAll;
    }

    public void setCutAll(Boolean cutAll) {
        this.cutAll = cutAll;
    }

    public void setStopWords(Set<String> stopWords) {
        this.stopWords = stopWords;
    }

    public abstract void loadDict(String dictFile);

    public abstract  void setLowerCase(Boolean lowerCase);
}
