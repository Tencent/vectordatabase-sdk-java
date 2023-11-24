package com.tencent.tcvectordb.model.param.collectionView;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.model.DocumentSet;
import com.tencent.tcvectordb.model.param.collection.ParamsSerializer;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SplitterPreprocessParams implements ParamsSerializer {
    private boolean appendTitleToChunk;
    private boolean appendKeywordsToChunk;

    public SplitterPreprocessParams() {
        this.appendTitleToChunk = false;
        this.appendKeywordsToChunk = false;
    }

    public SplitterPreprocessParams(Builder builder) {
        this.appendTitleToChunk = builder.appendTitleToChunk;
        this.appendKeywordsToChunk = builder.appendKeywordsToChunk;
    }

    public boolean isAppendTitleToChunk() {
        return appendTitleToChunk;
    }

    public void setAppendTitleToChunk(boolean appendTitleToChunk) {
        this.appendTitleToChunk = appendTitleToChunk;
    }

    public boolean isAppendKeywordsToChunk() {
        return appendKeywordsToChunk;
    }

    public void setAppendKeywordsToChunk(boolean appendKeywordsToChunk) {
        this.appendKeywordsToChunk = appendKeywordsToChunk;
    }

    public static DocumentSet.Builder newBuilder() {
        return new DocumentSet.Builder();
    }

    public static final class Builder {
        private boolean appendTitleToChunk;
        private boolean appendKeywordsToChunk;

        public Builder withAppendTitleToChunkEnum( boolean appendTitleToChunk) {
            this.appendTitleToChunk = appendTitleToChunk;
            return this;
        }

        public Builder withAppendKeywordsToChunkEnum(boolean appendKeywordsToChunk) {
            this.appendKeywordsToChunk = appendKeywordsToChunk;
            return this;
        }

        public SplitterPreprocessParams Build(){
            return new SplitterPreprocessParams(this);
        }

    }
}


