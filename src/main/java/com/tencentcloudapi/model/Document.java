package com.tencentcloudapi.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tencentcloudapi.exception.ParamException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * VectorDB Document
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class Document {
    private String id;
    private List<Float> vector;
    private Float score;
    private String doc;
    private List<DocField> otherScalarFields;

    @Override
    public String toString() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("id", id);
        ArrayNode vectorNode = JsonNodeFactory.instance.arrayNode();
        vector.forEach(vectorNode::add);
        node.set("vector", vectorNode);
        if (score != null) {
            node.put("score", score);
        }
        if (StringUtils.isNotEmpty(doc)) {
            node.put("doc", doc);
        }
        if (!otherScalarFields.isEmpty()) {
            for (DocField field : otherScalarFields) {
                node.set(field.getName(), (JsonNode) field.getValue());
            }
        }
        return node.toString();
    }

    private Document(Builder builder) {
        this.id = builder.id;
        this.vector = builder.vector;
        this.otherScalarFields = builder.otherScalarFields;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private List<Float> vector;
        private List<DocField> otherScalarFields;

        public Builder() {
            this.otherScalarFields = new ArrayList<>();
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withVector(List<Float> vector) {
            this.vector = vector;
            return this;
        }

        public Builder addScalarField(DocField field) {
            this.otherScalarFields.add(field);
            return this;
        }

        public Document build() {
            if (StringUtils.isEmpty(this.id)) {
                throw new ParamException("Document Create error: id is null");
            }
            if (this.vector == null || this.vector.isEmpty()) {
                throw new ParamException("Document Create error: vector is null");
            }
            return new Document(this);
        }
    }
}