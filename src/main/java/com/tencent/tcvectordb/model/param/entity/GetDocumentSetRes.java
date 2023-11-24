package com.tencent.tcvectordb.model.param.entity;

import java.util.List;

public class GetDocumentSetRes extends BaseRes{
    private DocumentFileContent documentSet;

    public GetDocumentSetRes(int code, String msg, String warning, int count) {
        super(code, msg, warning);
        this.count = count;
    }

    public DocumentFileContent getDocumentSet() {
        return documentSet;
    }

    public void setDocumentSet(DocumentFileContent documentSet) {
        this.documentSet = documentSet;
    }

    @Override
    public String toString() {
        return "GetDoucmentSetRes{" +
                "document=" + documentSet +
                '}';
    }
}
