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

package com.tencent.tcvectordb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.exception.ParamException;

import java.util.ArrayList;
import java.util.List;

/**
 * Search By Vector Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchByContentsParam {
    private String content;
    private SearchContenOption options;
    private Params params;
    private String filter;
    private List<String> outputFields;
    private List<String> documentSetName;
    private Integer limit;

    public SearchContenOption getOptions() {
        return options;
    }

    public void setOptions(SearchContenOption options) {
        this.options = options;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
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

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<String> getDocumentSetName() {
        return documentSetName;
    }

    public void setDocumentSetName(List<String> documentSetName) {
        this.documentSetName = documentSetName;
    }

    private SearchByContentsParam(Builder builder) {
        this.content = builder.content;
        this.options = builder.searchContenOption;
        this.params = builder.params;
        this.filter = builder.filter;
        this.outputFields = builder.outputFields;
        this.limit = builder.limit;
        this.documentSetName = builder.documentSetName;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder{
        private String content;
        private SearchContenOption searchContenOption;
        private Params params;
        private String filter;
        private List<String> outputFields;
        protected Integer limit;

        private List<String> documentSetName;

        private Builder() {
            this.content = "";
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        public Builder withSearchContentOption(SearchContenOption searchContenOption) {
            this.searchContenOption = searchContenOption;
            return this;
        }

        public Builder withParams(Params params) {
            this.params = params;
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

        public Builder withLimit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder withDocumentSerName(List<String> documentSetName) {
            this.documentSetName = documentSetName;
            return this;
        }


        public SearchByContentsParam build() {
            if (content.isEmpty()) {
                throw new ParamException("SearchByContentsParam error: content is empty");
            }
            return new SearchByContentsParam(this);
        }
    }
}
