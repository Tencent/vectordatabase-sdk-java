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

import java.util.List;

/**
 * Search Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchParam {
    List<List<Double>> vectors;
    List<String> documentIds;
    HNSWSearchParams params;
    String filter;
    boolean retrieveVector;
    int limit;

    public List<List<Double>> getVectors() {
        return vectors;
    }

    public List<String> getDocumentIds() {
        return documentIds;
    }

    public HNSWSearchParams getParams() {
        return params;
    }

    public String getFilter() {
        return filter;
    }

    public boolean isRetrieveVector() {
        return retrieveVector;
    }

    public int getLimit() {
        return limit;
    }

    protected SearchParam() {
    }

    public static BaseBuilder newBuilder() {
        return new BaseBuilder();
    }

    protected static class BaseBuilder {
        HNSWSearchParams params;
        Filter filter;
        boolean retrieveVector = false;
        int limit = 10;
    }

    public static class SearchBuilder extends BaseBuilder {
        private List<List<Double>> vectors;
        private List<String> documentIds;

        public SearchBuilder withVectors(List<List<Double>> vectors) {
            this.vectors = vectors;
            return this;
        }

        public SearchBuilder addVector(List<Double> vector) {
            this.vectors.add(vector);
            return this;
        }

        public SearchBuilder withDocumentIds(List<String> documentIds) {
            this.documentIds = documentIds;
            return this;
        }

        public SearchBuilder withHNSWSearchParams(HNSWSearchParams params) {
            this.params = params;
            return this;
        }

        public SearchBuilder withFilter(Filter filter) {
            this.filter = filter;
            return this;
        }

        public SearchBuilder withRetrieveVector(boolean retrieveVector) {
            this.retrieveVector = retrieveVector;
            return this;
        }

        public SearchBuilder withLimit(int limit) {
            this.limit = limit;
            return this;
        }

        public SearchParam build() {
            if ((documentIds == null || documentIds.isEmpty())
                    && (vectors == null || vectors.isEmpty())) {
                throw new ParamException("SearchByVectorsBuilder error: documentIds and vectors is empty");
            }
            SearchParam searchParam = new SearchParam();
            searchParam.vectors = this.vectors;
            searchParam.documentIds = this.documentIds;
            searchParam.params = this.params;
            if (this.filter != null) {
                searchParam.filter = this.filter.getCond();
            }
            searchParam.retrieveVector = this.retrieveVector;
            searchParam.limit = this.limit;
            return searchParam;
        }
    }
}
