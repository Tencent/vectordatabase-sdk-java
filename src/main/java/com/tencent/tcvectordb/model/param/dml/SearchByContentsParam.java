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
public class SearchByContentsParam extends SearchParam {
    private String content;
    private SearchContenOption options;

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

    private SearchByContentsParam(Builder builder) {
        super(builder);
        this.content = builder.content;
        this.options = builder.searchContenOption;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends SearchParam.Builder<Builder> {
        private String content;
        private SearchContenOption searchContenOption;
        private Builder() {
            super();
            this.content = "";
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }
        public Builder withSearchContenOption(SearchContenOption searchContenOption) {
            this.searchContenOption = searchContenOption;
            return this;
        }

        public SearchByContentsParam build() {
            if (content.isEmpty()) {
                throw new ParamException("SearchByContentsParam error: content is empty");
            }
            return new SearchByContentsParam(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
