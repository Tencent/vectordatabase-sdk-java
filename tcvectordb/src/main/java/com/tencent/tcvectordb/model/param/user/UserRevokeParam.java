package com.tencent.tcvectordb.model.param.user;

import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class UserRevokeParam {
    private String user;
    private List<PrivilegeParam> privileges;
    private UserRevokeParam(Builder builder) {
        this.user = builder.user;
        this.privileges = builder.privileges;
    }

    public static Builder newBuilder() {
        return new Builder();
    }
    public String getUser() {
        return user;
    }

    public List<PrivilegeParam> getPrivileges() {
        return privileges;
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

        public UserRevokeParam build() {
            return new UserRevokeParam(this);
        }
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }

}
