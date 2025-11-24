package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;



import com.tencent.tcvectordb.model.param.dml.CountQueryParam;

import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.utils.JsonUtils;

/**
 * Inner Query Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryCountParamInner {
    private String database;
    private String collection;
    private ReadConsistencyEnum readConsistency;
    private CountQueryParam query;

    public QueryCountParamInner(String database, String collection, CountQueryParam query, ReadConsistencyEnum readConsistency) {
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

    public CountQueryParam getQuery() {
        return query;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
