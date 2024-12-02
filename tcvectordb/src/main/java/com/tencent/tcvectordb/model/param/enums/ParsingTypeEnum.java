package com.tencent.tcvectordb.model.param.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ParsingTypeEnum {
    AlgorithmParsing("AlgorithmParsing"),
    VisionModel("VisionModelParsing");
    private final String value;
    ParsingTypeEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
