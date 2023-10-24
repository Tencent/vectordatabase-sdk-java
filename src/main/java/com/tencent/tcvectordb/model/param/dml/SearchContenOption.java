package com.tencent.tcvectordb.model.param.dml;

import com.tencent.tcvectordb.exception.ParamException;

import java.util.List;

public class SearchContenOption {
    private String resultType;
    private List<Integer> chunkExpand;
    private boolean mergeChunk;
    private Weights weights;

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public List<Integer> getChunkExpand() {
        return chunkExpand;
    }

    public void setChunkExpand(List<Integer> chunkExpand) {
        this.chunkExpand = chunkExpand;
    }

    public boolean isMergeChunk() {
        return mergeChunk;
    }

    public void setMergeChunk(boolean mergeChunk) {
        this.mergeChunk = mergeChunk;
    }

    public Weights getWeights() {
        return weights;
    }

    public void setWeights(Weights weights) {
        this.weights = weights;
    }

    public SearchContenOption(Builder builder) {
        this.resultType = "chunks";
        this.weights = builder.weights;
        this.chunkExpand = builder.chunkExpand;
        this.mergeChunk = builder.mergeChunk;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String resultType;
        private List<Integer> chunkExpand;
        private boolean mergeChunk;
        private Weights weights;

        private Builder() {
        }

        public Builder withChunkExpand(List<Integer> chunkExpand) {
            this.chunkExpand = chunkExpand;
            return this;
        }

        public Builder withMergeChunk(boolean mergeChunk) {
            this.mergeChunk = mergeChunk;
            return this;
        }

        public Builder withWeights(Weights weights) {
            this.weights = weights;
            return this;
        }

        public SearchContenOption build() {
            if (chunkExpand.size()!=2){
                throw new ParamException("SearchContenOption error: chunkExpand must have tow integers");
            }
            return new SearchContenOption(this);
        }
    }

}


