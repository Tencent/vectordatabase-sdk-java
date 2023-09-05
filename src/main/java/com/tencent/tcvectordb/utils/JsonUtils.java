package com.tencent.tcvectordb.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.tcvectordb.exception.VectorDBException;

public class JsonUtils {
    private JsonUtils() {
    }


    private static final ObjectMapper DESERIALIZE_IGNORE_KEY_MAPPER = new ObjectMapper();
    private static final ObjectMapper SERIALIZE_MAPPER = new ObjectMapper();


    static {
        DESERIALIZE_IGNORE_KEY_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * deserialize string to object, it will ignore key when object doesn't have field
     *
     * @param jsonStr {@link String} of json format
     * @param clz     {@link Class} of <T>
     * @param <T>     <T>
     * @return return {@link T}
     */
    public static <T> T parseObject(String jsonStr, Class<T> clz) {
        try {
            return DESERIALIZE_IGNORE_KEY_MAPPER.readValue(jsonStr, clz);
        } catch (JsonProcessingException e) {
            throw new VectorDBException(String.format(
                    "VectorDBServer response error: can't parse collection=%s", jsonStr));
        }
    }

    /**
     * @param obj value for serialize to json string
     * @param <T> <T>
     * @return return type is {@link String}
     */
    public static <T> String toJsonString(T obj) {
        try {
            return SERIALIZE_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new VectorDBException(String.format(
                    "VectorDBServer response error: can't parse collection=%s", obj));
        }
    }

    /**
     * convert java object to JsonNode
     *
     * @param obj java object
     * @return {@link JsonNode}
     */
    public static JsonNode toJsonNode(Object obj) {
        return SERIALIZE_MAPPER.valueToTree(obj);
    }

    /**
     * parse json string to JsonNode
     *
     * @param jsonStr json string
     * @return {@link JsonNode}
     */
    public static JsonNode parseToJsonNode(String jsonStr) {
        try {
            return SERIALIZE_MAPPER.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            throw new VectorDBException(String.format(
                    "VectorDBServer response error: can't parse collection=%s", jsonStr));
        }
    }
}
