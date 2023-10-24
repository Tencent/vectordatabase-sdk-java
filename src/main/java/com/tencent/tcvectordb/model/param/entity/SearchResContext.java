package com.tencent.tcvectordb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SearchResContext {
    @JsonProperty(value = "nextChunks")
    private List<String> nextChunks;
    @JsonProperty(value = "preChunks")
    private List<String> preChunks;

    @Override
    public String toString() {
        return "SearchResContext{" +
                "nextChunks=" + nextChunks +
                ", preChunks=" + preChunks +
                '}';
    }

    public SearchResContext() {
    }

    public List<String> getPreChunks() {
        return preChunks;
    }

    public void setPreChunks(List<String> preChunks) {
        this.preChunks = preChunks;
    }

    public List<String> getNextChunks() {
        return nextChunks;
    }

    public void setNextChunks(List<String> nextChunks) {
        this.nextChunks = nextChunks;
    }
}
