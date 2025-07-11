package com.tencent.tcvectordb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModelParam {
    private Boolean retrieveDenseVector;
    private Boolean retrieveSparseVector;

    public ModelParam(Builder builder) {
        this.retrieveDenseVector = builder.retrieveDenseVector;
        this.retrieveSparseVector = builder.retrieveSparseVector;
    }

    public Boolean getRetrieveDenseVector() {
        return retrieveDenseVector;
    }

    public void setRetrieveDenseVector(Boolean retrieveDenseVector) {
        this.retrieveDenseVector = retrieveDenseVector;
    }

    public Boolean getRetrieveSparseVector() {
        return retrieveSparseVector;
    }

    public void setRetrieveSparseVector(Boolean retrieveSparseVector) {
        this.retrieveSparseVector = retrieveSparseVector;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    // Builder pattern

    public static class Builder {
        private Boolean retrieveDenseVector;
        private Boolean retrieveSparseVector;

        public Builder() {
        }

        public Builder withRetrieveDenseVector(Boolean retrieveDenseVector) {
            this.retrieveDenseVector = retrieveDenseVector;
            return this;
        }

        public Builder withRetrieveSparseVector(Boolean retrieveSparseVector) {
            this.retrieveSparseVector = retrieveSparseVector;
            return this;
        }

        public ModelParam build() {
            return new ModelParam(this);
        }
    }
}
