package com.tencent.tcvectordb.model.param.user;

import java.util.ArrayList;
import java.util.List;

public class PrivilegeParam {
    private String resource;
    private List<String> actions;

    public PrivilegeParam() {
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public PrivilegeParam(Builder builder) {
        this.resource = builder.resource;
        this.actions = builder.actions;
    }

    public static Builder newBuilder() {
        return new Builder();
    }
    public String getResource() {
        return resource;
    }

    public List<String> getActions() {
        return actions;
    }

    //  建造者模式

    public static class Builder {
        private String resource;
        private List<String> actions;

        private Builder() {
            actions = new ArrayList<>();
        }

        public Builder withResource(String resource) {
            this.resource = resource;
            return this;
        }

        public Builder withActions(String actions) {
            this.actions.add(actions);
            return this;
        }

        public Builder withActions(List<String> actions) {
            this.actions.addAll(actions);
            return this;
        }
        public PrivilegeParam build() {
            return new PrivilegeParam(this);
        }
    }
}
