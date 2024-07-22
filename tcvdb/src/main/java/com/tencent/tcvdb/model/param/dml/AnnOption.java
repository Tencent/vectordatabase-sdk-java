package com.tencent.tcvdb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvdb.exception.ParamException;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnnOption {
    private String fieldName;
    private List<Object> data;
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

    public AnnOption(Builder builder) {
        this.fieldName = builder.fieldName;
        this.data = builder.data;
        this.params = builder.params;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String fieldName;
        private List<Object> data;
        private Params params;

        private Builder() {
        }

        public Builder withFieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public Builder withData(List<Object> data){
            this.data = data;
            return this;
        }

        public Builder withParam(Params params){
            this.params = params;
            return this;
        }

        public AnnOption build() {
            if (fieldName == null || data.isEmpty()){
                throw new ParamException("RetrieveOption error: fieldName or data is null");
            }
            return new AnnOption(this);
        }
    }

}


