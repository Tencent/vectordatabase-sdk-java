package com.tencent.tcvectordb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.model.param.enums.EmbeddingModelEnum;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Embedding {
    private String textField;
    private String vectorField;
    private EmbeddingModelEnum model;
    private String status;

    public Embedding() {
    }

    private Embedding(Builder builder) {
        this.textField = builder.textField;
        this.vectorField = builder.vectorField;
        this.model = builder.model;
        this.status = builder.status;
    }

    public String getTextField() {
        return textField;
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


    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String textField;
        private String vectorField;
        private EmbeddingModelEnum model;
        private String status;

        private Builder() {
        }

        public Builder withTextField(String textField) {
            this.textField = textField;
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

        public Embedding build() {
            return new Embedding(this);
        }
    }
}
