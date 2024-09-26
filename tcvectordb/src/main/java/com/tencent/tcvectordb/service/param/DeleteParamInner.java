package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.tcvdbtext.exception.ParamException;
import com.tencent.tcvectordb.model.param.dml.DeleteParam;

/**
 * Inner Delete Param
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
