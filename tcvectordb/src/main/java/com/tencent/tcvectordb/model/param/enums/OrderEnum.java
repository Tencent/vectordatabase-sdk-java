package com.tencent.tcvectordb.model.param.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderEnum {
    ASC("asc"),
    DESC("desc");

    private final String order;

    OrderEnum(String order) {
        this.order = order;
    }

    @JsonValue
    public String getOrder() {
        return order;
    }
}






