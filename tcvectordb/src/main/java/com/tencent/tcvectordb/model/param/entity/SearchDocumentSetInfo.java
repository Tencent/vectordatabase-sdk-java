package com.tencent.tcvectordb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tencent.tcvectordb.model.DocField;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchDocumentSetInfo {
    private String documentSetName;
    private String documentSetId;
    private List<DocField> docFields;
    private Map<String, Object> docKeyValue;

    public SearchDocumentSetInfo(Builder builder) {
        this.documentSetId = builder.documentSetId;
        this.documentSetName = builder.documentSetName;
        this.docFields = builder.docFields;
    }

    @JsonIgnore
    public Map<String, Object> getDocKeyValue() {
        if (Objects.isNull(docFields) || docFields.isEmpty()) {
            return null;
        }
        ensureDocKeyValue();
        return docKeyValue;
    }

    @JsonIgnore
    public Object getObject(String key) {
        if (Objects.isNull(docFields) || docFields.isEmpty()) {
            return null;
        }
        ensureDocKeyValue();

        return docKeyValue.get(key);
    }

    private void ensureDocKeyValue() {
        if (Objects.isNull(docKeyValue)) {
            docKeyValue = new TreeMap<>();
            for (DocField docField : docFields) {
                docKeyValue.put(docField.getName(), docField.getValue());
            }
        }
    }
    public String getDocumentSetName() {
        return documentSetName;
    }

    public void setDocumentSetName(String documentSetName) {
        this.documentSetName = documentSetName;
    }

    public String getDocumentSetId() {
        return documentSetId;
    }

    public void setDocumentSetId(String documentSetId) {
        this.documentSetId = documentSetId;
    }

    public List<DocField> getDocFields() {
        return docFields;
    }

    public void setDocFields(List<DocField> docFields) {
        this.docFields = docFields;
    }

    @Override
    public String toString() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        if (documentSetName != null) {
            node.put("documentSetName", documentSetName);
        }
        if (documentSetId != null) {
            node.put("documentSetId", documentSetId);
        }
        if (docFields != null && !docFields.isEmpty()) {
            for (DocField field : docFields) {
                switch (field.getFieldType()) {
                    case Uint64:
                        node.put(field.getName(), Long.valueOf(field.getStringValue()));
                        break;
                    case Array:
                        List<String> strValues = (List<String>) ((List) field.getValue());
                        ArrayNode strNode = JsonNodeFactory.instance.arrayNode();
                        strValues.forEach(strNode::add);
                        node.set(field.getName(), strNode);
                        break;
                    default:
                        node.put(field.getName(), field.getStringValue());
                }
            }
        }
        return node.toString();
    }

    public void addFilterField(DocField docField) {
        if(this.getDocFields()==null) {
            this.docFields = new ArrayList<>();
        }
        this.docFields.add(docField);
    }
    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String documentSetName;
        private String documentSetId;
        private List<DocField> docFields;

        public Builder() {
            this.docFields = new ArrayList<>();
        }

        public Builder withDocumentSetName(String documentSetName){
            this.documentSetName = documentSetName;
            return this;
        }

        public Builder withDocumentSetId(String documentSetId){
            this.documentSetId = documentSetId;
            return this;
        }

        public Builder addDocField(DocField docField) {
            this.docFields.add(docField);
            return this;
        }

        public SearchDocumentSetInfo build() {
            return new SearchDocumentSetInfo(this);
        }
    }
}
