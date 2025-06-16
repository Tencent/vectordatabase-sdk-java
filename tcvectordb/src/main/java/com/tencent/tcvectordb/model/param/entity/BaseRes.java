package com.tencent.tcvectordb.model.param.entity;

public class BaseRes {

    protected int code;
    protected String msg;
    protected String warning;

    protected long count;

    protected String requestId;

    public BaseRes() {
    }

    public BaseRes(int code, String msg, String warning) {
        this.code = code;
        this.msg = msg;
        this.warning = warning;
    }

    public BaseRes(int code, String msg, String warning, long count) {
        this.code = code;
        this.msg = msg;
        this.warning = warning;
        this.count = count;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    public long getCount() {
        return count;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public void setCount(long count) {
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

