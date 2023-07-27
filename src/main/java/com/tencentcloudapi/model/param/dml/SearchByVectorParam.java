package com.tencentcloudapi.model.param.dml;

import com.tencentcloudapi.exception.ParamException;

import java.util.ArrayList;
import java.util.List;

/**
 * Search By Vector Param
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class SearchByVectorParam extends SearchParam {

    private SearchByVectorParam() {
    }

    public static SearchByVectorsBuilder newBuilder() {
        return new SearchByVectorsBuilder();
    }

    public static class SearchByVectorsBuilder extends BaseBuilder {
        private List<List<Float>> vectors;

        private SearchByVectorsBuilder() {
            this.vectors = new ArrayList<>();
        }

        public SearchByVectorsBuilder withVectors(List<List<Float>> vectors) {
            this.vectors = vectors;
            return this;
        }

        public SearchByVectorsBuilder addVector(List<Float> vector) {
            this.vectors.add(vector);
            return this;
        }

        public SearchByVectorsBuilder withHNSWSearchParams(HNSWSearchParams params) {
            super.params = params;
            return this;
        }

        public SearchByVectorsBuilder withFilter(Filter filter) {
            super.filter = filter;
            return this;
        }

        public SearchByVectorsBuilder withRetrieveVector(boolean retrieveVector) {
            super.retrieveVector = retrieveVector;
            return this;
        }

        public SearchByVectorsBuilder withLimit(int limit) {
            super.limit = limit;
            return this;
        }

        public SearchByVectorParam build() {

            if (vectors == null || vectors.isEmpty()) {
                throw new ParamException("SearchByVectorsBuilder error: vectors is empty");
            }
            SearchByVectorParam searchParam = new SearchByVectorParam();
            searchParam.vectors = this.vectors;
            searchParam.params = this.params;
            searchParam.filter = this.filter.getCond();
            searchParam.retrieveVector = this.retrieveVector;
            searchParam.limit = this.limit;
            return searchParam;
        }
    }

    public static class SearchBuilder extends BaseBuilder {
        private List<List<Float>> vectors;
        private List<String> documentIds;

        public SearchBuilder withVectors(List<List<Float>> vectors) {
            this.vectors = vectors;
            return this;
        }

        public SearchBuilder addVector(List<Float> vector) {
            this.vectors.add(vector);
            return this;
        }

        public SearchBuilder withDocumentIds(List<String> documentIds) {
            this.documentIds = documentIds;
            return this;
        }

        public SearchBuilder withHNSWSearchParams(HNSWSearchParams params) {
            super.params = params;
            return this;
        }

        public SearchBuilder withFilter(Filter filter) {
            super.filter = filter;
            return this;
        }

        public SearchBuilder withRetrieveVector(boolean retrieveVector) {
            super.retrieveVector = retrieveVector;
            return this;
        }

        public SearchBuilder withLimit(int limit) {
            super.limit = limit;
            return this;
        }

        public SearchByVectorParam build() {
            if ((documentIds == null || documentIds.isEmpty())
                    && (vectors == null || vectors.isEmpty())) {
                throw new ParamException("SearchByVectorsBuilder error: documentIds and vectors is empty");
            }
            SearchByVectorParam searchParam = new SearchByVectorParam();
            searchParam.vectors = this.vectors;
            searchParam.params = this.params;
            searchParam.filter = this.filter.getCond();
            searchParam.retrieveVector = this.retrieveVector;
            searchParam.limit = this.limit;
            return searchParam;
        }
    }
}
