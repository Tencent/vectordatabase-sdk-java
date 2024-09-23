package com.tencent.tcvectordb.model.param.entity;

import com.tencent.tcvectordb.model.Document;

import java.util.List;

public class HybridSearchRes extends BaseRes {
    private List<Object> documents;

    public List<Object> getDocuments() {
        return documents;
    }

    public HybridSearchRes(int code, String msg, String warning, List<Object> documents) {
        super(code, msg, warning);
        this.documents = documents;
    }


    @Override
    public String toString() {
        return "HybridSearchRes{" +
                "documents=" + documents +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", warning='" + warning + '\'' +
                '}';
    }
}
