/*
 * Copyright (C) 2023 Tencent Cloud.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the vectordb-sdk-java), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package tcvdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.tuple.Pair;
import tcvdb.model.param.dml.ContextResult;
import org.apache.commons.lang3.StringUtils;
import tcvdb.model.param.dml.InsertParam;
import tcvdb.utils.JsonUtils;

import java.util.*;

/**
 * VectorDB Document
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Document {
    private String id;
    private List<Object> vector;
    private Double score;
    private String text;
    private List<Pair<Integer,Double>> sparseVector;
    private String textField;
    private String oldField;
    private String newField;
    private ContextResult contextResult;
    private List<DocField> docFields;
    private Map<String, Object> docKeyValue;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ContextResult getContextResult() {
        return contextResult;
    }

    public void setContextResult(ContextResult contextResult) {
        this.contextResult = contextResult;
    }

    public String getId() {
        return id;
    }

    public Double getScore() {
        return score;
    }

    public List<DocField> getDocFields() {
        return docFields;
    }

    public Map<String, Object> getDocKeyValue() {
        return docKeyValue;
    }

    public List<Object> getVector() {
        if (vector==null || vector.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(vector);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVector(List<Object> vector) {
        this.vector = vector;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public List<Pair<Integer, Double>> getSparseVector() {
        return sparseVector;
    }

    public void setSparseVector(List<Pair<Integer, Double>> sparseVector) {
        this.sparseVector = sparseVector;
    }

    public String getTextField() {
        return textField;
    }

    public void setTextField(String textField) {
        this.textField = textField;
    }

    public String getOldField() {
        return oldField;
    }

    public void setOldField(String oldField) {
        this.oldField = oldField;
    }

    public String getNewField() {
        return newField;
    }

    public void setNewField(String newField) {
        this.newField = newField;
    }

    public void setDocFields(List<DocField> docFields) {
        this.docFields = docFields;
    }

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

    @Override
    public String toString() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        if (StringUtils.isNotBlank(id)) {
            node.put("id", id);
        }
        if (vector != null && !vector.isEmpty()) {
            ArrayNode vectorNode = JsonNodeFactory.instance.arrayNode();
            vector.forEach(ele->{
                if (ele instanceof Double) {
                    vectorNode.add(((Double) ele).doubleValue());
                }
                if (ele instanceof String) {
                    vectorNode.add(ele.toString());
                }
            });
            node.set("vector", vectorNode);
        }
        if (score != null) {
            node.put("score", score);
        }
        if (contextResult != null) {
            node.put("context_result", JsonUtils.toJsonNode(contextResult));
        }
        if (text != null && !text.isEmpty()) {
            node.put("text", text);
        }
        if (textField != null && !textField.isEmpty()) {
            node.put("textField", textField);
        }
        if (oldField != null && !oldField.isEmpty()) {
            node.put("old_field", oldField);
        }
        if (newField != null && !newField.isEmpty()) {
            node.put("new_field", newField);
        }
        if (sparseVector != null && !sparseVector.isEmpty()) {
            node.put("sparse_vector", JsonUtils.toJsonNode(sparseVector));
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

    private Document(Builder builder) {
        this.id = builder.id;
        this.vector = builder.vector;
        this.score = builder.score;
        this.docFields = builder.docFields;
        this.contextResult = builder.contextResult;
        this.sparseVector = builder.sparseVector;
        this.text = builder.text;
        this.textField = builder.textField;
        this.oldField = builder.oldField;
        this.newField = builder.newField;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private List<Object> vector;

        private Double score;
        private List<DocField> docFields;
        private String text;
        private List<Pair<Integer,Double>> sparseVector;
        private String textField;
        private String oldField;
        private String newField;
        private ContextResult contextResult;

        public Builder() {
            this.docFields = new ArrayList<>();
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withVector(List<Object> vector) {
            this.vector = vector;
            return this;
        }

        public Builder withScore(Double score) {
            this.score = score;
            return this;
        }
        public Builder withText(String text) {
            this.text = text;
            return this;
        }
        public Builder withSparseVector(List<Pair<Integer,Double>> sparseVector) {
            this.sparseVector = sparseVector;
            return this;
        }
        public Builder withContextResult(ContextResult contextResult) {
            this.contextResult = contextResult;
            return this;
        }
        public Builder withTextField(String textField) {
            this.textField = textField;
            return this;
        }

        public Builder withOldField(String oldField) {
            this.oldField = oldField;
            return this;
        }

        public Builder withNewField(String newField) {
            this.newField = newField;
            return this;
        }

        /**
         * This is a deprecated method.
         *
         * @param field
         * @return
         * @deprecated This method is deprecated and should not be used anymore. Please use the
         * addDocField(DocField field) or addDocFields(List<DocField> docFields) instead.
         */
        @Deprecated
        public Builder addFilterField(DocField field) {
            this.docFields.add(field);
            return this;
        }

        public Builder addDocField(DocField docField) {
            this.docFields.add(docField);
            return this;
        }

        public Builder addDocFields(List<DocField> docFields) {
            this.docFields.addAll(docFields);
            return this;
        }

        public Document build() {
            return new Document(this);
        }
    }
}