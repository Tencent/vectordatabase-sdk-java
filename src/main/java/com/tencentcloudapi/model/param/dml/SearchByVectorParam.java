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

package com.tencentcloudapi.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencentcloudapi.exception.ParamException;

import java.util.ArrayList;
import java.util.List;

/**
 * Search By Vector Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchByVectorParam extends SearchParam {

    private SearchByVectorParam() {
    }

    public static SearchByVectorsBuilder newBuilder() {
        return new SearchByVectorsBuilder();
    }

    public static class SearchByVectorsBuilder extends BaseBuilder {
        private List<List<Double>> vectors;

        private SearchByVectorsBuilder() {
            this.vectors = new ArrayList<>();
        }

        public SearchByVectorsBuilder withVectors(List<List<Double>> vectors) {
            this.vectors = vectors;
            return this;
        }

        public SearchByVectorsBuilder addVector(List<Double> vector) {
            this.vectors.add(vector);
            return this;
        }

        public SearchByVectorsBuilder withHNSWSearchParams(HNSWSearchParams params) {
            this.params = params;
            return this;
        }

        public SearchByVectorsBuilder withFilter(Filter filter) {
            this.filter = filter;
            return this;
        }

        public SearchByVectorsBuilder withRetrieveVector(boolean retrieveVector) {
            this.retrieveVector = retrieveVector;
            return this;
        }

        public SearchByVectorsBuilder withLimit(int limit) {
            this.limit = limit;
            return this;
        }

        public SearchByVectorParam build() {

            if (vectors == null || vectors.isEmpty()) {
                throw new ParamException("SearchByVectorsBuilder error: vectors is empty");
            }
            SearchByVectorParam searchParam = new SearchByVectorParam();
            searchParam.vectors = this.vectors;
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
