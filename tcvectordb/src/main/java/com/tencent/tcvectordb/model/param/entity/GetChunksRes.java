package com.tencent.tcvectordb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetChunksRes extends BaseRes{
    private String documentSetId;
    private String documentSetName;

    private List<ChunkInfo> chunks;

    public String getDocumentSetId() {
        return documentSetId;
    }

    public void setDocumentSetId(String documentSetId) {
        this.documentSetId = documentSetId;
    }

    public String getDocumentSetName() {
        return documentSetName;
    }

    public void setDocumentSetName(String documentSetName) {
        this.documentSetName = documentSetName;
    }

    public List<ChunkInfo> getChunks() {
        return chunks;
    }

    public void setChunks(List<ChunkInfo> chunks) {
        this.chunks = chunks;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
