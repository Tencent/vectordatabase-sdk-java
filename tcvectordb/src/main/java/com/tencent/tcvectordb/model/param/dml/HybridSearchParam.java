package com.tencent.tcvectordb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * HybridSearchParam hybrid search param
 *  Params:
 *      ann(List<AnnOption>): ann options, annOption used for vector search,
 *      match(List<MatchOption>): match options, matchOption used for sparse vector search
 *      retrieve_vector(bool): Whether to return vector and sparse vector values.
 *      filter(Filter): filter rows before return result
 *      document_ids(List): filter rows by id list
 *      output_fields(List): return columns by column name list
 *      Limit(int): limit the number of rows returned
 *      rerank(RerankParam): rerank param, RRFRerankParam or WeightRerankParam
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HybridSearchParam {
    private List<AnnOption> ann;
    private List<MatchOption> match;

    private Boolean isArrayParam;
    private String filter;
    private List<String> outputFields;
    private boolean retrieveVector;
    private Integer limit;
    private RerankParam rerank;

    public HybridSearchParam(Builder builder) {
        this.ann = builder.ann;
        if (builder.filter != null) {
            this.filter = builder.filter;
        }
        if (builder.outputFields != null && !builder.outputFields.isEmpty()) {
            this.outputFields = Collections.unmodifiableList(builder.outputFields);
        }
        this.retrieveVector = builder.retrieveVector;
        this.limit = builder.limit;
        this.rerank = builder.rerank;
        this.match = builder.match;
        this.isArrayParam = builder.isAnnArrayParam | builder.isMatchArrayParam;
    }

    public List<AnnOption> getAnn() {
        return ann;
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

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public RerankParam getRerank() {
        return rerank;
    }

    public void setRerank(RerankParam rerank) {
        this.rerank = rerank;
    }

    public void setAnn(List<AnnOption> ann) {
        this.ann = ann;
    }

    public List<MatchOption> getMatch() {
        return match;
    }

    public void setMatch(List<MatchOption> match) {
        this.match = match;
    }

    public Boolean getIsArrayParam() {
        return isArrayParam;
    }
    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder{
        private List<AnnOption> ann;
        private String filter;
        private List<String> outputFields;
        private boolean retrieveVector;
        private int limit = 10;
        private RerankParam rerank;
        private List<MatchOption> match;

        private Boolean isAnnArrayParam = false;
        private Boolean isMatchArrayParam = false;

        protected Builder() {
        }

        public Builder withAnn(List<AnnOption> ann) {
            this.ann = ann;
            isAnnArrayParam = true;
            return this;
        }

        public Builder withMatch(List<MatchOption> match) {
            this.match = match;
            isMatchArrayParam = true;
            return this;
        }

        public Builder withAnn(AnnOption ann) {
            isAnnArrayParam = false;
            this.ann = Collections.singletonList(ann);
            return this;
        }

        public Builder withMatch(MatchOption match) {
            isMatchArrayParam = false;
            this.match = Collections.singletonList(match);
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
        public Builder withRerank(RerankParam rerank) {
            this.rerank = rerank;
            return this;
        }

        public Builder withLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public HybridSearchParam build() {
            return new HybridSearchParam(this);
        }
    }
}
