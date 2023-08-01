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

package com.tencentcloudapi.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencentcloudapi.exception.ParamException;

import java.util.List;

/**
 * Delete Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteParam {
    private List<String> documentIds;

    public DeleteParam(Builder builder) {
        this.documentIds = builder.documentIds;
    }

    public List<String> getDocumentIds() {
        return documentIds;
    }

    public static DeleteParam.Builder newBuilder() {
        return new DeleteParam.Builder();
    }

    public static class Builder {
        private List<String> documentIds;

        public Builder() {
        }

        public Builder withDocumentIds(List<String> documentIds) {
            this.documentIds = documentIds;
            return this;
        }

        public DeleteParam build() {
            if (documentIds == null || documentIds.isEmpty()) {
                throw new ParamException("DeleteParam error: documentIds is null");
            }
            return new DeleteParam(this);
        }
    }
}
