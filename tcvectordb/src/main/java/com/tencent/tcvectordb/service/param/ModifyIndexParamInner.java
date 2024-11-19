package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.model.param.collection.IndexField;
import com.tencent.tcvectordb.model.param.dml.AddIndexParam;
import com.tencent.tcvectordb.model.param.dml.ModifyVectorIndexParam;
import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModifyIndexParamInner {
    private String database;
    private String collection;
    private ModifyVectorIndexParam modifyVectorIndexParam;

    public ModifyIndexParamInner(String database, String collection, ModifyVectorIndexParam modifyVectorIndexParam) {
        this.database = database;
        this.collection = collection;
        this.modifyVectorIndexParam = modifyVectorIndexParam;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public ModifyVectorIndexParam getModifyVectorIndexParam() {
        return modifyVectorIndexParam;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
