package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.model.param.dml.RebuildIndexParam;
import com.tencent.tcvectordb.utils.JsonUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RebuildIndexParamInner {
    private String database;
    private String collection;
    private boolean dropBeforeRebuild;
    private int throttle;


    public RebuildIndexParamInner(String database, String collection, RebuildIndexParam rebuildIndexParam) {
        this.database = database;
        this.collection = collection;
        this.dropBeforeRebuild = rebuildIndexParam.getDropBeforeRebuild();
        this.throttle = rebuildIndexParam.getThrottle();
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public boolean isDropBeforeRebuild() {
        return dropBeforeRebuild;
    }

    public int getThrottle() {
        return throttle;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
