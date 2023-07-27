package com.tencentcloudapi.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencentcloudapi.exception.ParamException;

import java.util.List;

/**
 * Delete Param
 * User: wlleiiwang
 * Date: 2023/7/26
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteParam {
    private List<String> documentIds;

    public DeleteParam(Builder builder) {
        this.documentIds = builder.documentIds;
    }

    public List<String> getDocumentIds() {
        return documentIds;
    }

    public static DeleteParam.Builder newBuilder() {
        return new DeleteParam.Builder();
    }

    public static class Builder {
        private List<String> documentIds;

        public Builder() {
        }

        public Builder withDocumentIds(List<String> documentIds) {
            this.documentIds = documentIds;
            return this;
        }

        public DeleteParam build() {
            if (documentIds == null || documentIds.isEmpty()) {
                throw new ParamException("DeleteParam error: documentIds is null");
            }
            return new DeleteParam(this);
        }
    }
}
