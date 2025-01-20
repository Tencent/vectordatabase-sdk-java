package com.tencent.tcvectordb.model.param.user;

import com.tencent.tcvectordb.model.param.entity.BaseRes;

import java.util.List;

public class UserInfo{
    private String user;
    private String createTime;
    private List<PrivilegeParam> privileges;
    public UserInfo() {
    }
    public void setUser(String user) {
        this.user = user;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setPrivileges(List<PrivilegeParam> privileges) {
        this.privileges = privileges;
    }

    public String getUser() {
        return user;
    }

    public List<PrivilegeParam> getPrivileges() {
        return privileges;
    }


}
