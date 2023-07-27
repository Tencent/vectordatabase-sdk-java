package com.tencentcloudapi.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencentcloudapi.exception.ParamException;
import com.tencentcloudapi.model.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Insert Param
 * User: wlleiiwang
 * Date: 2023/7/26
 */
public class InsertParam {
    private List<Document> documents;

    private InsertParam(Builder builder) {
        this.documents = builder.documents;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private List<Document> documents;

        public Builder() {
            this.documents = new ArrayList<>();
        }

        public Builder withDocuments(List<Document> documents) {
            this.documents = documents;
            return this;
        }

        public Builder addDocument(Document document) {
            this.documents.add(document);
            return this;
        }

        public InsertParam build() {
            if (this.documents.isEmpty()) {
                throw new ParamException("InsertParam error: documents is empty");
            }
            return new InsertParam(this);
        }
    }
}
