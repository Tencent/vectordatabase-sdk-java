package com.tencent.tcvectordb.model.param.dml;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionViewConditionParam {
    private List<String> documnetSetIds;
    private List<String> documnetSetNames;
    private String filter;

    private int limit;
    private int offset;

    public CollectionViewConditionParam(Builder builder) {
        this.documnetSetIds = builder.documnetSetIds;
        this.documnetSetNames = builder.documnetSetNames;
        this.filter = builder.filter.getCond();
        this.limit = builder.limit;
        this.offset = builder.offset;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> documnetSetIds;
        private List<String> documnetSetNames;
        private Filter filter;

        private int limit;
        private int offset;

        private Builder() {
        }

        public Builder withDocumnetSetIds(List<String> documnetSetIds) {
            this.documnetSetIds = documnetSetIds;
            return this;
        }

        public Builder withDocumnetSetNames(List<String> documnetSetNames) {
            this.documnetSetNames = documnetSetNames;
            return this;
        }

        public Builder withFilter(Filter filter) {
            this.filter = filter;
            return this;
        }
        public Builder withLimit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder withOffset(int offset) {
            this.offset = offset;
            return this;
        }

        public CollectionViewConditionParam build(){
            return new CollectionViewConditionParam(this);
        }
    }

    public List<String> getDocumnetSetIds() {
        return documnetSetIds;
    }

    public void setDocumnetSetIds(List<String> documnetSetIds) {
        this.documnetSetIds = documnetSetIds;
    }

    public List<String> getDocumnetSetNames() {
        return documnetSetNames;
    }

    public void setDocumnetSetNames(List<String> documnetSetNames) {
        this.documnetSetNames = documnetSetNames;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
