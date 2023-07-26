package com.tencentcloudapi.model;

/**
 * Doc Field
 * User: wlleiiwang
 * Date: 2023/7/26
 */
public class DocField {
    private final String name;
    private final Object value;

    public DocField(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
