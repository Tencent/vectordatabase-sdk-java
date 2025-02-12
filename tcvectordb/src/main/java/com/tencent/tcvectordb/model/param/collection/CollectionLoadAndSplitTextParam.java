package com.tencent.tcvectordb.model.param.collection;


import com.tencent.tcvectordb.model.param.collectionView.FileType;
import com.tencent.tcvectordb.model.param.collectionView.ParsingProcessParam;
import com.tencent.tcvectordb.model.param.collectionView.SplitterPreprocessParams;

import java.io.InputStream;
import java.util.Map;


/**
 * LoadAndSplitTextParam upload file param
 *
 */
public class CollectionLoadAndSplitTextParam {
    /**
     * use local file path
     */
    private String localFilePath;
    /**
     * user input stream, when use this way,  fileName、inputStreamSize and fileType params must be specified
     */
    private InputStream fileInputStream;
    private Long inputStreamSize;

    private SplitterPreprocessParams splitterProcess;

    private ParsingProcessParam parsingProcess;

    private Map<String, String> fieldMappings;

    private String embeddingModel;

    private String fileName;

    public CollectionLoadAndSplitTextParam(Builder builder) {
        this.localFilePath = builder.localFilePath;
        this.splitterProcess = builder.splitterProcess;
        this.fileInputStream = builder.fileInputStream;
        this.inputStreamSize = builder.InputStreamSize;
        this.parsingProcess = builder.parsingProcess;
        this.fieldMappings = builder.fieldMappings;
        this.embeddingModel = builder.embeddingModel;
        this.fileName = builder.fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getEmbeddingModel() {
        return embeddingModel;
    }

    public void setEmbeddingModel(String embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public Long getInputStreamSize() {
        return inputStreamSize;
    }

    public void setInputStreamSize(Long inputStreamSize) {
        this.inputStreamSize = inputStreamSize;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public SplitterPreprocessParams getSplitterProcess() {
        return splitterProcess;
    }

    public void setSplitterProcess(SplitterPreprocessParams splitterProcess) {
        this.splitterProcess = splitterProcess;
    }

    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    public void setFileInputStream(InputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

    public ParsingProcessParam getParsingProcess() {
        return parsingProcess;
    }

    public void setParsingProcess(ParsingProcessParam parsingProcess) {
        this.parsingProcess = parsingProcess;
    }

    public Map<String, String> getFieldMappings() {
        return fieldMappings;
    }

    public void setFieldMappings(Map<String, String> fieldMappings) {
        this.fieldMappings = fieldMappings;
    }

    public static Builder newBuilder() {
        return new Builder();
    }
    public static class Builder{
        private String localFilePath;
        private SplitterPreprocessParams splitterProcess;

        private InputStream fileInputStream;
        private Long InputStreamSize;
        private ParsingProcessParam parsingProcess;
        private Map<String, String> fieldMappings;
        private String embeddingModel;

        private String fileName;

        public Builder withFileName(String fileName){
            this.fileName = fileName;
            return this;
        }

        public Builder withEmbeddingModel(String embeddingModel){
            this.embeddingModel = embeddingModel;
            return this;
        }

        public Builder withFieldMappings(Map<String, String> fieldMappings){
            this.fieldMappings = fieldMappings;
            return this;
        }

        public Builder withLocalFilePath(String localFilePath){
            this.localFilePath = localFilePath;
            return this;
        }

        public Builder withSplitterProcess(SplitterPreprocessParams splitterProcess){
            this.splitterProcess = splitterProcess;
            return this;
        }

        public Builder withFileInputStream(InputStream fileInputStream){
            this.fileInputStream = fileInputStream;
            return this;
        }

        public Builder withInputStreamDataSize(Long inputStreamDataSize){
            this.InputStreamSize = inputStreamDataSize;
            return this;
        }

        public Builder withParsingProcess(ParsingProcessParam parsingProcess){
            this.parsingProcess = parsingProcess;
            return this;
        }

        public CollectionLoadAndSplitTextParam Build(){
            return new CollectionLoadAndSplitTextParam(this);
        }
    }

}
