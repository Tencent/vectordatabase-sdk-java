package com.tencent.tcvectordb.model.param.collectionView;

import com.tencent.tcvectordb.model.DocumentSet;

public class LoadAndSplitTextParam {
    private String localFilePath;
    private String documentSetName;

    public LoadAndSplitTextParam(Builder builder) {
        this.localFilePath = builder.localFilePath;
        this.documentSetName = builder.documentSetName;
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

    public static Builder newBuilder() {
        return new Builder();
    }
    public static class Builder{
        private String localFilePath;
        private String documentSetName;

        public Builder withLocalFilePath(String localFilePath){
            this.localFilePath = localFilePath;
            return this;
        }

        public Builder withDocumentSetName(String documentSetName){
            this.documentSetName = documentSetName;
            return this;
        }

        public LoadAndSplitTextParam Build(){
            return new LoadAndSplitTextParam(this);
        }
    }

}
