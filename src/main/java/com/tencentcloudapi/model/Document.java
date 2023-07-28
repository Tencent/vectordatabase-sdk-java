package com.tencentcloudapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tencentcloudapi.exception.ParamException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * VectorDB Document
 * User: wlleiiwang
 * Date: 2023/7/24
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Document {
    private final String id;
    private final List<Double> vector;
    private Double score;
    private String doc;
    private List<DocField> otherScalarFields;

    public String getId() {
        return id;
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
        if (otherScalarFields != null && !otherScalarFields.isEmpty()) {
            for (DocField field : otherScalarFields) {
                node.put(field.getName(), field.getValue().toString());
            }
        }
        return node.toString();
    }

    private Document(Builder builder) {
        this.id = builder.id;
        this.vector = builder.vector;
        this.doc = builder.doc;
        this.score = builder.score;
        this.otherScalarFields = builder.otherScalarFields;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private List<Double> vector;

        private Double score;
        private String doc;
        private List<DocField> otherScalarFields;

        public Builder() {
            this.otherScalarFields = new ArrayList<>();
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

        public Builder addScalarField(DocField field) {
            this.otherScalarFields.add(field);
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