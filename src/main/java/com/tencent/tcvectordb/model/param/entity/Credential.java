package com.tencent.tcvectordb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Credential {
    @JsonProperty(value = "TmpSecretId")
    private String tmpSecretId;
    @JsonProperty(value = "TmpSecretKey")
    private String tmpSecretKey;
    @JsonProperty(value = "Token")
    private String token;

    public Credential() {
    }

    public String getTmpSecretId() {
        return tmpSecretId;
    }

    public void setTmpSecretId(String tmpSecretId) {
        this.tmpSecretId = tmpSecretId;
    }

    public String getTmpSecretKey() {
        return tmpSecretKey;
    }

    public void setTmpSecretKey(String tmpSecretKey) {
        this.tmpSecretKey = tmpSecretKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
