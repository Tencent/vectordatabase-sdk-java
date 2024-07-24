package com.tencent.tcvdb.model.param.dml;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WordsEmbeddingRerankOption extends RerankOption{
    private Boolean enable;
    private Double expectRecallMultiples;

    public WordsEmbeddingRerankOption(Boolean enable, Double expectRecallMultiples) {
        super("wordsEmbedding");
        this.enable = enable;
        this.expectRecallMultiples = expectRecallMultiples;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Double getExpectRecallMultiples() {
        return expectRecallMultiples;
    }

    public void setExpectRecallMultiples(Double expectRecallMultiples) {
        this.expectRecallMultiples = expectRecallMultiples;
    }

    public static Builder newBuilder(){
        return new Builder();
    }
    // 建造者模式
    public static class Builder{
        private Boolean enable;
        private Double expectRecallMultiples;
        public Builder withEnable(Boolean enable){
            this.enable = enable;
            return this;
        }
        public Builder withExpectRecallMultiples(Double expectRecallMultiples){
            this.expectRecallMultiples = expectRecallMultiples;
            return this;
        }
        public WordsEmbeddingRerankOption build(){
            return new WordsEmbeddingRerankOption(enable, expectRecallMultiples);
        }
    }
}
