package com.tencent.tcvectordb.model.param.entity;

public class EmbeddingExtraInfo {
    private Long tokenUsed;

    public EmbeddingExtraInfo(Long tokenUsed) {
        this.tokenUsed = tokenUsed;
    }

    public EmbeddingExtraInfo() {
    }

    public Long getTokenUsed() {
        return tokenUsed;
    }

    public void setTokenUsed(Long tokenUsed) {
        this.tokenUsed = tokenUsed;
    }

    @Override
    public String toString() {
        return "EmbeddingExtraInfo{" +
                "tokenUsed=" + tokenUsed +
                '}';
    }
}
