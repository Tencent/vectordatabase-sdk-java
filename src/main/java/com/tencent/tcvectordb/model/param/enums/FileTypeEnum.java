package com.tencent.tcvectordb.model.param.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FileTypeEnum {
    MARKDOWN("markdown"),
    UNSUPPORT("unsupport");

    private final String fileType;

    FileTypeEnum(String fileType) {
        this.fileType = fileType;
    }

    @JsonValue
    public String getDataFileType() {
        return fileType;
    }
}






