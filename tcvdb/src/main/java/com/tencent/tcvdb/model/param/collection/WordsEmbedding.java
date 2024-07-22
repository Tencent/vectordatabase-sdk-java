package com.tencent.tcvdb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Embedding is used to create embedding collection, and specify embedding model with set model or modelName, and if
 * modelName is not null, it will be used first
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class WordsEmbedding {

    private String fieldName;
    private String emptyInputRerank;
    private boolean allowEmpty;

    public WordsEmbedding() {
    }

    private WordsEmbedding(Builder builder) {
        this.fieldName = builder.fieldName;
        this.emptyInputRerank = builder.emptyInputRerank;
        this.allowEmpty = builder.allowEmpty;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String fieldName;
        private String emptyInputRerank;
        private boolean allowEmpty;

        private Builder() {
        }

        public Builder withFieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public Builder withEmptyInputRerank(String emptyInputRerank) {
            this.emptyInputRerank = emptyInputRerank;
            return this;
        }

        public Builder withAllowEmpty(boolean allowEmpty) {
            this.allowEmpty = allowEmpty;
            return this;
        }

        public WordsEmbedding build() {
            return new WordsEmbedding(this);
        }
    }
}
