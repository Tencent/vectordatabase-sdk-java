package com.tencent.tcvectordb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChunkInfo {
    private String chunk;
    @JsonProperty(value = "endPos")
    private int endPos;
    @JsonProperty(value = "startPos")
    private int startPos;

    @Override
    public String toString() {
        return "ChunkInfo{" +
                "chunk='" + chunk + '\'' +
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
