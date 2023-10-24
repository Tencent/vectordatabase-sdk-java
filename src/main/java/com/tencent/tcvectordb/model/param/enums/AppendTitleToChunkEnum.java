package com.tencent.tcvectordb.model.param.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AppendTitleToChunkEnum {
    /**
     * 不处理
     */
    DEFAULT("1"),
    /**
     * 将段落title叠加在Chunk上Embedding'
     */
    AppendTitleToChunkEmbedding("1"),
    /**
     * 叠加逐级title
     */
    AppendEveryLevelTitle("2");

    private final String value;

    private AppendTitleToChunkEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
