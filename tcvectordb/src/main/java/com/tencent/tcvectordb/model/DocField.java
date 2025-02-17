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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.param.collection.FieldType;
import com.tencent.tcvectordb.utils.JsonUtils;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Doc Field
 */
public class DocField {
    private final String name;
    private final Object value;

    public DocField(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }


    @JsonIgnore
    public String getStringValue() {
        return value.toString();
    }

    @JsonIgnore
    public FieldType getFieldType() {
        if (this.value == null) {
            throw new VectorDBException("DocField value is null, " + "filed name is " + this.name);
        }
        String valueClassName = this.value.getClass().getName();
        if (valueClassName.equals("java.lang.Integer") || valueClassName.equals("java.lang.Long")) {
            return FieldType.Uint64;
        }
        if (this.value instanceof List) {
            return FieldType.Array;
        }
        if (this.value instanceof JSONObject) {
            return FieldType.Json;
        }
        return FieldType.String;
    }

    public static String fillDocFiledsJsonString(ObjectNode node, List<DocField> docFields) {
        if (docFields != null && !docFields.isEmpty()) {
            for (DocField field : docFields) {
                String valueClassName = field.getValue().getClass().getName();
                if (valueClassName.equals("java.lang.Integer") || valueClassName.equals("java.lang.Long")) {
                    node.put(field.getName(), Long.valueOf(field.getStringValue()));
                }else if (valueClassName.equals("java.lang.Float") || valueClassName.equals("java.lang.Double")) {
                    node.put(field.getName(), Double.valueOf(field.getStringValue()));
                }else if (field.getValue() instanceof List){
                    List<String> strValues = (List<String>) ((List) field.getValue());
                    ArrayNode strNode = JsonNodeFactory.instance.arrayNode();
                    strValues.forEach(strNode::add);
                    node.set(field.getName(), strNode);
                } else if (field.getValue() instanceof JSONObject) {
                    Map<String, Object> map = JsonUtils.parseObject(field.getValue().toString(), Map.class);
                    JsonNode jsonNode = JsonUtils.toJsonNode(map);
                    node.put(field.getName(), jsonNode);
                }else {
                    node.put(field.getName(), field.getStringValue());
                }
            }
        }
        return node.toString();
    }
}
