package com.tencentcloudapi.service.param;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencentcloudapi.exception.ParamException;
import com.tencentcloudapi.model.Document;

import java.util.List;

/**
 * Inner Insert Param
 * User: wlleiiwang
 * Date: 2023/7/26
 */
public class InsertParamInner {
    private String database;
    private String collection;
    private List<Document> documents;
    private boolean buildIndex = true;

    public InsertParamInner(String database, String collection,
                            List<Document> documents) {
        this.database = database;
        this.collection = collection;
        this.documents = documents;
    }

    public void setBuildIndex(boolean buildIndex) {
        this.buildIndex = buildIndex;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new ParamException(String.format(
                    "InsertParam error: %s", e.getMessage()));
        }
    }
}
