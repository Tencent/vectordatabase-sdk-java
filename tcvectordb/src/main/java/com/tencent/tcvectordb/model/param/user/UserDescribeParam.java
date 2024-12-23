package com.tencent.tcvectordb.model.param.user;

import com.tencent.tcvectordb.utils.JsonUtils;

public class UserDescribeParam {
    private String user;

    public String getUser() {
        return user;
    }

    public UserDescribeParam(String user) {
        this.user = user;
    }
    private UserDescribeParam(Builder builder) {
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

        public UserDescribeParam build() {
            return new UserDescribeParam(this);
        }
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
