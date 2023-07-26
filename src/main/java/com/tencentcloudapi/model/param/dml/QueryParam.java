package com.tencentcloudapi.model.param.dml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencentcloudapi.exception.ParamException;

import java.util.List;

/**
 * Query Param
 * User: wlleiiwang
 * Date: 2023/7/26
 */
public class QueryParam {
    private List<String> documentIds;
    private boolean retrieveVector;

    public QueryParam(Builder builder) {
        this.documentIds = builder.documentIds;
        this.retrieveVector = builder.retrieveVector;
    }

    public static class Builder {
        private List<String> documentIds;
        private boolean retrieveVector = false;

        public Builder() {
        }

        public Builder withDocumentIds(List<String> documentIds) {
            this.documentIds = documentIds;
            return this;
        }

        public Builder withRetrieveVector(boolean retrieveVector) {
            this.retrieveVector = retrieveVector;
            return this;
        }

        public QueryParam build() {
            if (documentIds == null || documentIds.isEmpty()) {
                throw new ParamException("QueryParam error: documentIds is null");
            }
            return new QueryParam(this);
        }
    }
}
