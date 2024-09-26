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

package com.tencent.tcvectordb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.exception.ParamException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Insert Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertParam {
    private boolean buildIndex;
    private List<Object> documents;

    private InsertParam(Builder builder) {
        this.documents = builder.documents;
        this.buildIndex = builder.buildIndex;
    }

    public boolean isBuildIndex() {
        return buildIndex;
    }

    public List<Object> getDocuments() {
        return documents;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private boolean buildIndex = true;
        private List<Object> documents;

        public Builder() {
            this.documents = new ArrayList<>();
        }

        public Builder withBuildIndex(boolean buildIndex) {
            this.buildIndex = buildIndex;
            return this;
        }

        public Builder withDocuments(List<?> documents) {
            this.documents = Collections.unmodifiableList(documents);
            return this;
        }

        public Builder addDocument(Object document) {
            this.documents.add(document);
            return this;
        }

        public Builder addAllDocument(List<?> documentList) {
            this.documents.addAll(documentList);
            return this;
        }


        public InsertParam build() {
            if (this.documents.isEmpty()){
                throw new ParamException("InsertParam error: documents is empty");
            }
            return new InsertParam(this);
        }
    }
}
