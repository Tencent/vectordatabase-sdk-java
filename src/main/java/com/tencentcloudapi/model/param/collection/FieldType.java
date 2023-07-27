package com.tencentcloudapi.model.param.collection;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * FieldType
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

    @JsonValue
    public String getValue() {
        return value;
    }


}
