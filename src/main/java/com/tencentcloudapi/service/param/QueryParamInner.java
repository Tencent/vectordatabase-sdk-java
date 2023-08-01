package com.tencentcloudapi.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencentcloudapi.exception.ParamException;
import com.tencentcloudapi.model.param.dml.QueryParam;

/**
 * Inner Query Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryParamInner {
    private String database;
    private String collection;
    private QueryParam query;

    public QueryParamInner(String database, String collection, QueryParam query) {
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

    public QueryParam getQuery() {
        return query;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new ParamException(String.format(
                    "QueryParam error: %s", e.getMessage()));
        }
    }
}
