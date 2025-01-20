package com.tencent.tcvectordb.model.param.user;

import com.tencent.tcvectordb.model.param.entity.BaseRes;
import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.List;

public class UserListRes extends BaseRes {
    private List<UserInfo> users;

    public UserListRes(){}

    public UserListRes(int code, String message,String warning) {
        super(code, message, warning);
    }
    public List<UserInfo> getUsers() {
        return users;
    }
    public void setUsers(List<UserInfo> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(users);
    }
}
