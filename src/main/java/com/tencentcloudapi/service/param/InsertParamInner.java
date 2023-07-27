package com.tencentcloudapi.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tencentcloudapi.exception.ParamException;
import com.tencentcloudapi.model.Document;

import java.util.List;

/**
 * Inner Insert Param
 * User: wlleiiwang
 * Date: 2023/7/26
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertParamInner {
    private String database;
    private String collection;
    private List<Document> documents;
    private Boolean buildIndex = true;

    public InsertParamInner(String database, String collection,
                            List<Document> documents) {
        this.database = database;
        this.collection = collection;
        this.documents = documents;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public List<Document> getDocuments() {
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
        documents.forEach(doc -> {
            try {
                docsNode.add(mapper.readTree(doc.toString()));
            } catch (JsonProcessingException e) {
                throw new ParamException(String.format(
                        "Create document param error: %s", e));
            }
        });
        node.set("documents", docsNode);
        return node.toString();
    }
}
