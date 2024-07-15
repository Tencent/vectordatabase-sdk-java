package com.tencent.tcvectordb.model.param.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DataBaseTypeEnum {
    BASE("BASE"),
    AI_DOC("AI_DOC"),
    BASE_DB("BASE_DB"),
    AI_DB("AI_DB");

    private final String dataBaseType;

    DataBaseTypeEnum(String dataBaseType) {
        this.dataBaseType = dataBaseType;
    }

    public static boolean isAIDataBase(DataBaseTypeEnum dbType){
        return dbType.equals(DataBaseTypeEnum.AI_DOC) || dbType.equals(DataBaseTypeEnum.AI_DB);
    }

    @JsonValue
    public String getDataBaseType() {
        return dataBaseType;
    }
}






