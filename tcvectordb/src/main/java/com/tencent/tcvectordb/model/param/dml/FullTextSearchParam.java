package com.tencent.tcvectordb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.List;

/**
 * FullTextSearchParam full text  search param
 *  Params:
 *      match(MatchOption): match options, matchOption used for full text  search
 *      retrieve_vector(bool): Whether to return vector and sparse vector values.
 *      filter(Filter): filter rows before return result
 *      output_fields(List): return columns by column name list
 *      Limit(int): limit the number of rows returned
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FullTextSearchParam {
    private MatchParam match;
    private String filter;
    private List<String> outputFields;
    private boolean retrieveVector;
    private Integer limit;

    public FullTextSearchParam(Builder builder) {
        if (builder.filter != null) {
            this.filter = builder.filter;
        }
        if (builder.outputFields != null && !builder.outputFields.isEmpty()) {
            this.outputFields = Collections.unmodifiableList(builder.outputFields);
        }
        this.retrieveVector = builder.retrieveVector;
        this.limit = builder.limit;
        this.match = builder.match;
    }

    public MatchParam getMatch() {
        return match;
    }

    public void setMatch(MatchParam match) {
        this.match = match;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<String> getOutputFields() {
        return outputFields;
    }

    public void setOutputFields(List<String> outputFields) {
        this.outputFields = outputFields;
    }

    public boolean isRetrieveVector() {
        return retrieveVector;
    }

    public void setRetrieveVector(boolean retrieveVector) {
        this.retrieveVector = retrieveVector;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder{
        private String filter;
        private List<String> outputFields;
        private boolean retrieveVector;
        private int limit = 10;
        private MatchParam match;

        protected Builder() {
        }

        public Builder withMatch(MatchParam match) {
            this.match = match;
            return this;
        }


        public Builder withFilter(Filter filter) {
            this.filter = filter.getCond();
            return this;
        }

        public Builder withFilter(String filter) {
            this.filter = filter;
            return this;
        }

        public Builder withOutputFields(List<String> outputFields) {
            this.outputFields = outputFields;
            return this;
        }
        public Builder withRetrieveVector(boolean retrieveVector) {
            this.retrieveVector = retrieveVector;
            return this;
        }

        public Builder withLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public FullTextSearchParam build() {
            return new FullTextSearchParam(this);
        }
    }
}
