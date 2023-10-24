package com.tencent.tcvectordb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadUrlRes extends BaseRes{
    @JsonProperty(value = "cos_endpoint")
    private String cosEndPoint;

    @JsonProperty(value = "upload_path")
    private String uploadPath;

    @JsonProperty(value = "file_id")
    private String fileId;

    @JsonProperty(value = "credentials")
    private Credential credential;

    @JsonProperty(value = "upload_condition")
    private UploadCondtion upCondition;

    public UploadCondtion getUpCondition() {
        return upCondition;
    }

    public void setUpCondition(UploadCondtion upCondition) {
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
        this.uploadPath = uploadPath;
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



