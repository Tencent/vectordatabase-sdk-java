package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tencent.tcvdbtext.exception.ParamException;
import com.tencent.tcvectordb.model.param.dml.InsertParam;

import java.util.List;

/**
 * Inner Insert Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertParamInner {
    private String database;
    private String collection;
    private List<Object> documents;
    private Boolean buildIndex;

    public InsertParamInner(String database, String collection, InsertParam param) {
        this.database = database;
        this.collection = collection;
        this.documents = param.getDocuments();
        this.buildIndex = param.isBuildIndex();
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public List<Object> getDocuments() {
        return documents;
    }

    public Boolean getBuildIndex() {
        return buildIndex;
    }

    public void setBuildIndex(boolean buildIndex) {
        this.buildIndex = buildIndex;
    }


    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("database", database);
        node.put("collection", collection);
        node.put("buildIndex", buildIndex);
        ArrayNode docsNode = JsonNodeFactory.instance.arrayNode();
        if (!documents.isEmpty()) {
            documents.forEach(doc -> {
                try {
                    docsNode.add(mapper.readTree(doc.toString()));
                } catch (JsonProcessingException e) {
                    throw new ParamException(String.format(
                            "Create document param error: %s", e));
                }
            });
        }
        node.set("documents", docsNode);
        return node.toString();
    }
}
