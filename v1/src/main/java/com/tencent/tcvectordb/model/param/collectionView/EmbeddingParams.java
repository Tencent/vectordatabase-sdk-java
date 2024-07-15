package com.tencent.tcvectordb.model.param.collectionView;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.model.DocumentSet;


@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class EmbeddingParams {
    private String language;
    private Boolean enableWordsEmbedding;

    public EmbeddingParams() {
    }

    public EmbeddingParams(Builder builder) {
        if(builder.language != null) {
            this.language = builder.language.getValue();
        }
        this.enableWordsEmbedding = builder.enableWordEmbedding;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(LanguageType language) {
        this.language = language.getValue();
    }

    public Boolean isEnableWordsEmbedding() {
        return enableWordsEmbedding;
    }

    public void setEnableWordsEmbedding(boolean enableWordsEmbedding) {
        this.enableWordsEmbedding = enableWordsEmbedding;
    }

    public static Builder newBuilder() {
        return new Builder();
    }
    public static class Builder{
        private LanguageType language;
        private Boolean enableWordEmbedding;

        public Builder withLanguage(LanguageType language){
            this.language = language;
            return this;
        }

        public Builder withEnableWordEmbedding(boolean enableWordEmbedding){
            this.enableWordEmbedding = enableWordEmbedding;
            return this;
        }

        public EmbeddingParams Build(){
            return new EmbeddingParams(this);
        }
    }

}
