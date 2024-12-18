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
import com.tencent.tcvdbtext.exception.ParamException;

/**
 * Delete Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteParam extends BaseQuery {
    private Integer limit;

    public DeleteParam(Builder builder) {
        super(builder);
        this.limit = builder.limit;
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

    public static class Builder extends BaseQuery.Builder<Builder> {
        private Integer limit;
        private Builder() {
            super();
        }

        public DeleteParam build() {
            if (this.limit !=null && this.limit == 0){
                throw new ParamException("The value of limit cannot be 0");
            }
            return new DeleteParam(this);
        }

        public Builder withLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
