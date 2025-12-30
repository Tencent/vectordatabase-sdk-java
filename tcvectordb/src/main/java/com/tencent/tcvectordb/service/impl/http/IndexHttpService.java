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
import com.tencent.tcvectordb.model.param.entity.BaseRes;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.service.ApiPath;
import com.tencent.tcvectordb.service.param.*;
import com.tencent.tcvectordb.utils.JsonUtils;

/**
 * HTTP service implementation for index operations.
 */
public class IndexHttpService extends BaseHttpService {

    /**
     * Rebuild the index for a collection to optimize search performance.
     */
    public BaseRes rebuildIndex(RebuildIndexParamInner param) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.REBUILD_INDEX);
        JsonNode jsonNode = this.post(url, param.toString(), false);
        return JsonUtils.parseObject(jsonNode.toString(), BaseRes.class);
    }

    /**
     * Rebuild the AI index for collection views.
     */
    public BaseRes rebuildAIIndex(RebuildIndexParamInner param) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_REBUILD_INDEX);
        JsonNode jsonNode = this.post(url, param.toString(), true);
        return JsonUtils.parseObject(jsonNode.toString(), BaseRes.class);
    }

    /**
     * Add a new index to improve query performance.
     */
    public BaseRes addIndex(AddIndexParamInner addIndexParamInner) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.ADD_INDEX);
        JsonNode jsonNode = this.post(url, addIndexParamInner.toString(), true);
        return JsonUtils.parseObject(jsonNode.toString(), BaseRes.class);
    }

    /**
     * Modify existing vector index configuration.
     */
    public BaseRes modifyVectorIndex(ModifyIndexParamInner param, boolean ai) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.MODIFY_VECTOR_INDEX);
        JsonNode jsonNode = this.post(url, param.toString(), ai);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * Drop an existing index from a collection.
     */
    public BaseRes dropIndex(DropIndexParamInner dropIndexParamInner) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DROP_INDEX);
        JsonNode jsonNode = this.post(url, JsonUtils.toJsonString(dropIndexParamInner), false);
        return JsonUtils.parseObject(jsonNode.toString(), BaseRes.class);
    }
}