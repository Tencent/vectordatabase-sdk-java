package com.tencentcloudapi.model.param.collection;

/**
 * Description:
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class HNSWParams {
    private int M;
    private int efConstruction;

    public HNSWParams(int m, int intefconstruction) {
        this.M = m;
        this.efConstruction = intefconstruction;
    }
}
