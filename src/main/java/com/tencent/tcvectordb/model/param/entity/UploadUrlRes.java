package com.tencent.tcvectordb.model.param.entity;

public class UploadUrlRes extends BaseRes{
    private String cosEndPoint;
    private String uploadPath;

    private String fileId;

    private Credential credential;

    private UpCondition upCondition;

    public UpCondition getUpCondition() {
        return upCondition;
    }

    public void setUpCondition(UpCondition upCondition) {
        this.upCondition = upCondition;
    }

    public String getCosEndPoint() {
        return cosEndPoint;
    }

    public void setCosEndPoint(String cosEndPoint) {
        this.cosEndPoint = cosEndPoint;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        uploadPath = uploadPath;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredentials(Credential credential) {
        this.credential = credential;
    }

    @Override
    public String toString() {
        return "UploadUrlRes{" +
                "cos_endpoint=" + cosEndPoint +
                "upload_path=" + uploadPath +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}

class UpCondition{
    private int maxSupportContentLength;

    public int getMaxSupportContentLength() {
        return maxSupportContentLength;
    }

    public void setMaxSupportContentLength(int maxSupportContentLength) {
        this.maxSupportContentLength = maxSupportContentLength;
    }
}

