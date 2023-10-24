package com.tencent.tcvectordb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentIndexParams implements ParamsSerializer{
    /**
     * 是否启用Word级别的Similarity
     * 	false: 只做Chunk的Similarity
     * 	true: 做Chunk、Word级别的双路Similarity（default）
     */
    @JsonProperty(value = "enable_words_similarity")
    private boolean enableWordsSimilarity;

    @JsonProperty(value = "enableFulltextSearch")
    private boolean enableFulltextSearch;

    public DocumentIndexParams() {
    }

    public DocumentIndexParams(boolean enableWordsSimilarity, boolean enableFulltextSearch) {
        this.enableWordsSimilarity = enableWordsSimilarity;
        this.enableFulltextSearch = enableFulltextSearch;
    }

    public boolean isEnableWordsSimilarity() {
        return enableWordsSimilarity;
    }

    public void setEnableWordsSimilarity(boolean enableWordsSimilarity) {
        this.enableWordsSimilarity = enableWordsSimilarity;
    }

    public boolean isEnableFulltextSearch() {
        return enableFulltextSearch;
    }

    public void setEnableFulltextSearch(boolean enableFulltextSearch) {
        this.enableFulltextSearch = enableFulltextSearch;
    }
}
