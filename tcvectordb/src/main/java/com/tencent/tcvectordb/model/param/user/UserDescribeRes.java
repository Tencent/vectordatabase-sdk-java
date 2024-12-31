package com.tencent.tcvectordb.model.param.user;

import com.tencent.tcvectordb.model.param.entity.BaseRes;
import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class UserDescribeRes extends BaseRes {
    private String user;
    private String createTime;
    private List<PrivilegeParam> privileges;

    public UserDescribeRes() {
    }

    public UserDescribeRes(int code, String msg, String warning) {
        super(code, msg, warning);
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

    @Override
    public String toString() {
        return "UserDescribeRes{" +
                "user='" + user + '\'' +
                ", createTime='" + createTime + '\'' +
                ", privileges=" + JsonUtils.toJsonString(privileges) +
                '}';
    }
}
