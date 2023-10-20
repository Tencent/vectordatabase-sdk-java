package com.tencent.tcvectordb.model.param.entity;

public class DataBaseTypeRes extends BaseRes{
    private DataBaseType database;

    public DataBaseTypeRes() {
        super();
    }

    public DataBaseType getDatabase() {
        return database;
    }

    public void setDatabase(DataBaseType database) {
        this.database = database;
    }

    @Override
    public String toString() {
        return "DataBaseTypeRes{" +
                "dbType=" + database.getDbType() +
                "createTime=" + database.getCreateTime() +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
class DataBaseType{
    private String dbType;
    private int createTime;

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }
}
