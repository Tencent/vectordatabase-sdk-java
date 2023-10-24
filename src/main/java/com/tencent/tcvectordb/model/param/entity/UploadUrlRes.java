package com.tencent.tcvectordb.model.param.entity;

public class UploadUrlRes extends BaseRes{
    private String cosEndpoint;
    private String uploadPath;
    private String fileId;
    private Credential credentials;
    private UploadCondtion uploadCondition;

    public UploadCondtion getUploadCondition() {
        return uploadCondition;
    }

    public void setUploadCondition(UploadCondtion uploadCondition) {
        this.uploadCondition = uploadCondition;
    }

    public String getCosEndpoint() {
        return cosEndpoint;
    }

    public void setCosEndpoint(String cosEndpoint) {
        this.cosEndpoint = cosEndpoint;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Credential getCredentials() {
        return credentials;
    }

    public void setCredentials(Credential credential) {
        this.credentials = credential;
    }

    @Override
    public String toString() {
        return "UploadUrlRes{" +
                "cos_endpoint=" + cosEndpoint +
                "upload_path=" + uploadPath +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}



