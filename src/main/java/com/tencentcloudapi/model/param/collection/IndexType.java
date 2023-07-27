package com.tencentcloudapi.model.param.collection;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * IndexType
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public enum IndexType {
    /**
     *
     */
    FLAT("FLAT"),
    /**
     *
     */
    HNSW("HNSW"),
    /**
     *
     */
    PRIMARY_KEY("primaryKey"),
    /**
     *
     */
    FILTER("filter");

    private final String value;

    private IndexType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
