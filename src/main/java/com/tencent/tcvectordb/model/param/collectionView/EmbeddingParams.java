package com.tencent.tcvectordb.model.param.collectionView;

import com.tencent.tcvectordb.model.DocumentSet;

public class EmbeddingParams {
    private String language;
    private boolean enableWordEmbedding;

    public EmbeddingParams(Builder builder) {
        this.language = builder.language.getValue();
        this.enableWordEmbedding = builder.enableWordEmbedding;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(LanuageType language) {
        this.language = language.getValue();
    }

    public boolean isEnableWordEmbedding() {
        return enableWordEmbedding;
    }

    public void setEnableWordEmbedding(boolean enableWordEmbedding) {
        this.enableWordEmbedding = enableWordEmbedding;
    }

    public static DocumentSet.Builder newBuilder() {
        return new DocumentSet.Builder();
    }
    public static class Builder{
        private LanuageType language;
        private boolean enableWordEmbedding;

        public Builder withLanguage(LanuageType language){
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
