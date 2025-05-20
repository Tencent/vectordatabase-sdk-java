package com.tencent.tcvectordb.model.param.collectionView;


import java.io.InputStream;


/**
 * LoadAndSplitTextParam upload file param
 *
 */
public class LoadAndSplitTextParam {
    /**
     * use local file path
     */
    private String localFilePath;
    /**
     * user input stream, when use this way,  documentSetName、inputStreamSize and fileType params must be specified
     */
    private InputStream fileInputStream;
    private String documentSetName;
    private Long inputStreamSize;
    private String fileType;

    private SplitterPreprocessParams splitterProcess;

    private ParsingProcessParam parsingProcess;

    private Integer byteLength;

    public LoadAndSplitTextParam(Builder builder) {
        this.localFilePath = builder.localFilePath;
        this.documentSetName = builder.documentSetName;
        this.splitterProcess = builder.splitterProcess;
        this.fileInputStream = builder.fileInputStream;
        this.fileType = builder.fileType;
        this.inputStreamSize = builder.InputStreamSize;
        this.parsingProcess = builder.parsingProcess;
        this.byteLength = builder.byteLength;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType.getValue();
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

    public Integer getByteLength() {
        return byteLength;
    }

    public void setByteLength(Integer byteLength) {
        this.byteLength = byteLength;
    }

    public static Builder newBuilder() {
        return new Builder();
    }
    public static class Builder{
        private String localFilePath;
        private String documentSetName;
        private SplitterPreprocessParams splitterProcess;

        private InputStream fileInputStream;
        private String fileType;
        private Long InputStreamSize;
        private ParsingProcessParam parsingProcess;

        private Integer byteLength;

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

        public Builder withFileInputStream(InputStream fileInputStream){
            this.fileInputStream = fileInputStream;
            return this;
        }


        public Builder withFileType(FileType fileType){
            this.fileType = fileType.getValue();
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

        public Builder withByteLength(Integer byteLenth){
            this.byteLength = byteLenth;
            return this;
        }

        public LoadAndSplitTextParam Build(){
            return new LoadAndSplitTextParam(this);
        }
    }

}
