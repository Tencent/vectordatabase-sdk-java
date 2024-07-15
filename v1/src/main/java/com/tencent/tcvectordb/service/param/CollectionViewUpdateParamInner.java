package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tencent.tcvectordb.model.DocField;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.dml.CollectionViewConditionParam;
import com.tencent.tcvectordb.model.param.dml.UpdateParam;
import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.Map;

/**
 * Inner Delete Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionViewUpdateParamInner {
    private String database;
    private String collectionView;
    private CollectionViewConditionParam query;
    @JsonIgnore
    private Document update;

    public CollectionViewUpdateParamInner(String database, String collection, CollectionViewConditionParam query,
                                          Map<String, Object> updateFieldValues) {
        this.database = database;
        this.collectionView = collection;
        this.query = query;
        this.update = covertMapDocument(updateFieldValues);
    }

    private Document covertMapDocument(Map<String, Object> updateFieldValues) {
        Document.Builder builder = Document.newBuilder();
        updateFieldValues.forEach((key, value)->{
            builder.addDocField(new DocField(key, value));
        });
        return builder.build();
    }

    public String getDatabase() {
        return database;
    }

    public String getCollectionView() {
        return collectionView;
    }

    public CollectionViewConditionParam getQuery() {
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
