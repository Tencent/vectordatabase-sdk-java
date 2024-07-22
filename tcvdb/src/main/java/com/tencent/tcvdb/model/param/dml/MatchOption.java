package com.tencent.tcvdb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvdb.exception.ParamException;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchOption {
    private String fieldName;
    private List<Object> data;
    private Integer limit;

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

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public MatchOption(Builder builder) {
        this.fieldName = builder.fieldName;
        this.data = builder.data;
        this.limit = builder.limit;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String fieldName;
        private List<Object> data;
        private Integer limit;

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

        public Builder withLimit(Integer limit){
            this.limit = limit;
            return this;
        }

        public MatchOption build() {
            if (fieldName == null || data.isEmpty()){
                throw new ParamException("RetrieveOption error: fieldName or data is null");
            }
            return new MatchOption(this);
        }
    }

}


