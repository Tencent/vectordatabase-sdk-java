package com.tencent.tcvectordb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tencent.tcvectordb.model.param.enums.AppendKeywordsToChunkEnum;
import com.tencent.tcvectordb.model.param.enums.AppendTitleToChunkEnum;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DocumentPreprocessParams implements ParamsSerializer{
    @JsonProperty(value = "append_title_to_chunk")
    private AppendTitleToChunkEnum appendTitleToChunkEnum;

    @JsonProperty(value = "append_keywords_to_chunk")
    private AppendKeywordsToChunkEnum appendKeywordsToChunkEnum;

    public DocumentPreprocessParams() {
        this.appendTitleToChunkEnum = AppendTitleToChunkEnum.DEFAULT;
        this.appendKeywordsToChunkEnum = AppendKeywordsToChunkEnum.DEFAULT;
    }

    public DocumentPreprocessParams(AppendTitleToChunkEnum appendTitleToChunkEnum, AppendKeywordsToChunkEnum appendKeywordsToChunkEnum) {
        this.appendTitleToChunkEnum = appendTitleToChunkEnum;
        this.appendKeywordsToChunkEnum = appendKeywordsToChunkEnum;
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


