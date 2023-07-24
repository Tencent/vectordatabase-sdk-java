package com.tencentcloudapi.model.param;

/**
 * VectorDB HNSWSearchParams
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class HNSWSearchParams {
    private int ef;

    public HNSWSearchParams(int ef) {
        this.ef = ef;
    }

    public int getEf() {
        return ef;
    }
}
