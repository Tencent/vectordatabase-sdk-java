package com.tencentcloudapi.model.param.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * HNSWParams
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class HNSWParams {

    @JsonProperty(value = "M")
    private int M;
    private int efConstruction;

    public HNSWParams(int m, int intefconstruction) {
        this.M = m;
        this.efConstruction = intefconstruction;
    }

    public HNSWParams() {
    }

    @JsonIgnore
    public int getM() {
        return M;
    }

    public int getEfConstruction() {
        return efConstruction;
    }
}
