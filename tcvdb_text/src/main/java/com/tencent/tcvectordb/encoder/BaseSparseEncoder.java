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


import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.List;
public interface BaseSparseEncoder extends Serializable {
    /**
     * Convert the given texts into its corresponding sparse vector representation.
     * @param texts:the texts to be encoded
     * @return List<List<Pair<Long, Float>>>: sparse vectors of each text
     */
    public List<List<Pair<Long, Float>>> encodeTexts(List<String> texts);

    /**
     * Convert the given query texts into its corresponding sparse vector representation.
     * @param texts: the query  texts to be encoded
     * @return List<List<Pair<Long, Float>>>: sparse vectors of each query
     */
    public List<List<Pair<Long, Float>>> encodeQueries(List<String> texts);

    /**
     * Based on the given text corpus, calculate and adjust parameters such as term frequency and document count.
     * @param texts: the text to be fit and adjust parameters.
     */
    public void fitCorpus(List<String> texts);

    /**
     * Download the params of the encoder model to the local file
     * @param paramsFile: the file path to save the params
     */
    public void downloadParams(String paramsFile);

    /**
     * Set the params of the encoder model
     * @param paramsFile: the file path to load the params
     */
    public void setParams(String paramsFile);

    /**
     * Load the dictionary file used by the tokenizer.
     * @param dictFile: the file path to load the dict
     */
    public void setDict(String dictFile);

}
