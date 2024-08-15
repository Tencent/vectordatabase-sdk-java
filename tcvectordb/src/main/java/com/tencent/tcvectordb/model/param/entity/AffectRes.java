package com.tencent.tcvectordb.model.param.entity;

public class AffectRes extends BaseRes {

    protected long affectedCount;

    public AffectRes() {
        super();
    }

    public long getAffectedCount() {
        return affectedCount;
    }


    @Override
    public String toString() {
        return "AffectRes{" +
                "affectedCount=" + affectedCount +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public AffectRes(int code, String msg, String warning, long affectedCount) {
        super(code, msg, warning);
        this.affectedCount = affectedCount;
    }
}
