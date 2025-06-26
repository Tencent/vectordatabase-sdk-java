package com.tencent.tcvectordb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RebuildIndexParam {
    private Boolean dropBeforeRebuild;
    private Integer throttle;

    private String fieldName;


    public RebuildIndexParam() {
    }

    public RebuildIndexParam(Builder builder) {
        this.dropBeforeRebuild = builder.dropBeforeRebuild;
        this.throttle = builder.throttle;
        this.fieldName = builder.fieldName;
    }


    public String getFieldName() {
        return fieldName;
    }

    public Boolean getDropBeforeRebuild() {
        return dropBeforeRebuild;
    }

    public int getThrottle() {
        return throttle;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private boolean dropBeforeRebuild = false;
        private Integer throttle = 1;

        private String fieldName;

        private Builder() {
        }

        public Builder withDropBeforeRebuild(boolean dropBeforeRebuild) {
            this.dropBeforeRebuild = dropBeforeRebuild;
            return this;
        }

        public Builder withThrottle(int throttle) {
            this.throttle = throttle;
            return this;
        }

        public Builder withFieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }
        public RebuildIndexParam build() {
            return new RebuildIndexParam(this);
        }
    }
}
