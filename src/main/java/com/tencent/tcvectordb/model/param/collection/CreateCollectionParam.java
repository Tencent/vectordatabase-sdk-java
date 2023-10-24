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

package com.tencent.tcvectordb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.exception.ParamException;
import com.tencent.tcvectordb.model.collection.Collection;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create Collection Param
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CreateCollectionParam extends Collection {

    private CreateCollectionParam(Builder builder) {
        this.collection = builder.name;
        this.replicaNum = builder.replicaNum;
        this.shardNum = builder.shardNum;
        this.description = builder.description;
        this.indexes = builder.indexes;
        this.embedding = builder.embedding;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private int replicaNum = 2;
        private int shardNum = 1;
        private String description;

        private Embedding embedding;
        private final List<IndexField> indexes;

        private Builder() {
            this.indexes = new ArrayList<>();
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withReplicaNum(int replicaNum) {
            this.replicaNum = replicaNum;
            return this;
        }

        public Builder withShardNum(int shardNum) {
            this.shardNum = shardNum;
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

        public Builder withEmbedding(Embedding embedding) {
            this.embedding = embedding;
            return this;
        }

        public CreateCollectionParam build() throws ParamException {
            if (StringUtils.isEmpty(this.name)) {
                throw new ParamException("ConnectParam error: name is null");
            }
            if (this.indexes.isEmpty()) {
                throw new ParamException("ConnectParam error: indexes is empty");
            }
            return new CreateCollectionParam(this);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
