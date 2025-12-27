package com.tencent.tcvectordb.model.param.collectionView;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SplitterPreprocessParams{
    private boolean appendTitleToChunk;
    private boolean appendKeywordsToChunk;
    private String chunkSplitter;

    public SplitterPreprocessParams() {
        this.appendTitleToChunk = false;
        this.appendKeywordsToChunk = false;
    }

    public SplitterPreprocessParams(Builder builder) {
        this.appendTitleToChunk = builder.appendTitleToChunk;
        this.appendKeywordsToChunk = builder.appendKeywordsToChunk;
        this.chunkSplitter = builder.chunkSplitter;
    }

    public boolean isAppendTitleToChunk() {
        return appendTitleToChunk;
    }

    public String getChunkSplitter() {
        return chunkSplitter;
    }

    public void setAppendTitleToChunk(boolean appendTitleToChunk) {
        this.appendTitleToChunk = appendTitleToChunk;
    }

    public boolean isAppendKeywordsToChunk() {
        return appendKeywordsToChunk;
    }


    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private boolean appendTitleToChunk;
        private boolean appendKeywordsToChunk;
        private String chunkSplitter;

        public Builder withAppendTitleToChunkEnum( boolean appendTitleToChunk) {
            this.appendTitleToChunk = appendTitleToChunk;
            return this;
        }

        public Builder withAppendKeywordsToChunkEnum(boolean appendKeywordsToChunk) {
            this.appendKeywordsToChunk = appendKeywordsToChunk;
            return this;
        }

        public Builder withChunkSplitter(String chunkSplitter) {
            this.chunkSplitter = chunkSplitter;
            return this;
        }

        public SplitterPreprocessParams Build(){
            return new SplitterPreprocessParams(this);
        }

    }
}


