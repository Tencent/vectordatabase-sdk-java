package com.tencent.tcvectordb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DocumentPreprocessParams implements ParamsSerializer{
    @JsonProperty(value = "appendTitleToChunk")
    private AppendTitleToChunkEnum appendTitleToChunkEnum;

    @JsonProperty(value = "appendKeywordsToChunk")
    private AppendKeywordsToChunkEnum appendKeywordsToChunkEnum;

    public DocumentPreprocessParams() {
        this.appendTitleToChunkEnum = AppendTitleToChunkEnum.DEFAULT;
        this.appendKeywordsToChunkEnum = AppendKeywordsToChunkEnum.DEFAULT;
    }

    public AppendTitleToChunkEnum getAppendTitleToChunkEnum() {
        return appendTitleToChunkEnum;
    }

    public void setAppendTitleToChunkEnum(AppendTitleToChunkEnum appendTitleToChunkEnum) {
        this.appendTitleToChunkEnum = appendTitleToChunkEnum;
    }

    public AppendKeywordsToChunkEnum getAppendKeywordsToChunkEnum() {
        return appendKeywordsToChunkEnum;
    }

    public void setAppendKeywordsToChunkEnum(AppendKeywordsToChunkEnum appendKeywordsToChunkEnum) {
        this.appendKeywordsToChunkEnum = appendKeywordsToChunkEnum;
    }
}

enum AppendTitleToChunkEnum {
    /**
     * 不处理
     */
    DEFAULT(0),
    /**
     * 将段落title叠加在Chunk上Embedding'
     */
    AppendTitleToChunkEmbedding(1),
    /**
     * 叠加逐级title
     */
    AppendEveryLevelTitle(2);

    private final int value;

    private AppendTitleToChunkEnum(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}

enum AppendKeywordsToChunkEnum {
    /**
     * 不处理
     */
    DEFAULT(0),
    /**
     * 将段落title叠加在Chunk上Embedding'
     */
    AppendKeywordsToChunkEmbedding(1);


    private final int value;

    private AppendKeywordsToChunkEnum(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}


