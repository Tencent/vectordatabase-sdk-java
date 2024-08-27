package com.tencent.tcvectordb.model.param.entity;

import com.tencent.tcvectordb.model.Document;

import java.util.List;

public class SearchContentRes extends BaseRes {
    private List<SearchContentInfo> documents;

    public List<SearchContentInfo> getDocuments() {
        return documents;
    }

    public SearchContentRes(int code, String msg, String warning, List<SearchContentInfo> documents) {
        super(code, msg, warning);
        this.documents = documents;
    }

    @Override
    public String toString() {
        return "SearchContentRes{" +
                "documents=" + documents +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", warning='" + warning + '\'' +
                '}';
    }
}
