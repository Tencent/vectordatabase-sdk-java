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

package com.tencent.tcvectordb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tencent.tcvectordb.model.param.collection.FieldType;
import com.tencent.tcvectordb.utils.ConvertUtils;
import com.tencent.tcvectordb.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import java.util.*;

/**
 * VectorDB Document
 * <ol>
 * <li> id: Unique identifier for the document, id must be set primaryKey when create collection </li>
 * <li> vector: vector of document, this name of document's vector, dimension of the vector must be set when create collection
 *       eg: use addField(new VectorIndex("vector", 3, IndexType.HNSW, MetricType.COSINE, new HNSWParams(16, 200))) when create collection
 *           document can use withVector(Arrays.asList(0.2123, 0.22, 0.213)) to set vector of document </li>
 * <li> sparseVector：sparse vector of document should be set if collection use sparse vector,
 *      eg: use addField(new SparseVectorIndex("sparse_vector", IndexType.INVERTED, MetricType.IP)) when create collection
 *          document can use withSparseVector(Arrays.asList(Pair.of(12233l,0，3), ...)) to set sparse vector of document
 *      </li>
 * <li> score: the score will be set if search document</li>
 * <li> docFields: the scalar field of the document,</li>
 * </ol>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Document {
    private String id;
    private Object vector;
    private List<Pair<Long,Float>> sparseVector;
    private Double score;
    private String doc;
    private List<DocField> docFields;
    private Map<String, Object> docKeyValue;

    public String getId() {
        return id;
    }

    public Double getScore() {
        return score;
    }

    public String getDoc() {
        return doc;
    }

    public List<DocField> getDocFields() {
        return docFields;
    }

    public Map<String, Object> getDocKeyValue() {
        return docKeyValue;
    }

    public Object getVector() {
        return vector;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVector(Object vector) {
        this.vector = vector;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public void setDocFields(List<DocField> docFields) {
        this.docFields = docFields;
    }


    public List<Pair<Long, Float>> getSparseVector() {
        return sparseVector;
    }

    public void setSparseVector(List<Pair<Long, Float>> sparseVector) {
        this.sparseVector = sparseVector;
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
        if (vector != null) {
            if(vector instanceof List){
                ArrayNode vectorNode = JsonNodeFactory.instance.arrayNode();
                ((List<?>) vector).forEach(ele->{
                    if (ele instanceof Number) {
                        vectorNode.add(((Number) ele).doubleValue());
                    }
                    if (ele instanceof String) {
                        vectorNode.add(ele.toString());
                    }
                });
                node.set("vector", vectorNode);
            }
            if (vector instanceof String){
                node.put("vector", vector.toString());
            }
        }
        if (sparseVector != null && !sparseVector.isEmpty()) {
            node.put("sparse_vector", JsonUtils.toJsonNode(ConvertUtils.convertPairToList(sparseVector)));
        }
        if (score != null) {
            node.put("score", score);
        }
        if (StringUtils.isNotEmpty(doc)) {
            node.put("doc", doc);
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
                    case Json:
                        Map<String, Object> map = JsonUtils.parseObject(field.getValue().toString(), Map.class);
                        JsonNode jsonNode = JsonUtils.toJsonNode(map);
                        node.put(field.getName(), jsonNode);
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
        this.doc = builder.doc;
        this.score = builder.score;
        this.docFields = builder.docFields;
        this.sparseVector = builder.sparseVector;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private Object vector;
        private List<Pair<Long,Float>> sparseVector;
        private Double score;
        private String doc;
        private List<DocField> docFields;

        public Builder() {
            this.docFields = new ArrayList<>();
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withVector(List<? extends Number> vector) {
            this.vector = vector;
            return this;
        }

        public Builder withVectorByEmbeddingText(String embeddingText) {
            this.vector = embeddingText;
            return this;
        }

        public Builder withDoc(String doc) {
            this.doc = doc;
            return this;
        }

        public Builder withScore(Double score) {
            this.score = score;
            return this;
        }

        public Builder withVectorByText(String embeddingText) {
            this.vector = embeddingText;
            return this;
        }

        public Builder withSparseVector(List<Pair<Long,Float>> sparseVector) {
            this.sparseVector = sparseVector;
            return this;
        }

        public Builder withSparseVectorList(List<Object> sparseVectors) {
            List<Pair<Long, Float>> sparseVectorTmp = new ArrayList<>();
            sparseVectors.forEach(sparseVector -> {
                if (sparseVector instanceof List) {
                    List<Object> sparseVectorList = (List<Object>) sparseVector;
                    sparseVectorTmp.add(Pair.of(Long.valueOf(sparseVectorList.get(0).toString()),
                            Float.valueOf(sparseVectorList.get(1).toString())));
                }
            });
            this.sparseVector = sparseVectorTmp;
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