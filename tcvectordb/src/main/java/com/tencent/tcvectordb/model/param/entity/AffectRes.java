package com.tencent.tcvectordb.model.param.entity;

public class AffectRes extends BaseRes {

    protected long affectedCount;

    private EmbeddingExtraInfo embeddingExtraInfo;

    public AffectRes() {
        super();
    }

    public long getAffectedCount() {
        return affectedCount;
    }

    public EmbeddingExtraInfo getEmbeddingExtraInfo() {
        return embeddingExtraInfo;
    }

    public void setEmbeddingExtraInfo(EmbeddingExtraInfo embeddingExtraInfo) {
        this.embeddingExtraInfo = embeddingExtraInfo;
    }

    @Override
    public String toString() {
        return "AffectRes{" +
                "affectedCount=" + affectedCount +
                ", embeddingExtraInfo=" + embeddingExtraInfo +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public AffectRes(int code, String msg, String warning, long affectedCount) {
        super(code, msg, warning);
        this.affectedCount = affectedCount;
    }
}
