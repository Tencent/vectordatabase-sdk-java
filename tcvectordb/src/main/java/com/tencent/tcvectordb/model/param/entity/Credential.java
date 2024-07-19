package com.tencent.tcvectordb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Credential {
    @JsonProperty(value = "TmpSecretId")
    private String TmpSecretId;
    @JsonProperty(value = "TmpSecretKey")
    private String TmpSecretKey;
    @JsonProperty(value = "Token")
    private String Token;

    public Credential() {
    }

    public String getTmpSecretId() {
        return TmpSecretId;
    }

    public void setTmpSecretId(String tmpSecretId) {
        this.TmpSecretId = tmpSecretId;
    }

    public String getTmpSecretKey() {
        return TmpSecretKey;
    }

    public void setTmpSecretKey(String tmpSecretKey) {
        this.TmpSecretKey = tmpSecretKey;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }
}
