package com.tencent.tcvectordb.model.param.dml;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionViewConditionParam {
    protected List<String> documentSetId;
    protected List<String> documentSetName;
    protected String filter;

    public CollectionViewConditionParam(Builder builder) {
        this.documentSetId = builder.documentSetIds;
        this.documentSetName = builder.documentSetNames;
        this.filter = builder.filter;
    }

    public CollectionViewConditionParam() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
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

        public CollectionViewConditionParam build(){
            return new CollectionViewConditionParam(this);
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
}
