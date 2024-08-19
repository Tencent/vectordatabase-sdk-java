package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.tencent.tcvectordb.exception.ParamException;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.dml.InsertParam;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Inner Insert Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertParamInner {
    private String database;
    private String collection;
    private List<Document> documents;

    private List<JSONObject> documentsData;
    private Boolean buildIndex;

    public InsertParamInner(String database, String collection, InsertParam param) {
        this.database = database;
        this.collection = collection;
        this.documents = param.getDocuments();
        this.buildIndex = param.isBuildIndex();
        this.documentsData = param.getDocumentsData();
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

    public List<JSONObject> getDocumentsData() {
        return documentsData;
    }

    public void setDocumentsData(List<JSONObject> documentsData) {
        this.documentsData = documentsData;
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
        if (documents.isEmpty() && !documentsData.isEmpty()) {
            documentsData.forEach(doc -> {
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

    public static void main(String[] args) throws InterruptedException {
        List<JSONObject> documentList = Arrays.asList(
                new JSONObject("{\"id\": 1, \"vector\": [0.2123, 0.21, 0.213], \"bookName\": \"西游记\"}"),
                new JSONObject("{\"id\": 2, \"vector\": [0.2123, 0.22, 0.213], \"bookName\": \"西游记11\"}"),
                new JSONObject("{\"id\": 3, \"vector\": [0.2123, 0.23, 0.213], \"bookName\": \"三国演义\"}"),
                new JSONObject("{\"id\": 4, \"vector\": [0.2123, 0.24, 0.213], \"bookName\": \"三国演义11\"}")
                );
        InsertParamInner param = new InsertParamInner("test", "test", InsertParam.newBuilder().withAllDocumentsData(documentList).withBuildIndex(true).build());
        System.out.println(param.toString());
    }
}
