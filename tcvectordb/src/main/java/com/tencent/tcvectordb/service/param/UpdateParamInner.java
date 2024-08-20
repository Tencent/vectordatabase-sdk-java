package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.dml.UpdateParam;
import com.tencent.tcvectordb.utils.JsonUtils;
import org.json.JSONObject;

/**
 * Inner Delete Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateParamInner {
    private String database;
    private String collection;
    private UpdateParam query;
    @JsonIgnore
    private Document update;
    private JSONObject updateData;

    public UpdateParamInner(String database, String collection, UpdateParam query, Document document) {
        this.database = database;
        this.collection = collection;
        this.query = query;
        this.update = document;
    }

    public UpdateParamInner(String database, String collection, UpdateParam query, JSONObject document) {
        this.database = database;
        this.collection = collection;
        this.query = query;
        this.updateData = document;
    }

    public JSONObject getUpdateData() {
        return updateData;
    }

    public void setUpdateData(JSONObject updateData) {
        this.updateData = updateData;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public UpdateParam getQuery() {
        return query;
    }

    public Document getUpdate() {
        return update;
    }

    @Override
    public String toString() {
        ObjectNode objectNode = (ObjectNode) JsonUtils.toJsonNode(this);
        if (update!=null){
            objectNode.put("update", JsonUtils.parseToJsonNode(update.toString()));
        }else if (updateData!=null){
            objectNode.put("update", JsonUtils.parseToJsonNode(updateData.toString()));
        }

        return objectNode.toString();
    }
    
}
