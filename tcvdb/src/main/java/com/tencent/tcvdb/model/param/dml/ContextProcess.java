package com.tencent.tcvdb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContextProcess {
    private List<Integer> returnContextChunks;

    public ContextProcess(List<Integer> returnContextChunks) {
        if (returnContextChunks == null || returnContextChunks.size()!=2) {
            throw new IllegalArgumentException("returnContextChunks must be a list of 2 integers");
        }
        this.returnContextChunks = returnContextChunks;
    }

    public List<Integer> getReturnContextChunks() {
        return returnContextChunks;
    }

    public void setReturnContextChunks(List<Integer> returnContextChunks) {
        if (returnContextChunks == null || returnContextChunks.size()!=2) {
            throw new IllegalArgumentException("returnContextChunks must be a list of 2 integers");
        }
        this.returnContextChunks = returnContextChunks;
    }
}
