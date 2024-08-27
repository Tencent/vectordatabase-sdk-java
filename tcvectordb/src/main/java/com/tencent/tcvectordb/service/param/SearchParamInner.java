package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.tcvectordb.exception.ParamException;
import com.tencent.tcvectordb.model.param.dml.SearchParam;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;

/**
 * Inner Search Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchParamInner {
    private String database;
    private String collection;
    private SearchParam search;
    private ReadConsistencyEnum readConsistency = ReadConsistencyEnum.EVENTUAL_CONSISTENCY;

    public SearchParamInner(String database, String collection, SearchParam search, ReadConsistencyEnum readConsistency) {
        this.database = database;
        this.collection = collection;
        this.search = search;
        this.readConsistency = readConsistency;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public SearchParam getSearch() {
        return search;
    }

    public ReadConsistencyEnum getReadConsistency() {
        return readConsistency;
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
