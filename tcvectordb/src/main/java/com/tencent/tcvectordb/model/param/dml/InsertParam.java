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
import com.tencent.tcvectordb.model.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Insert Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertParam {
    private boolean buildIndex;
    private List<Document> documents;
    private List<JSONObject> documentsData;

    private InsertParam(Builder builder) {
        this.documents = builder.documents;
        this.buildIndex = builder.buildIndex;
        this.documentsData = builder.documentsData;
    }

    public boolean isBuildIndex() {
        return buildIndex;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public List<JSONObject> getDocumentsData() {
        return documentsData;
    }
    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private boolean buildIndex = true;
        private List<Document> documents;

        private List<JSONObject> documentsData;

        public Builder() {
            this.documents = new ArrayList<>();
        }

        public Builder withBuildIndex(boolean buildIndex) {
            this.buildIndex = buildIndex;
            return this;
        }

        public Builder withDocuments(List<Document> documents) {
            this.documents = documents;
            return this;
        }

        public Builder addDocument(Document document) {
            this.documents.add(document);
            return this;
        }

        public Builder addAllDocument(List<Document> documentList) {
            this.documents.addAll(documentList);
            return this;
        }

        public Builder withAllDocumentsData(List<JSONObject> documentsData) {
            this.documentsData = documentsData;
            return this;
        }


        public InsertParam build() {
            if (this.documents.isEmpty() && this.documentsData.isEmpty()){
                throw new ParamException("InsertParam error: documents is empty");
            }
            return new InsertParam(this);
        }
    }
}
