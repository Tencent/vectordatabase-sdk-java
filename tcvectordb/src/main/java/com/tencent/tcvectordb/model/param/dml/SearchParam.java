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

import java.util.Collections;
import java.util.List;

/**
 * Search Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class SearchParam {
    protected Params params;
    protected String filter;
    protected List<String> outputFields;
    protected boolean retrieveVector;
    protected int limit;
    protected Float radius;

    protected SearchParam(Builder<?> builder) {
        this.params = builder.params;
        if (builder.filter != null) {
            this.filter = builder.filter;
        }
        if (builder.outputFields != null && !builder.outputFields.isEmpty()) {
            this.outputFields = Collections.unmodifiableList(builder.outputFields);
        }
        this.retrieveVector = builder.retrieveVector;
        this.limit = builder.limit;
        this.radius = builder.radius;
    }


    public Params getParams() {
        return params;
    }


    public String getFilter() {
        return filter;
    }

    public List<String> getOutputFields() {
        return outputFields;
    }

    public boolean isRetrieveVector() {
        return retrieveVector;
    }

    public int getLimit() {
        return limit;
    }


    protected static abstract class Builder<T extends Builder<T>> {
        protected Params params;
        protected String filter;
        protected List<String> outputFields;
        protected boolean retrieveVector = false;
        protected int limit = 10;
        protected Float radius;


        protected Builder() {
        }

        protected abstract T self();

        public T withParams(Params params) {
            this.params = params;
            return self();
        }


        public T withOutputFields(List<String> outputFields) {
            this.outputFields = outputFields;
            return self();
        }

        public T withFilter(Filter filter) {
            if (filter != null) {
                this.filter = filter.getCond();
            }
            return self();
        }

        public T withFilter(String filter) {
            this.filter = filter;
            return self();
        }

        public T withRetrieveVector(boolean retrieveVector) {
            this.retrieveVector = retrieveVector;
            return self();
        }

        public T withLimit(int limit) {
            this.limit = limit;
            return self();
        }
    }
}
