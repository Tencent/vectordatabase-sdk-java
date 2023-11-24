package com.tencent.tcvectordb.model.param.entity;

import com.tencent.tcvectordb.model.DocField;

import java.util.ArrayList;
import java.util.List;

public class DocumentFileContent {
    private String documentSetName;
    private String documentSetId;
    private String text;
    private DocumentSetInfo documentSetInfo;

    private List<DocField> docFields;

    public String getDocumentSetName() {
        return documentSetName;
    }

    public void setDocumentSetName(String documentSetName) {
        this.documentSetName = documentSetName;
    }

    public String getDocumentSetId() {
        return documentSetId;
    }

    public void setDocumentSetId(String documentSetId) {
        this.documentSetId = documentSetId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DocumentSetInfo getDocumentSetInfo() {
        return documentSetInfo;
    }

    public void setDocumentSetInfo(DocumentSetInfo documentSetInfo) {
        this.documentSetInfo = documentSetInfo;
    }

    public List<DocField> getDocFields() {
        return docFields;
    }

    public void setDocFields(List<DocField> docFields) {
        this.docFields = docFields;
    }

    @Override
    public String toString() {
        return "DocumentFileContent{" +
                "documentSetName='" + documentSetName + '\'' +
                ", documentSetId='" + documentSetId + '\'' +
                ", text='" + text + '\'' +
                ", documentSetInfo=" + documentSetInfo +
                '}';
    }

    public void addFilterField(DocField docField) {
        if(this.getDocFields()==null) {
            this.docFields = new ArrayList<>();
        }
        this.docFields.add(docField);
    }
}
