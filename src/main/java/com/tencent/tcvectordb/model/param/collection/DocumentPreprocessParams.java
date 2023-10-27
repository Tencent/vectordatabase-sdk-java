package com.tencent.tcvectordb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.model.param.enums.AppendKeywordsToChunkEnum;
import com.tencent.tcvectordb.model.param.enums.AppendTitleToChunkEnum;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DocumentPreprocessParams implements ParamsSerializer{
    private AppendTitleToChunkEnum appendTitleToChunk;
    private AppendKeywordsToChunkEnum appendKeywordsToChunk;

    public DocumentPreprocessParams() {
        this.appendTitleToChunk = AppendTitleToChunkEnum.DEFAULT;
        this.appendKeywordsToChunk = AppendKeywordsToChunkEnum.DEFAULT;
    }

    public DocumentPreprocessParams(AppendTitleToChunkEnum appendTitleToChunkEnum, AppendKeywordsToChunkEnum appendKeywordsToChunkEnum) {
        this.appendTitleToChunk = appendTitleToChunkEnum;
        this.appendKeywordsToChunk = appendKeywordsToChunkEnum;
    }

    public AppendTitleToChunkEnum getAppendTitleToChunk() {
        return appendTitleToChunk;
    }

    public void setAppendTitleToChunk(AppendTitleToChunkEnum appendTitleToChunk) {
        this.appendTitleToChunk = appendTitleToChunk;
    }

    public AppendKeywordsToChunkEnum getAppendKeywordsToChunk() {
        return appendKeywordsToChunk;
    }

    public void setAppendKeywordsToChunk(AppendKeywordsToChunkEnum appendKeywordsToChunk) {
        this.appendKeywordsToChunk = appendKeywordsToChunk;
    }
}


