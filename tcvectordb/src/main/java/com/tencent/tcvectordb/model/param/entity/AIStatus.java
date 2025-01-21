package com.tencent.tcvectordb.model.param.entity;

public class AIStatus {
    private long indexedDocumentSets;
    private long totalDocumentSets;
    private long unIndexedDocumentSets;
    private long failIndexedDocumentSets;

    public long getFailIndexedDocumentSets() {
        return failIndexedDocumentSets;
    }

    public void setFailIndexedDocumentSets(long failIndexedDocumentSets) {
        this.failIndexedDocumentSets = failIndexedDocumentSets;
    }

    public long getIndexedDocumentSets() {
        return indexedDocumentSets;
    }

    public void setIndexedDocumentSets(long indexedDocumentSets) {
        this.indexedDocumentSets = indexedDocumentSets;
    }

    public long getTotalDocumentSets() {
        return totalDocumentSets;
    }

    public void setTotalDocumentSets(long totalDocumentSets) {
        this.totalDocumentSets = totalDocumentSets;
    }

    public long getUnIndexedDocumentSets() {
        return unIndexedDocumentSets;
    }

    public void setUnIndexedDocumentSets(long unIndexedDocumentSets) {
        this.unIndexedDocumentSets = unIndexedDocumentSets;
    }
}
