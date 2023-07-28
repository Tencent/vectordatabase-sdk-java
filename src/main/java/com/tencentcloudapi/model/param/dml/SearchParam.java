package com.tencentcloudapi.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencentcloudapi.exception.ParamException;

import java.util.List;

/**
 * Search Param
 * User: wlleiiwang
 * Date: 2023/7/25
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
