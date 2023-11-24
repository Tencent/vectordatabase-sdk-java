package com.tencent.tcvectordb.model.param.collectionView;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.model.DocumentSet;
import com.tencent.tcvectordb.model.param.collection.ParamsSerializer;
import com.tencent.tcvectordb.model.param.enums.AppendKeywordsToChunkEnum;
import com.tencent.tcvectordb.model.param.enums.AppendTitleToChunkEnum;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SplitterPreprocessParams implements ParamsSerializer {
    private AppendTitleToChunkEnum appendTitleToChunk;
    private AppendKeywordsToChunkEnum appendKeywordsToChunk;

    public SplitterPreprocessParams() {
        this.appendTitleToChunk = AppendTitleToChunkEnum.DEFAULT;
        this.appendKeywordsToChunk = AppendKeywordsToChunkEnum.DEFAULT;
    }

    public SplitterPreprocessParams(Builder builder) {
        this.appendTitleToChunk = builder.appendTitleToChunk;
        this.appendKeywordsToChunk = builder.appendKeywordsToChunk;
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

    public static DocumentSet.Builder newBuilder() {
        return new DocumentSet.Builder();
    }

    public static final class Builder {
        private AppendTitleToChunkEnum appendTitleToChunk;
        private AppendKeywordsToChunkEnum appendKeywordsToChunk;

        public Builder withAppendTitleToChunkEnum(AppendTitleToChunkEnum appendTitleToChunk) {
            this.appendTitleToChunk = appendTitleToChunk;
            return this;
        }

        public Builder withAppendKeywordsToChunkEnum(AppendKeywordsToChunkEnum appendKeywordsToChunk) {
            this.appendKeywordsToChunk = appendKeywordsToChunk;
            return this;
        }

        public SplitterPreprocessParams Build(){
            return new SplitterPreprocessParams(this);
        }

    }
}


