package com.tencentcloudapi.model.param.collection.index;

/**
 * Description:
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

    public String getValue() {
        return value;
    }
}
