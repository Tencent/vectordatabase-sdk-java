package com.tencent.tcvdb.model.param.dml;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WordsEmbeddingRerankOption extends RerankOption{
    private Boolean enable;
    private Integer expectRecallMultiples;

    public WordsEmbeddingRerankOption(Boolean enable, Integer expectRecallMultiples) {
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

    public Integer getExpectRecallMultiples() {
        return expectRecallMultiples;
    }

    public void setExpectRecallMultiples(Integer expectRecallMultiples) {
        this.expectRecallMultiples = expectRecallMultiples;
    }

    public static Builder newBuilder(){
        return new Builder();
    }
    // 建造者模式
    public static class Builder{
        private Boolean enable;
        private Integer expectRecallMultiples;
        public Builder withEnable(Boolean enable){
            this.enable = enable;
            return this;
        }
        public Builder withExpectRecallMultiples(Integer expectRecallMultiples){
            this.expectRecallMultiples = expectRecallMultiples;
            return this;
        }
        public WordsEmbeddingRerankOption build(){
            return new WordsEmbeddingRerankOption(enable, expectRecallMultiples);
        }
    }
}
