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
import com.tencent.tcvdbtext.exception.ParamException;

import java.util.ArrayList;
import java.util.List;

/**
 * Search By Id Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchByIdParam extends SearchParam {
    private List<String> documentIds;


    private SearchByIdParam(Builder builder) {
        super(builder);
        this.documentIds = builder.documentIds;
    }

    public List<String> getDocumentIds() {
        return documentIds;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends SearchParam.Builder<Builder> {
        private List<String> documentIds;

        private Builder() {
            super();
            this.documentIds = new ArrayList<>();
        }

        public Builder withDocumentIds(List<String> documentIds) {
            this.documentIds = documentIds;
            return this;
        }

        public Builder addDocumentId(String documentId) {
            this.documentIds.add(documentId);
            return this;
        }

        public SearchByIdParam build() {
            if (documentIds == null || documentIds.isEmpty()) {
                throw new ParamException("SearchByVectorsBuilder error: documentIds is empty");
            }
            return new SearchByIdParam(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
