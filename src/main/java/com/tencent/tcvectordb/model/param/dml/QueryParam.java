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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Query Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryParam extends BaseQuery {
    private boolean retrieveVector;
    private long limit;
    private long offset;
    @JsonIgnore
    private ReadConsistencyEnum readConsistency;
    private List<String> outputFields;


    public boolean isRetrieveVector() {
        return retrieveVector;
    }

    public long getLimit() {
        return limit;
    }

    public long getOffset() {
        return offset;
    }


    public ReadConsistencyEnum getReadConsistency() {
        return readConsistency;
    }

    public List<String> getOutputFields() {
        return outputFields;
    }

    public QueryParam(Builder builder) {
        super(builder);
        this.retrieveVector = builder.retrieveVector;
        this.limit = builder.limit;
        this.offset = builder.offset;
        this.readConsistency = builder.readConsistency;
        if (builder.outputFields != null && !builder.outputFields.isEmpty()) {
            this.outputFields = Collections.unmodifiableList(builder.outputFields);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends BaseQuery.Builder<Builder> {
        private boolean retrieveVector = false;
        /**
         * limit between 1 and 16384L
         * default is 10
         */
        private long limit = 10;
        /**
         * default is 0
         */
        private long offset = 0;
        private ReadConsistencyEnum readConsistency = ReadConsistencyEnum.EVENTUAL_CONSISTENCY;
        private List<String> outputFields;

        private Builder() {
            super();
            this.outputFields = new ArrayList<>();
        }

        @Override
        protected Builder self() {
            return this;
        }


        public Builder withRetrieveVector(boolean retrieveVector) {
            this.retrieveVector = retrieveVector;
            return this;
        }

        public Builder withLimit(long limit) {
            this.limit = limit;
            return this;
        }

        public Builder withOffset(long offset) {
            this.offset = offset;
            return this;
        }

        public Builder withReadConsistency(ReadConsistencyEnum readConsistency) {
            this.readConsistency = readConsistency;
            return this;
        }

        public Builder addOutputFields(String outputField) {
            this.outputFields.add(outputField);
            return this;
        }

        public Builder addAllOutputFields(List<String> outputFields) {
            this.outputFields.addAll(outputFields);
            return this;
        }

        public Builder withOutputFields(List<String> outputFields) {
            this.outputFields = outputFields;
            return this;
        }


        public QueryParam build() {
            return new QueryParam(this);
        }
    }
}
