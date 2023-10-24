package com.tencent.tcvectordb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResDocumentInfo {
    @JsonProperty(value = "createTime")
    private String createTime;
    @JsonProperty(value = "fileName")
    private String fileName;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "SearchResDocumentInfo{" +
                "createTime='" + createTime + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
