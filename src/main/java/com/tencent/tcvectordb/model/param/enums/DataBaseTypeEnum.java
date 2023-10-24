package com.tencent.tcvectordb.model.param.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DataBaseTypeEnum {
    BASE("BASE"),
    AI_DOC("AI_DOC");

    private final String dataBaseType;

    DataBaseTypeEnum(String dataBaseType) {
        this.dataBaseType = dataBaseType;
    }

    @JsonValue
    public String getDataBaseType() {
        return dataBaseType;
    }
}






