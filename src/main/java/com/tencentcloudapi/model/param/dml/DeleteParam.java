package com.tencentcloudapi.model.param.dml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencentcloudapi.exception.ParamException;

import java.util.List;

/**
 * Delete Param
 * User: wlleiiwang
 * Date: 2023/7/26
 */
public class DeleteParam {
    private List<String> documentIds;

    public DeleteParam(Builder builder) {
        this.documentIds = builder.documentIds;
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

    public static class DeleteParamInner {
        private String database;
        private String collection;
        private DeleteParam query;

        public DeleteParamInner(String database, String collection, DeleteParam query) {
            this.database = database;
            this.collection = collection;
            this.query = query;
        }

        @Override
        public String toString() {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                throw new ParamException(String.format(
                        "DeleteParam error: %s", e.getMessage()));
            }
        }
    }
}
