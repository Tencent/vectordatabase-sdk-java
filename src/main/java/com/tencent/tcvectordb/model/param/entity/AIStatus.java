package com.tencent.tcvectordb.model.param.entity;

public class AIStatus {
    private long indexedDocuments;
    private long totalDocuments;
    private long unIndexedDocuments;

    public long getIndexedDocuments() {
        return indexedDocuments;
    }

    public void setIndexedDocuments(long indexedDocuments) {
        this.indexedDocuments = indexedDocuments;
    }

    public long getTotalDocuments() {
        return totalDocuments;
    }

    public void setTotalDocuments(long totalDocuments) {
        this.totalDocuments = totalDocuments;
    }

    public long getUnIndexedDocuments() {
        return unIndexedDocuments;
    }

    public void setUnIndexedDocuments(long unIndexedDocuments) {
        this.unIndexedDocuments = unIndexedDocuments;
    }
}
