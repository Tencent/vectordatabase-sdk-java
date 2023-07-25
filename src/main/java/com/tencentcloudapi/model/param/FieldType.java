package com.tencentcloudapi.model.param;

/**
 * Description:
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public enum FieldType {
    /**
     *
     */
    Uint64("uint64"),
    /**
     *
     */
    String("string"),
    /**
     *
     */
    Vector("vector");

    private final String value;

    private FieldType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
