package com.tencent.tcvectordb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tencent.tcvectordb.model.param.collection.DocumentIndexParams;
import com.tencent.tcvectordb.model.param.collection.DocumentPreprocessParams;

public class AIConfig {
    private int maxFiles;
    // 文件的平均大小
    private int averageFileSize;
    private String language;
    private DocumentPreprocessParams documentPreprocess;
    private DocumentIndexParams documentIndex;

    public int getMaxFiles() {
        return maxFiles;
    }

    public void setMaxFiles(int maxFiles) {
        this.maxFiles = maxFiles;
    }

    public int getAverageFileSize() {
        return averageFileSize;
    }

    public void setAverageFileSize(int averageFileSize) {
        this.averageFileSize = averageFileSize;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public DocumentPreprocessParams getDocumentPreprocess() {
        return documentPreprocess;
    }

    public void setDocumentPreprocess(DocumentPreprocessParams documentPreprocess) {
        this.documentPreprocess = documentPreprocess;
    }

    public DocumentIndexParams getDocumentIndex() {
        return documentIndex;
    }

    public void setDocumentIndex(DocumentIndexParams documentIndex) {
        this.documentIndex = documentIndex;
    }
}
