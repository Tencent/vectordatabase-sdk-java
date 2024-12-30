package com.tencent.tcvectordb.model.param.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.utils.JsonUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDropParam {
    private String user;

    public UserDropParam(String user) {
        this.user = user;
    }
    public String getUser() {
        return user;
    }

    private UserDropParam(Builder builder) {
        this.user = builder.user;
    }

    public static Builder newBuilder() {
        return new Builder();
    }
    //  建造者模式
    public static class Builder {
        private String user;

        public Builder withUser(String user) {
            this.user = user;
            return this;
        }

        public UserDropParam build() {
            return new UserDropParam(this);
        }
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
