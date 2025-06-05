package com.tencent.tcvectordb.model.param.entity;

import java.util.List;


public class QueryFileDetailRes extends BaseRes{
    private List<FileDetail> documents;

    public List<FileDetail> getDocuments() {
        return documents;
    }

    public void setDocuments(List<FileDetail> documents) {
        this.documents = documents;
    }
}
