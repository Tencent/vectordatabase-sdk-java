package com.tencent.tcvectordb.model.param.entity;

import java.util.List;

public class ChunkInfo {
    private String text;
    private int endPos;
    private int startPos;
    private List<String> next;
    private List<String> pre;

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
                ", nextChunks=" + next +
                ", preChunks=" + pre +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getNext() {
        return next;
    }

    public void setNext(List<String> next) {
        this.next = next;
    }

    public List<String> getPre() {
        return pre;
    }

    public void setPre(List<String> pre) {
        this.pre = pre;
    }

}
