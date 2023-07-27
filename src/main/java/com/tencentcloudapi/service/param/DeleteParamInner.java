package com.tencentcloudapi.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencentcloudapi.exception.ParamException;
import com.tencentcloudapi.model.param.dml.DeleteParam;

/**
 * Inner Delete Param
 * User: wlleiiwang
 * Date: 2023/7/26
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteParamInner {
    private String database;
    private String collection;
    private DeleteParam query;

    public DeleteParamInner(String database, String collection, DeleteParam query) {
        this.database = database;
        this.collection = collection;
        this.query = query;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public DeleteParam getQuery() {
        return query;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new ParamException(String.format(
                    "DeleteParam error: %s", e.getMessage()));
        }
    }
}
