package com.tencentcloudapi.model.param.dml;

import com.tencentcloudapi.exception.ParamException;

import java.util.List;

/**
 * Search By Id Param
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class SearchByIdParam extends SearchParam {

    private SearchByIdParam() {
    }

    public static class SearchByIdsBuilder extends BaseBuilder {
        private List<String> documentIds;

        private SearchByIdsBuilder() {
        }

        public SearchByIdsBuilder withDocumentIds(List<String> documentIds) {
            this.documentIds = documentIds;
            return this;
        }

        public SearchByIdsBuilder withHNSWSearchParams(HNSWSearchParams params) {
            super.params = params;
            return this;
        }

        public SearchByIdsBuilder withFilter(Filter filter) {
            super.filter = filter;
            return this;
        }

        public SearchByIdsBuilder withRetrieveVector(boolean retrieveVector) {
            super.retrieveVector = retrieveVector;
            return this;
        }

        public SearchByIdsBuilder withLimit(int limit) {
            super.limit = limit;
            return this;
        }

        public SearchByIdParam build() {
            if (documentIds == null || documentIds.isEmpty()) {
                throw new ParamException("SearchByVectorsBuilder error: documentIds is empty");
            }
            SearchByIdParam searchParam = new SearchByIdParam();
            searchParam.documentIds = this.documentIds;
            searchParam.params = this.params;
            searchParam.filter = this.filter.getCond();
            searchParam.retrieveVector = this.retrieveVector;
            searchParam.limit = this.limit;
            return searchParam;
        }
    }
}
