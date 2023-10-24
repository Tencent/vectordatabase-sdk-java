package com.tencent.tcvectordb.model.param.dml;

public class Weights{
    private double chunkSimilarity;
    private double wordSimilarity;
    private double wordBm25;

    public Weights(double chunkSimilarity, double wordSimilarity, double wordBm25) {
        this.chunkSimilarity = chunkSimilarity;
        this.wordSimilarity = wordSimilarity;
        this.wordBm25 = wordBm25;
    }

    public double getChunkSimilarity() {
        return chunkSimilarity;
    }

    public void setChunkSimilarity(double chunkSimilarity) {
        this.chunkSimilarity = chunkSimilarity;
    }

    public double getWordSimilarity() {
        return wordSimilarity;
    }

    public void setWordSimilarity(double wordSimilarity) {
        this.wordSimilarity = wordSimilarity;
    }

    public double getWordBm25() {
        return wordBm25;
    }

    public void setWordBm25(double wordBm25) {
        this.wordBm25 = wordBm25;
    }
}
