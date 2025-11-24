package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.model.param.collection.IndexField;
import com.tencent.tcvectordb.model.param.dml.AddIndexParam;

import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddIndexParamInner {
    private String database;
    private String collection;
    private boolean buildExistedData;
    private List<IndexField> indexes;


    public AddIndexParamInner(String database, String collection, AddIndexParam addIndexParam) {
        this.database = database;
        this.collection = collection;
        this.indexes = addIndexParam.getIndexes();
        this.buildExistedData = addIndexParam.getBuildExistedData();
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public boolean isBuildExistedData() {
        return buildExistedData;
    }

    public List<IndexField> getIndexes() {
        return indexes;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
