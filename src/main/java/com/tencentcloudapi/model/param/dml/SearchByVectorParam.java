package com.tencentcloudapi.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencentcloudapi.exception.ParamException;

import java.util.ArrayList;
import java.util.List;

/**
 * Search By Vector Param
 * User: wlleiiwang
 * Date: 2023/7/25
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
