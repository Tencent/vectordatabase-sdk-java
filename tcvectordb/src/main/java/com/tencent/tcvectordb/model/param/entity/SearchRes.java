package com.tencent.tcvectordb.model.param.entity;

import com.tencent.tcvectordb.model.Document;

import java.util.List;

public class SearchRes extends BaseRes {
    private List<List<Document>> documents;

    public List<List<Document>> getDocuments() {
        return documents;
    }

    public SearchRes(int code, String msg, String warning, List<List<Document>> documents) {
        super(code, msg, warning);
        this.documents = documents;
    }


    @Override
    public String toString() {
        return "SearchByEmRes{" +
                "documents=" + documents +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", warning='" + warning + '\'' +
                '}';
    }
}
