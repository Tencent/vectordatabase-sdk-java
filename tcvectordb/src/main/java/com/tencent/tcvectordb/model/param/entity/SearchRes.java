package com.tencent.tcvectordb.model.param.entity;

import com.tencent.tcvectordb.model.Document;

import java.util.List;

public class SearchRes extends BaseRes {
    private List<List<Document>> documents;
    private EmbeddingExtraInfo embeddingExtraInfo;

    public List<List<Document>> getDocuments() {
        return documents;
    }

    public void setDocuments(List<List<Document>> documents) {
        this.documents = documents;
    }

    public SearchRes() {
    }

    public SearchRes(int code, String msg, String warning, List<List<Document>> documents) {
        super(code, msg, warning);
        this.documents = documents;
    }

    public EmbeddingExtraInfo getEmbeddingExtraInfo() {
        return embeddingExtraInfo;
    }

    public void setEmbeddingExtraInfo(EmbeddingExtraInfo embeddingExtraInfo) {
        this.embeddingExtraInfo = embeddingExtraInfo;
    }

    @Override
    public String toString() {
        return "SearchRes{" +
                "embeddingExtraInfo=" + embeddingExtraInfo +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", warning='" + warning + '\'' +
                '}';
    }
}
