/*
 * Copyright (C) 2023 Tencent Cloud.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the vectordb-sdk-java), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tencent.tcvectordb.service.impl.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.tencent.tcvectordb.model.param.dml.AtomicEmbeddingParam;
import com.tencent.tcvectordb.model.param.entity.AtomicEmbeddingRes;
import com.tencent.tcvectordb.service.ApiPath;
import com.tencent.tcvectordb.utils.JsonUtils;

/**
 * HTTP service implementation for embedding operations.
 * Handles text embedding generation using AI models.
 */
public class EmbeddingHttpService extends BaseHttpService {

    /**
     * Generate atomic embeddings for text content.
     * 
     * @param param Parameters for embedding generation including text content and model settings
     * @return Embedding vectors and metadata
     */
    public AtomicEmbeddingRes atomicEmbedding(AtomicEmbeddingParam param) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_ATOMIC_EMBEDDING_URL);
        JsonNode jsonNode = this.post(url, JsonUtils.toJsonString(param), false);
        return JsonUtils.parseObject(jsonNode.toString(), AtomicEmbeddingRes.class);
    }
}
