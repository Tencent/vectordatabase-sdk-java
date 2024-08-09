package com.tencent.tcvdb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvdb.utils.JsonUtils;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataBaseInfoRes extends BaseRes {
    private List<String> databases;
    private List<DataBaseInfo> info;

    public DataBaseInfoRes() {
        super();
    }

    public DataBaseInfoRes(int code, String msg, String warning) {
        super(code, msg, warning);
    }

    public List<String> getDatabases() {
        return databases;
    }

    public void setDatabases(List<String> databases) {
        this.databases = databases;
    }

    public List<DataBaseInfo> getInfo() {
        return info;
    }

    public void setInfo(List<DataBaseInfo> info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}

