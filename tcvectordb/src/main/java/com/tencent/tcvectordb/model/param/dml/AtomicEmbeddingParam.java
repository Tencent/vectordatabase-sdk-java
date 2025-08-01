package com.tencent.tcvectordb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AtomicEmbeddingParam {
    private String model;
    private String dataType;

    private ModelParam modelParams;
    private List<String> data;

    public AtomicEmbeddingParam(Builder builder) {
        this.model = builder.model;
        this.dataType = builder.dataType;
        this.modelParams = builder.modelParam;
        this.data = builder.data;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public ModelParam getModelParams() {
        return modelParams;
    }

    public void setModelParams(ModelParam modelParams) {
        this.modelParams = modelParams;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    // builder pattern

    public static class Builder {
        private String model;
        private String dataType;
        private ModelParam modelParam;
        private List<String> data;

        public Builder() {
        }

        public Builder withModel(String model) {
            this.model = model;
            return this;
        }

        public Builder withDataType(String dataType) {
            this.dataType = dataType;
            return this;
        }

        public Builder withModelParam(ModelParam modelParam) {
            this.modelParam = modelParam;
            return this;
        }

        public Builder withData(List<String> data) {
            this.data = data;
            return this;
        }

        public AtomicEmbeddingParam build() {
            return new AtomicEmbeddingParam(this);
        }
    }
}
