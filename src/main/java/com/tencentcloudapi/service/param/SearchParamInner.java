package com.tencentcloudapi.service.param;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencentcloudapi.exception.ParamException;
import com.tencentcloudapi.model.param.dml.SearchParam;

/**
 * Inner Search Param
 * User: wlleiiwang
 * Date: 2023/7/26
 */
public class SearchParamInner {
    private String database;
    private String collection;
    private SearchParam search;

    public SearchParamInner(String database, String collection, SearchParam search) {
        this.database = database;
        this.collection = collection;
        this.search = search;
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
