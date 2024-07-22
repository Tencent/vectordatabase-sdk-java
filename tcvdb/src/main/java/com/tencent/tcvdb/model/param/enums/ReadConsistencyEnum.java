package com.tencent.tcvdb.model.param.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReadConsistencyEnum {
    EVENTUAL_CONSISTENCY("eventualConsistency"),
    STRONG_CONSISTENCY("strongConsistency");

    ReadConsistencyEnum(String readConsistency) {
        this.readConsistency = readConsistency;
    }

    private final String readConsistency;

    @JsonValue
    public String getReadConsistency() {
        return readConsistency;
    }
}
