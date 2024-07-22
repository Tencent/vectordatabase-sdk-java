package com.tencent.tcvdb.model.param.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PartitionTypeEnum {
    HASH("hash");


    private String value;

    PartitionTypeEnum(String value) {
        this.value = value;
    }



    @JsonValue
    public String getValue() {
        return value;
    }
}
