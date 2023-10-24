package com.tencent.tcvectordb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadCondtion {
    @JsonProperty(value = "max_support_content_length")
    private int maxSupportContentLength;

    public int getMaxSupportContentLength() {
        return maxSupportContentLength;
    }

    public void setMaxSupportContentLength(int maxSupportContentLength) {
        this.maxSupportContentLength = maxSupportContentLength;
    }
}
