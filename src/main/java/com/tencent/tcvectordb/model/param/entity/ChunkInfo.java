package com.tencent.tcvectordb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChunkInfo {
    private String text;
    private int endPos;
    private int startPos;
    private List<String> nextChunks;
    private List<String> preChunks;

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    @Override
    public String toString() {
        return "ChunkInfo{" +
                "text='" + text + '\'' +
                ", endPos=" + endPos +
                ", startPos=" + startPos +
                ", nextChunks=" + nextChunks +
                ", preChunks=" + preChunks +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getNextChunks() {
        return nextChunks;
    }

    public void setNextChunks(List<String> nextChunks) {
        this.nextChunks = nextChunks;
    }

    public List<String> getPreChunks() {
        return preChunks;
    }

    public void setPreChunks(List<String> preChunks) {
        this.preChunks = preChunks;
    }

}
