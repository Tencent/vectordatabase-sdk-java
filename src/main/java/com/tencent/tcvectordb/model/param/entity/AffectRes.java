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
}
