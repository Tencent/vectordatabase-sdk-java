package com.tencent.tcvectordb.model.param.entity;

public class FileContent {
    private String fileName;
    private String createTime;
    private String fileText;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFileText() {
        return fileText;
    }

    public void setFileText(String fileText) {
        this.fileText = fileText;
    }

    @Override
    public String toString() {
        return "FileContent{" +
                "fileName='" + fileName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", fileText='" + fileText + '\'' +
                '}';
    }
}
