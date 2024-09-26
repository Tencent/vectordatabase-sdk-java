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
package com.tencent.tcvdbtext.encoder;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bm25Parameter {
    private Double k1;
    private Double b;
    @JsonProperty("token_freq")
    private Map<String, Integer> tokenFreq;
    @JsonProperty("doc_count")
    private Integer docCount;
    @JsonProperty("average_doc_length")
    private Double averageDocLength;
    @JsonProperty("stop_words")
    private Boolean  stopWords;
    @JsonProperty("lower_case")
    private Boolean  lowerCase;
    @JsonProperty("dict_file")
    private String  dictFile;

    public Double getK1() {
        return k1;
    }

    public void setK1(Double k1) {
        this.k1 = k1;
    }

    public Double getB() {
        return b;
    }

    public void setB(Double b) {
        this.b = b;
    }

    public Map<String, Integer> getTokenFreq() {
        return tokenFreq;
    }

    public void setTokenFreq(Map<String, Integer> tokenFreq) {
        this.tokenFreq = tokenFreq;
    }

    public Integer getDocCount() {
        return docCount;
    }

    public void setDocCount(Integer docCount) {
        this.docCount = docCount;
    }

    public Double getAverageDocLength() {
        return averageDocLength;
    }

    public void setAverageDocLength(Double averageDocLength) {
        this.averageDocLength = averageDocLength;
    }

    public Boolean getStopWords() {
        if(stopWords == null) {
            return true;
        }
        return stopWords;
    }

    public void setStopWords(Boolean stopWords) {
        this.stopWords = stopWords;
    }

    public Boolean getLowerCase() {
        return lowerCase;
    }

    public void setLowerCase(Boolean lowerCase) {
        this.lowerCase = lowerCase;
    }

    public String getDictFile() {
        return dictFile;
    }

    public void setDictFile(String dictFile) {
        this.dictFile = dictFile;
    }
}
