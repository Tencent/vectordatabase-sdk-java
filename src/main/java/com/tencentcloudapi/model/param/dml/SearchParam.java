package com.tencentcloudapi.model.param.dml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencentcloudapi.exception.ParamException;

import java.util.List;

/**
 * Search Param
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class SearchParam {
    List<List<Float>> vectors;
    List<String> documentIds;
    HNSWSearchParams params;
    String filter;
    boolean retrieveVector;
    int limit;

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new ParamException(String.format(
                    "InsertParam error: %s", e.getMessage()));
        }
    }

    protected SearchParam() {
    }

    protected static class BaseBuilder {
        HNSWSearchParams params;
        Filter filter;
        boolean retrieveVector = false;
        int limit = 10;
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

        public SearchParam build() {
            if ((documentIds == null || documentIds.isEmpty())
                    && (vectors == null || vectors.isEmpty())) {
                throw new ParamException("SearchByVectorsBuilder error: documentIds and vectors is empty");
            }
            SearchParam searchParam = new SearchParam();
            searchParam.vectors = this.vectors;
            searchParam.documentIds = this.documentIds;
            searchParam.params = this.params;
            searchParam.filter = this.filter.getCond();
            searchParam.retrieveVector = this.retrieveVector;
            searchParam.limit = this.limit;
            return searchParam;
        }
    }

    public static class SearchParamInner {
        private String database;
        private String collection;
        private SearchParam search;

        public SearchParamInner(String database, String collection, SearchParam search) {
            this.database = database;
            this.collection = collection;
            this.search = search;
        }

        @Override
        public String toString() {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                throw new ParamException(String.format(
                        "InsertParam error: %s", e.getMessage()));
            }
        }
    }
}
