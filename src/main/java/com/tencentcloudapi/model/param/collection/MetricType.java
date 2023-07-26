package com.tencentcloudapi.model.param.collection;

/**
 * MetricType
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public enum MetricType {
    /**
     *
     */
    L2("L2"),
    /**
     *
     */
    IP("IP"),
    /**
     *
     */
    COSINE("COSINE");

    private final String value;

    private MetricType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
