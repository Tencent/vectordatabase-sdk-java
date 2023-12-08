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

package com.tencent.tcvectordb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tencent.tcvectordb.model.DocField;
import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.*;

/**
 * VectorDB Document
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchContentInfo {
    private Double score;
    private ContentInfo data;

    private SearchDocumentSetInfo documentSet;
    public Double getScore() {
        return score;
    }
    public void setScore(Double score) {
        this.score = score;
    }

    public ContentInfo getData() {
        return data;
    }

    public void setData(ContentInfo data) {
        this.data = data;
    }

    public SearchDocumentSetInfo getDocumentSet() {
        return documentSet;
    }

    public void setDocumentSet(SearchDocumentSetInfo documentSet) {
        this.documentSet = documentSet;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }

    private SearchContentInfo(Builder builder) {
        this.score = builder.score;
        this.data = builder.data;
        this.documentSet = builder.documentSet;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Double score;
        private ContentInfo data;
        private SearchDocumentSetInfo documentSet;

        public Builder() {
        }

        public Builder withSearchContentInfo(ContentInfo data){
            this.data = data;
            return this;
        }

        public Builder withScore(Double score) {
            this.score = score;
            return this;
        }

        public Builder withSearchDocumentSetInfo(SearchDocumentSetInfo documentSet) {
            this.documentSet = documentSet;
            return this;
        }

        public SearchContentInfo build() {
            return new SearchContentInfo(this);
        }
    }
}