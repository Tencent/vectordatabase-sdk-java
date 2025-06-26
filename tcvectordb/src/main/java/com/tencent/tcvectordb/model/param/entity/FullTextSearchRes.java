package com.tencent.tcvectordb.model.param.entity;

import com.tencent.tcvectordb.model.Document;

import java.util.List;

public class FullTextSearchRes extends BaseRes {
    private List<Document> documents;

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public FullTextSearchRes(int code, String msg, String warning, List<Document> documents) {
        super(code, msg, warning);
        this.documents = documents;
    }

}
