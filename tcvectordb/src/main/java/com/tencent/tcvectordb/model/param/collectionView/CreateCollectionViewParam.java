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

package com.tencent.tcvectordb.model.param.collectionView;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvdbtext.exception.ParamException;
import com.tencent.tcvectordb.model.CollectionView;
import com.tencent.tcvectordb.model.param.collection.IndexField;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create Collection Param
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CreateCollectionViewParam extends CollectionView {

    private CreateCollectionViewParam(Builder builder) {
        this.collectionView = builder.name;
        this.description = builder.description;
        this.splitterPreprocess = builder.splitterPreprocess;
        this.embedding = builder.embedding;
        this.indexes = builder.indexes;
        this.expectedFileNum = builder.expectedFileNum;
        this.averageFileSize = builder.averageFileSize;
    }
    public SplitterPreprocessParams getSplitterPreprocess() {
        return splitterPreprocess;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String description;
        private final List<IndexField> indexes;
        private SplitterPreprocessParams splitterPreprocess;

        private EmbeddingParams embedding;
        private Integer expectedFileNum;
        private Integer averageFileSize;

        private Builder() {
            this.indexes = new ArrayList<>();
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder addField(IndexField field) {
            this.indexes.add(field);
            return this;
        }
        public Builder withEmbedding(EmbeddingParams embedding) {
            this.embedding = embedding;
            return this;
        }

        public Builder withSplitterPreprocess(SplitterPreprocessParams documentPreprocess) {
            this.splitterPreprocess = documentPreprocess;
            return this;
        }

        public Builder withAverageFileSize(int averageFileSize) {
            this.averageFileSize = averageFileSize;
            return this;
        }

        public Builder withExpectedFileNum(int expectedFileNum) {
            this.expectedFileNum = expectedFileNum;
            return this;
        }

        public CreateCollectionViewParam build() throws ParamException {
            if (StringUtils.isEmpty(this.name)) {
                throw new ParamException("ConnectParam error: name is null");
            }
            return new CreateCollectionViewParam(this);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
