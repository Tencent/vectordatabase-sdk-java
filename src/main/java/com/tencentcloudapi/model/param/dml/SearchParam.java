package com.tencentcloudapi.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    public List<List<Float>> getVectors() {
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
}
