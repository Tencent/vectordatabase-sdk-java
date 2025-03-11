package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.model.param.collection.IndexField;
import com.tencent.tcvectordb.model.param.dml.AddIndexParam;
import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DropIndexParamInner {
    private String database;
    private String collection;
    private List<String> filedNames;


    public DropIndexParamInner(String database, String collection, List<String> filedNames) {
        this.database = database;
        this.collection = collection;
        this.filedNames = filedNames;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public List<String> getFiledNames() {
        return filedNames;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
