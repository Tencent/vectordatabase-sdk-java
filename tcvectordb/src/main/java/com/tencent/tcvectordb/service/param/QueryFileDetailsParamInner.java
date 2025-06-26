package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.model.param.dml.CountQueryParam;
import com.tencent.tcvectordb.model.param.dml.QueryFileDetailParam;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.utils.JsonUtils;

/**
 * Inner Query Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryFileDetailsParamInner {
    private String database;
    private String collection;
    private ReadConsistencyEnum readConsistency;
    private QueryFileDetailParam query;

    public QueryFileDetailsParamInner() {
    }

    public QueryFileDetailsParamInner(String database, String collection, QueryFileDetailParam query, ReadConsistencyEnum readConsistency) {
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

    public QueryFileDetailParam getQuery() {
        return query;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public void setReadConsistency(ReadConsistencyEnum readConsistency) {
        this.readConsistency = readConsistency;
    }

    public void setQuery(QueryFileDetailParam query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
