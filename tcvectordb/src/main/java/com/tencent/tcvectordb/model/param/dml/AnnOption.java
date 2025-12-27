package com.tencent.tcvectordb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * AnnOption for ann vector search
 * Params:
 *     fieldName: String, field name of the vector field, value must be "vector".
 *     data: List<Double>, vector data,
 *     documentIds: List<String>, document ids used for search.
 * eg:
 *     AnnOption option = new AnnOption.Builder().withFieldName("vector").withData(vector).build();
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnnOption {
    private String fieldName;
    private List<Object> data;
    private List<String> documentIds;
    private Params params;

    private  Integer limit;

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

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public AnnOption(Builder builder) {
        this.fieldName = builder.fieldName;
        this.data = builder.data;
        this.params = builder.params;
        this.documentIds = builder.documentIds;
        this.limit = builder.limit;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String fieldName;
        private List<Object> data;
        private Params params;

        private List<String> documentIds;

        private Integer limit;

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

        public Builder withTextData(String texts){
            this.data = Collections.singletonList(texts);
            return this;
        }

        public Builder withTextData(List<String> texts){
            this.data = Collections.singletonList(texts);
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

        public Builder withLimit(Integer limit){
            this.limit = limit;
            return this;
        }
        public AnnOption build() {
            return new AnnOption(this);
        }
    }

}
