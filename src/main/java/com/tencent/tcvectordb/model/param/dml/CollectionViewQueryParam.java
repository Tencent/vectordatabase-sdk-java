package com.tencent.tcvectordb.model.param.dml;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionViewQueryParam extends CollectionViewConditionParam{
    private List<String> outputFields;

    private Integer limit;
    private Integer offset;

    public CollectionViewQueryParam(Builder builder) {
        super();
        this.limit = builder.limit;
        this.offset = builder.offset;
        this.outputFields = builder.outputFields;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends CollectionViewConditionParam.Builder {

        private Integer limit;
        private Integer offset;
        private List<String> outputFields;
        private List<String> documentSetIds;
        private List<String> documentSetNames;
        private String filter;
        public Builder withDocumentSetIds(List<String> documentSetIds) {
            this.documentSetIds = documentSetIds;
            return this;
        }

        public Builder withDocumentSetNames(List<String> documentSetNames) {
            this.documentSetNames = documentSetNames;
            return this;
        }

        public Builder withFilter(Filter filter) {
            this.filter = filter.getCond();
            return this;
        }
        public Builder withLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public Builder withOffset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public Builder withOutputFields(List<String> outputFields) {
            this.outputFields = outputFields;
            return this;
        }

        public CollectionViewQueryParam build(){
            return new CollectionViewQueryParam(this);
        }
    }

    public List<String> getDocumentSetId() {
        return documentSetId;
    }

    public void setDocumentSetId(List<String> documentSetId) {
        this.documentSetId = documentSetId;
    }

    public List<String> getDocumentSetName() {
        return documentSetName;
    }

    public void setDocumentSetName(List<String> documentSetName) {
        this.documentSetName = documentSetName;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<String> getOutputFields() {
        return outputFields;
    }

    public void setOutputFields(List<String> outputFields) {
        this.outputFields = outputFields;
    }
}
