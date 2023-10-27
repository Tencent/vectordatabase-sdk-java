package com.tencent.tcvectordb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChunkInfo {
    private String chunk;
    @JsonProperty(value = "endPos")
    private int endPos;
    @JsonProperty(value = "startPos")
    private int startPos;
    private List<String> chunkIds;

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

    public List<String> getChunkIds() {
        return chunkIds;
    }

    public void setChunkIds(List<String> chunkIds) {
        this.chunkIds = chunkIds;
    }

    @Override
    public String toString() {
        return "ChunkInfo{" +
                "chunk='" + chunk + '\'' +
                ", chunkIds=" + chunkIds +
                ", endPos=" + endPos +
                ", startPos=" + startPos +
                '}';
    }

    public String getChunk() {
        return chunk;
    }

    public void setChunk(String chunk) {
        this.chunk = chunk;
    }
}
