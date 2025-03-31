package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DropIndexParamInner {
    private String database;
    private String collection;
    private List<String> fieldNames;


    public DropIndexParamInner(String database, String collection, List<String> fieldNames) {
        this.database = database;
        this.collection = collection;
        this.fieldNames = fieldNames;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public List<String> getFieldNames() {
        return fieldNames;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
