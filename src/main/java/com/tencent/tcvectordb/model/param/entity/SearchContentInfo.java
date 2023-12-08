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

package com.tencent.tcvectordb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tencent.tcvectordb.model.DocField;

import java.util.*;

/**
 * VectorDB Document
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchContentInfo {
    private Double score;
    private ContentInfo data;
    private List<DocField> docFields;
    private Map<String, Object> docKeyValue;

    public Double getScore() {
        return score;
    }

    public List<DocField> getDocFields() {
        return docFields;
    }

    public Map<String, Object> getDocKeyValue() {
        return docKeyValue;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void setDocFields(List<DocField> docFields) {
        this.docFields = docFields;
    }

    public ContentInfo getData() {
        return data;
    }

    public void setData(ContentInfo data) {
        this.data = data;
    }

    public Object getObject(String key) {
        if (Objects.isNull(docFields) || docFields.isEmpty()) {
            return null;
        }
        ensureDocKeyValue();

        return docKeyValue.get(key);
    }

    private void ensureDocKeyValue() {
        if (Objects.isNull(docKeyValue)) {
            docKeyValue = new TreeMap<>();
            for (DocField docField : docFields) {
                docKeyValue.put(docField.getName(), docField.getValue());
            }
        }
    }

    @Override
    public String toString() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        if (score != null) {
            node.put("score", score);
        }
        if (data != null) {
            node.put("data", data.toString());
        }
        if (docFields != null && !docFields.isEmpty()) {
            for (DocField field : docFields) {
                switch (field.getFieldType()) {
                    case Uint64:
                        node.put(field.getName(), Long.valueOf(field.getStringValue()));
                        break;
                    case Array:
                        List<String> strValues = (List<String>) ((List) field.getValue());
                        ArrayNode strNode = JsonNodeFactory.instance.arrayNode();
                        strValues.forEach(strNode::add);
                        node.set(field.getName(), strNode);
                        break;
                    default:
                        node.put(field.getName(), field.getStringValue());
                }
            }
        }
        return node.toString();
    }

    private SearchContentInfo(Builder builder) {
        this.score = builder.score;
        this.docFields = builder.docFields;
        this.data = builder.data;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Double score;
        private ContentInfo data;
        private List<DocField> docFields;

        public Builder() {
            this.docFields = new ArrayList<>();
        }

        public Builder withSearchContentInfo(ContentInfo data){
            this.data = data;
            return this;
        }

        public Builder withScore(Double score) {
            this.score = score;
            return this;
        }

        public Builder addDocField(DocField docField) {
            this.docFields.add(docField);
            return this;
        }

        public Builder addDocFields(List<DocField> docFields) {
            this.docFields.addAll(docFields);
            return this;
        }

        public SearchContentInfo build() {
            return new SearchContentInfo(this);
        }
    }
}