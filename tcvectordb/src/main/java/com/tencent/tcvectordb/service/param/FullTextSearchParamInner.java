package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;



import com.tencent.tcvectordb.model.param.dml.FullTextSearchParam;

import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.utils.JsonUtils;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class FullTextSearchParamInner {
    private String database;
    private String collection;
    private FullTextSearchParam search;
    private ReadConsistencyEnum readConsistency = ReadConsistencyEnum.EVENTUAL_CONSISTENCY;

    public FullTextSearchParamInner(String database, String collection, FullTextSearchParam search, ReadConsistencyEnum readConsistency) {
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


    public FullTextSearchParam getSearch() {
        return search;
    }

    public ReadConsistencyEnum getReadConsistency() {
        return readConsistency;
    }

    @Override
    public String toString() {
        return  JsonUtils.toJsonString(this);
    }
}
