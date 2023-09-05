package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.dml.UpdateParam;
import com.tencent.tcvectordb.utils.JsonUtils;

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

    public UpdateParamInner(String database, String collection, UpdateParam query, Document document) {
        this.database = database;
        this.collection = collection;
        this.query = query;
        this.update = document;
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
        objectNode.put("update", JsonUtils.parseToJsonNode(update.toString()));
        return objectNode.toString();
    }
    
}
