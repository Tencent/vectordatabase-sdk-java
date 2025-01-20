package com.tencent.tcvectordb.model.param.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.utils.JsonUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserChangePasswordParam {
    private String user;
    private String password;

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    private UserChangePasswordParam(Builder builder) {
        this.user = builder.user;
        this.password = builder.password;
    }

    public static Builder newBuilder() {
        return new Builder();
    }
    //  建造者模式
    public static class Builder {
        private String user;
        private String password;

        public Builder withUser(String user) {
            this.user = user;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserChangePasswordParam build() {
            return new UserChangePasswordParam(this);
        }
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
