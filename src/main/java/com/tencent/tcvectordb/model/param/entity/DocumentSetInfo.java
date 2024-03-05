package com.tencent.tcvectordb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentSetInfo {
    private int textLength;
    private int byteLength;
    private int indexedProgress;
    private String indexedStatus;
    private String createTime;
    private String lastUpdateTime;
    private String indexedErrorMsg;
    private String keywords;

    public int getTextLength() {
        return textLength;
    }

    public void setTextLength(int textLength) {
        this.textLength = textLength;
    }

    public int getByteLength() {
        return byteLength;
    }

    public void setByteLength(int byteLength) {
        this.byteLength = byteLength;
    }

    public int getIndexedProgress() {
        return indexedProgress;
    }

    public void setIndexedProgress(int indexedProgress) {
        this.indexedProgress = indexedProgress;
    }

    public String getIndexedStatus() {
        return indexedStatus;
    }

    public void setIndexedStatus(String indexedStatus) {
        this.indexedStatus = indexedStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getIndexedErrorMsg() {
        return indexedErrorMsg;
    }

    public void setIndexedErrorMsg(String indexedErrorMsg) {
        this.indexedErrorMsg = indexedErrorMsg;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }


    @Override
    public String toString() {
        return "DocumentSetInfo{" +
                "textLength=" + textLength +
                ", byteLength=" + byteLength +
                ", indexedProgress=" + indexedProgress +
                ", indexedStatus='" + indexedStatus + '\'' +
                ", createTime='" + createTime + '\'' +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                ", indexedErrorMsg='" + indexedErrorMsg + '\'' +
                ", keywords='" + keywords + '\'' +
                '}';
    }
}
