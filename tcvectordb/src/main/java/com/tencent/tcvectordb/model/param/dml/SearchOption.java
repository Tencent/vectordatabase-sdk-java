package com.tencent.tcvectordb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.exception.ParamException;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchOption {
    private String resultType;
    private List<Integer> chunkExpand;
    private RerankOption rerank;

    public RerankOption getRerank() {
        return rerank;
    }

    public void setRerank(RerankOption rerank) {
        this.rerank = rerank;
    }

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

    public SearchOption(Builder builder) {
        this.resultType = "chunks";
        this.chunkExpand = builder.chunkExpand;
        this.rerank = builder.rerank;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String resultType;
        private List<Integer> chunkExpand;
        private RerankOption rerank;

        private Builder() {
        }

        public Builder withChunkExpand(List<Integer> chunkExpand) {
            this.chunkExpand = chunkExpand;
            return this;
        }

        public Builder withRerank(RerankOption rerank) {
            this.rerank = rerank;
            return this;
        }

        public SearchOption build() {
            if (chunkExpand != null && chunkExpand.size() != 2) {
                throw new ParamException("SearchContenOption error: chunkExpand must have tow integers");
            }
            return new SearchOption(this);
        }
    }

}
