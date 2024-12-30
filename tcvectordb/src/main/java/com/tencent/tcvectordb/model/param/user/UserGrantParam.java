package com.tencent.tcvectordb.model.param.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserGrantParam {
    private String user;
    private List<PrivilegeParam> privileges;
    private UserGrantParam(Builder builder) {
        this.user = builder.user;
        this.privileges = builder.privileges;
    }

    public String getUser() {
        return user;
    }

    public List<PrivilegeParam> getPrivileges() {
        return privileges;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    // 建造者模式
    public static class Builder {
        private String user;
        private List<PrivilegeParam> privileges;

        public Builder() {
            privileges = new ArrayList<>();
        }
        public Builder withUser(String user) {
            this.user = user;
            return this;
        }

        public Builder withPrivileges(List<PrivilegeParam> privileges) {
            this.privileges.addAll(privileges);
            return this;
        }

        public Builder withPrivilege(PrivilegeParam privilege) {
            this.privileges.add(privilege);
            return this;
        }

        public UserGrantParam build() {
            return new UserGrantParam(this);
        }
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }

}
