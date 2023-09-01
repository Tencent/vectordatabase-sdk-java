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

package com.tencent.tcvectordb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tencent.tcvectordb.exception.ParamException;
import com.tencent.tcvectordb.model.param.collection.FieldType;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * VectorDB Document
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Document {
    private String id;
    private List<Double> vector;
    private Double score;
    private String doc;
    private List<DocField> otherFilterFields;

    public String getId() {
        return id;
    }

    public Double getScore() {
        return score;
    }

    public String getDoc() {
        return doc;
    }

    public List<DocField> getOtherFilterFields() {
        return otherFilterFields;
    }

    public List<Double> getVector() {
        return Collections.unmodifiableList(vector);
    }

    @Override
    public String toString() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("id", id);
        if (vector != null && !vector.isEmpty()) {
            ArrayNode vectorNode = JsonNodeFactory.instance.arrayNode();
            vector.forEach(vectorNode::add);
            node.set("vector", vectorNode);
        }
        if (score != null) {
            node.put("score", score);
        }
        if (StringUtils.isNotEmpty(doc)) {
            node.put("doc", doc);
        }
        if (otherFilterFields != null && !otherFilterFields.isEmpty()) {
            for (DocField field : otherFilterFields) {
                if (FieldType.Uint64.equals(field.getFieldType())) {
                    node.put(field.getName(), field.getLongValue());
                } else {
                    node.put(field.getName(), field.getStringValue());
                }
            }
        }
        return node.toString();
    }

    private Document(Builder builder) {
        this.id = builder.id;
        this.vector = builder.vector;
        this.doc = builder.doc;
        this.score = builder.score;
        this.otherFilterFields = builder.otherFilterFields;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private List<Double> vector;

        private Double score;
        private String doc;
        private List<DocField> otherFilterFields;

        public Builder() {
            this.otherFilterFields = new ArrayList<>();
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withVector(List<Double> vector) {
            this.vector = vector;
            return this;
        }

        public Builder withDoc(String doc) {
            this.doc = doc;
            return this;
        }

        public Builder withScore(Double score) {
            this.score = score;
            return this;
        }

        public Builder addFilterField(DocField field) {
            this.otherFilterFields.add(field);
            return this;
        }

        public Document build() {
            if (StringUtils.isEmpty(this.id)) {
                throw new ParamException("Document Create error: id is null");
            }
            return new Document(this);
        }
    }
}