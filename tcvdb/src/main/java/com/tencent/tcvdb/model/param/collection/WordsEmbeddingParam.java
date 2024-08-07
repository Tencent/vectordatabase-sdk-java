package com.tencent.tcvdb.model.param.collection;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WordsEmbeddingParam {
    private Boolean allowEmpty;
    private String emptyInputRerank;
    private String fieldName;

    public WordsEmbeddingParam(Boolean allowEmpty, String emptyInputRerank, String fieldName) {

        this.allowEmpty = allowEmpty;
        this.emptyInputRerank = emptyInputRerank;
        this.fieldName = fieldName;
    }

    public Boolean getAllowEmpty() {
        return allowEmpty;
    }

    public void setAllowEmpty(Boolean allowEmpty) {
        this.allowEmpty = allowEmpty;
    }

    public String getEmptyInputRerank() {
        return emptyInputRerank;
    }

    public void setEmptyInputRerank(String emptyInputRerank) {
        this.emptyInputRerank = emptyInputRerank;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public static Builder newBuilder(){
        return new Builder();
    }
    // 建造者模式
    public static class Builder{
        private String emptyInputRerank;
        private Boolean enable;
        private String fieldName;
        public Builder withEnable(Boolean enable){
            this.enable = enable;
            return this;
        }
        public Builder withEmptyInputRerank(String emptyInputRerank){
            this.emptyInputRerank = emptyInputRerank;
            return this;
        }
        public Builder withFieldName(String fieldName){
            this.fieldName = fieldName;
            return this;
        }
        public WordsEmbeddingParam build(){
            return new WordsEmbeddingParam(enable, fieldName, emptyInputRerank);
        }
    }
}
