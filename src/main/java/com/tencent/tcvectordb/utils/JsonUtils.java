package com.tencent.tcvectordb.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tencent.tcvectordb.exception.ParamException;
import com.tencent.tcvectordb.model.param.collection.Embedding;
import com.tencent.tcvectordb.model.param.collection.ParamsSerializer;
import com.tencent.tcvectordb.serializer.EmbeddingDeserialize;
import com.tencent.tcvectordb.serializer.ParamsDeserialize;

import java.text.SimpleDateFormat;

public class JsonUtils {
    private JsonUtils() {
    }

    private static final String DATE_FORMAT_STR_ISO8601_CH = "yyyy-MM-dd HH:mm:ss";
    private static final ObjectMapper DESERIALIZE_IGNORE_KEY_MAPPER = new ObjectMapper();
    private static final ObjectMapper PARAMS_DESERIALIZE_MAPPER = new ObjectMapper();
    private static final ObjectMapper SERIALIZE_MAPPER = new ObjectMapper();


    static {
        DESERIALIZE_IGNORE_KEY_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DESERIALIZE_IGNORE_KEY_MAPPER.setDateFormat(new SimpleDateFormat(DATE_FORMAT_STR_ISO8601_CH));
        PARAMS_DESERIALIZE_MAPPER.setDateFormat(new SimpleDateFormat(DATE_FORMAT_STR_ISO8601_CH));

        SimpleModule module = new SimpleModule();
        module.addDeserializer(ParamsSerializer.class, new ParamsDeserialize());
        module.addDeserializer(Embedding.class, new EmbeddingDeserialize());
        PARAMS_DESERIALIZE_MAPPER.registerModule(module);
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
            throw new ParamException(String.format(
                    "can't parse content=%s", jsonStr));
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
            throw new ParamException(String.format(
                    "can't serialize value=%s, type=%s", obj, obj.getClass().getName()));
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
            throw new ParamException(String.format(
                    "can't parse content=%s", jsonStr));
        }
    }

    /**
     * @param jsonStr json {@link String}
     * @param clz     {@link TypeReference}
     * @param <T>     result type
     * @return {@link T}
     */
    public static <T> T collectionDeserializer(String jsonStr, TypeReference<T> clz) {
        try {
            return PARAMS_DESERIALIZE_MAPPER.readValue(jsonStr, clz);
        } catch (JsonProcessingException e) {
            System.out.println(e);
            throw new ParamException(String.format(
                    "can't parse content=%s", jsonStr));
        }
    }
}
