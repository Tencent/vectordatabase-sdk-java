package com.tencent.tcvectordb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnnOption {
    private String fieldName;
    private List<Object> data;
    private List<String> documentIds;
    private Params params;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public List<String> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<String> documentIds) {
        this.documentIds = documentIds;
    }

    public AnnOption(Builder builder) {
        this.fieldName = builder.fieldName;
        this.data = builder.data;
        this.params = builder.params;
        this.documentIds = builder.documentIds;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String fieldName;
        private List<Object> data;
        private Params params;

        private List<String> documentIds;

        private Builder() {

        }

        public Builder withFieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }
        public Builder withData(List<Double> vector){
            this.data = Arrays.asList(vector);
            return this;
        }

        public Builder withParam(Params params){
            this.params = params;
            return this;
        }
        public Builder withDocumentIds(List<String> documentIds){
            this.documentIds = documentIds;
            return this;
        }

        public AnnOption build() {
            return new AnnOption(this);
        }
    }

}
