package com.tencent.tcvectordb.model.param.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AppendKeywordsToChunkEnum {

    /**
     * 不处理
     */
    DEFAULT("0"),
    /**
     * 将段落title叠加在Chunk上Embedding'
     */
    AppendKeywordsToChunkEmbedding("1");


    private final String value;

    private AppendKeywordsToChunkEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
