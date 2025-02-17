package com.tencent.tcvectordb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetImageUrlParam {
    private String fileName;
    private List<String> documentIds;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<String> documentIds) {
        this.documentIds = documentIds;
    }

    // builder
    public static Builder newBuilder() {
        return new Builder();
    }
    public static class Builder {
        private String fileName;
        private List<String> documentIds;

        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder setDocumentIds(List<String> documentIds) {
            this.documentIds = documentIds;
            return this;
        }

        public GetImageUrlParam build() {
            GetImageUrlParam param = new GetImageUrlParam();
            param.setFileName(this.fileName);
            param.setDocumentIds(this.documentIds);
            return param;
        }
    }
}
