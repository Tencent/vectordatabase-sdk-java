package com.tencent.tcvectordb.model.param.entity;

public class BaseRes {

    protected int code;
    protected String msg;
    protected String warning;

    protected int count;

    public BaseRes() {
    }

    public BaseRes(int code, String msg, String warning) {
        this.code = code;
        this.msg = msg;
        this.warning = warning;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getWarning() {
        return warning;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "BaseRes{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", warning='" + warning + '\'' +
                '}';
    }
}

