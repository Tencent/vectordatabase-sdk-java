package com.tencent.tcvectordb.model.param.entity;

import com.tencent.tcvectordb.utils.JsonUtils;

public class ChunkInfo {
    private String text;
    private int endPos;
    private int startPos;

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
        return JsonUtils.toJsonString(this);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
