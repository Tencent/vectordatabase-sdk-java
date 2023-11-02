package com.tencent.tcvectordb.model.param.dml;

import com.tencent.tcvectordb.exception.ParamException;

import java.util.List;

public class SearchContenOption {
    private String resultType;
    private List<Integer> chunkExpand;

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

    public SearchContenOption(Builder builder) {
        this.resultType = "chunks";
        this.chunkExpand = builder.chunkExpand;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String resultType;
        private List<Integer> chunkExpand;

        private Builder() {
        }

        public Builder withChunkExpand(List<Integer> chunkExpand) {
            this.chunkExpand = chunkExpand;
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


