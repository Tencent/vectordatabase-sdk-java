package tcvdb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import tcvdb.model.Document;
import tcvdb.model.param.dml.InsertParam;
import tcvdb.utils.JsonUtils;

import java.util.List;

/**
 * Inner Insert Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertParamInner {
    private String database;
    private String collection;
    private List<Document> documents;
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
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("database", database);
        node.put("collection", collection);
        node.put("buildIndex", buildIndex);
        ArrayNode docsNode = JsonNodeFactory.instance.arrayNode();
        documents.forEach(doc -> docsNode.add(JsonUtils.parseToJsonNode(doc.toString())));
        node.set("documents", docsNode);
        return node.toString();
    }
}
