package com.tencent.tcvectordb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RebuildIndexParam {
    private boolean dropBeforeRebuild;
    private int throttle;


    public RebuildIndexParam() {
    }

    public RebuildIndexParam(Builder builder) {
        this.dropBeforeRebuild = builder.dropBeforeRebuild;
        this.throttle = builder.throttle;
    }

    public boolean dropBeforeRebuild() {
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
        private int throttle = 0;

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

        public RebuildIndexParam build() {
            return new RebuildIndexParam(this);
        }
    }
}
