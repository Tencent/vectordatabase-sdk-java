package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.model.param.collection.VectorIndex;
import com.tencent.tcvectordb.model.param.dml.ModifyVectorIndexParam;
import com.tencent.tcvectordb.model.param.dml.RebuildIndexParam;
import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModifyIndexParamInner {
    private String database;
    private String collection;
    private RebuildIndexParam rebuildRules;
    private List<VectorIndex> vectorIndexes;

    public ModifyIndexParamInner(String database, String collection, ModifyVectorIndexParam modifyVectorIndexParam) {
        this.database = database;
        this.collection = collection;
        this.rebuildRules = modifyVectorIndexParam.getRebuildRules();
        this.vectorIndexes = modifyVectorIndexParam.getVectorIndexes();
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public RebuildIndexParam getRebuildRules() {
        return rebuildRules;
    }

    public List<VectorIndex> getVectorIndexes() {
        return vectorIndexes;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
