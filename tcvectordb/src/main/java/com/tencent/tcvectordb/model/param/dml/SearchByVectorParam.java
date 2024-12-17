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
import java.util.stream.Collectors;

/**
 * Search By Vector Param
 * Params:
 *  SearchParam: SearchParam.class include search base params
 *  vectors: List<List<Double>>, search documents by the vectors
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchByVectorParam extends SearchParam {
    private List<List<? extends Number>> vectors;

    private SearchByVectorParam(Builder builder) {
        super(builder);
        this.vectors = builder.vectors;
    }

    public List<List<? extends Number>> getVectors() {
        return vectors;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends SearchParam.Builder<Builder> {
        private List<List<? extends Number>> vectors;

        private Builder() {
            super();
            this.vectors = new ArrayList<>();
        }

        public Builder withVectors(List<? extends List<? extends Number>> vectors) {
            this.vectors = vectors.stream().collect(Collectors.toList());
            return this;
        }

        public Builder addVector(List<? extends Number> vector) {
            this.vectors.add(vector);
            return this;
        }

        public SearchByVectorParam build() {
            if (vectors == null || vectors.isEmpty()) {
                throw new ParamException("SearchByVectorsBuilder error: vectors is empty");
            }
            return new SearchByVectorParam(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
