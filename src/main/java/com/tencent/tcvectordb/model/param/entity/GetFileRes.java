package com.tencent.tcvectordb.model.param.entity;

public class GetFileRes extends BaseRes{
    private FileContent fileContent;

    public FileContent getFileContent() {
        return fileContent;
    }

    public void setFileContent(FileContent fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public String toString() {
        return "GetFileRes{" +
                "fileContent=" + fileContent +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", warning='" + warning + '\'' +
                '}';
    }
}



