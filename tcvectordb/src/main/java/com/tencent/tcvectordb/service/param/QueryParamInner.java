package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.tcvdbtext.exception.ParamException;
import com.tencent.tcvectordb.model.param.dml.QueryParam;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;

/**
 * Inner Query Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryParamInner {
    private String database;
    private String collection;
    private ReadConsistencyEnum readConsistency;
    private QueryParam query;

    public QueryParamInner(String database, String collection, QueryParam query, ReadConsistencyEnum readConsistency) {
        this.database = database;
        this.collection = collection;
        this.readConsistency = readConsistency;
        this.query = query;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public ReadConsistencyEnum getReadConsistency() {
        return readConsistency;
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
