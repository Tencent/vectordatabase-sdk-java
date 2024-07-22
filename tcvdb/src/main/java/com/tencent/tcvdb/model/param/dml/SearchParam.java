/*
 * Copyright (C) 2023 Tencent Cloud.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the vectordb-sdk-java), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tencent.tcvdb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.List;

/**
 * Search Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchParam {
    private List<AnnOption> ann;
    private List<MatchOption> match;
    private String filter;
    private List<String> outputFields;
    private boolean retrieveVector;
    private Integer limit;
    private RerankOption rerank;
    private ContextProcess contextProcess;

    public SearchParam(Builder builder) {
        this.ann = builder.ann;
        if (builder.filter != null) {
            this.filter = builder.filter.getCond();
        }
        if (builder.outputFields != null && !builder.outputFields.isEmpty()) {
            this.outputFields = Collections.unmodifiableList(builder.outputFields);
        }
        this.retrieveVector = builder.retrieveVector;
        this.limit = builder.limit;
        this.rerank = builder.rerank;
        this.contextProcess = builder.contextProcess;
        this.match = builder.match;
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

    public RerankOption getRerank() {
        return rerank;
    }

    public void setRerank(RerankOption rerank) {
        this.rerank = rerank;
    }

    public ContextProcess getContextProcess() {
        return contextProcess;
    }

    public void setContextProcess(ContextProcess contextProcess) {
        this.contextProcess = contextProcess;
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

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder{
        private List<AnnOption> ann;
        private Filter filter;
        private List<String> outputFields;
        private boolean retrieveVector;
        private int limit = 10;
        private RerankOption rerank;
        private ContextProcess contextProcess;
        private List<MatchOption> match;


        protected Builder() {
        }

        public Builder withAnn(List<AnnOption> ann) {
            this.ann = ann;
            return this;
        }

        public Builder withMatch(List<MatchOption> match) {
            this.match = match;
            return this;
        }

        public Builder withFilter(Filter filter) {
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
        public Builder withRerank(RerankOption rerank) {
            this.rerank = rerank;
            return this;
        }

        public Builder withLimit(Integer limit) {
            this.limit = limit;
            return this;
        }
        public Builder withContextProcess(ContextProcess contextProcess) {
            this.contextProcess = contextProcess;
            return this;
        }

        public SearchParam build() {
            return new SearchParam(this);
        }
    }
}
