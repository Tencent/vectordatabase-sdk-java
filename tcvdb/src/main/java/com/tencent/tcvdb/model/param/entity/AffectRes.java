package com.tencent.tcvdb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AffectRes extends BaseRes {

    protected long affectedCount;

    public AffectRes() {
        super();
    }

    public long getAffectedCount() {
        return affectedCount;
    }

    public AffectRes(int code, String msg, String warning, long affectedCount) {
        super(code, msg, warning);
        this.affectedCount = affectedCount;
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
