package com.tencent.tcvectordb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tencent.tcvectordb.model.param.enums.EmbeddingModelEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * init Embedding when create embedding collection
 * Param:
 *   vector_field (String): vector field name
 *   status (String): status of embedding, enable is available, disabled is unavailable, status is invalid when
 *                 create collection
 *   field (String): field name of embedding content
 *   model (EmbeddingModel): [Deprecated] embedding model enum, this is a deprecated parameter, it will be
 *                 instead of arg model_name, if model and model_name both are not None, model_name is used first.
 *   model_name (str): model_name specify embedding model wher upsert documents
 * Example:
 *  Embedding embedding = Embedding.newBuilder().withField("field")
 *                                              .withVectorField("vectorField")
 *                                              .withModel(EmbeddingModelEnum.BGE_BASE_ZH)
 *                                              .withStatus("enable").build();
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Embedding {

    public static final String FIELD_NAME = "field";
    public static final String VECTOR_FIELD_NAME = "vectorField";
    public static final String MODEL_NAME = "model";
    public static final String STATUS_NAME = "status";

    private String field;
    private String vectorField;
    private EmbeddingModelEnum model;
    private String status;

    private String modelName;

    public Embedding() {
    }

    private Embedding(Builder builder) {
        this.field = builder.field;
        this.vectorField = builder.vectorField;
        this.model = builder.model;
        this.status = builder.status;
        this.modelName = builder.modelName;
    }

    @JsonValue
    public ObjectNode jsonValue() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        if (StringUtils.isNotBlank(this.field)) {
            node.put(FIELD_NAME, this.field);
        }
        if (StringUtils.isNotBlank(this.vectorField)) {
            node.put(VECTOR_FIELD_NAME, this.vectorField);
        }
        if (StringUtils.isNotBlank(this.modelName)) {
            node.put(MODEL_NAME, this.modelName);
        } else if (this.model != null) {
            node.put(MODEL_NAME, this.model.getModelName());
        }
        if (StringUtils.isNotBlank(this.status)) {
            node.put(STATUS_NAME, this.status);
        }
        return node;
    }

    public String getField() {
        return field;
    }

    public String getVectorField() {
        return vectorField;
    }

    public EmbeddingModelEnum getModel() {
        return model;
    }

    public String getStatus() {
        return status;
    }

    public String getModelName() {
        return modelName;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String field;
        private String vectorField;
        private EmbeddingModelEnum model;
        private String status;
        private String modelName;

        private Builder() {
        }

        public Builder withField(String textField) {
            this.field = textField;
            return this;
        }

        public Builder withVectorField(String vectorField) {
            this.vectorField = vectorField;
            return this;
        }

        public Builder withModel(EmbeddingModelEnum model) {
            this.model = model;
            return this;
        }

        public Builder withStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder withModelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Embedding build() {
            return new Embedding(this);
        }
    }
}
