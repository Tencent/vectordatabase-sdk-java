package com.tencent.tcvectordb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataBaseTypeRes extends BaseRes{
    @JsonProperty(value = "database")
    private DataBaseType databaseType;

    public DataBaseTypeRes(int code, String msg, String warning, DataBaseType databaseType) {
        super(code, msg, warning);
        this.databaseType = databaseType;
    }

    public DataBaseTypeRes() {
        super();
    }

    public DataBaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DataBaseType database) {
        this.databaseType = database;
    }

    @Override
    public String toString() {
        return "DataBaseTypeRes{" +
                "dbType=" + databaseType.getDbType() +
                "createTime=" + databaseType.getCreateTime() +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}

