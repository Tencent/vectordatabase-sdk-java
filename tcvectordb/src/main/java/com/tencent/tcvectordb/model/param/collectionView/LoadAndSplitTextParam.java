package com.tencent.tcvectordb.model.param.collectionView;

import com.tencent.tcvectordb.model.DocumentSet;

public class LoadAndSplitTextParam {
    private String localFilePath;
    private String documentSetName;
    private SplitterPreprocessParams splitterProcess;

    public LoadAndSplitTextParam(Builder builder) {
        this.localFilePath = builder.localFilePath;
        this.documentSetName = builder.documentSetName;
        this.splitterProcess = builder.splitterProcess;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getDocumentSetName() {
        return documentSetName;
    }

    public void setDocumentSetName(String documentSetName) {
        this.documentSetName = documentSetName;
    }

    public SplitterPreprocessParams getSplitterProcess() {
        return splitterProcess;
    }

    public void setSplitterProcess(SplitterPreprocessParams splitterProcess) {
        this.splitterProcess = splitterProcess;
    }

    public static Builder newBuilder() {
        return new Builder();
    }
    public static class Builder{
        private String localFilePath;
        private String documentSetName;
        private SplitterPreprocessParams splitterProcess;

        public Builder withLocalFilePath(String localFilePath){
            this.localFilePath = localFilePath;
            return this;
        }

        public Builder withDocumentSetName(String documentSetName){
            this.documentSetName = documentSetName;
            return this;
        }

        public Builder withSplitterProcess(SplitterPreprocessParams splitterProcess){
            this.splitterProcess = splitterProcess;
            return this;
        }

        public LoadAndSplitTextParam Build(){
            return new LoadAndSplitTextParam(this);
        }
    }

}
