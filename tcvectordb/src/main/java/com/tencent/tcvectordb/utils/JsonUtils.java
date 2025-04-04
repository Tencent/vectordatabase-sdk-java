package com.tencent.tcvectordb.utils;
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
    private static final ObjectMapper SERIALIZE_MAPPER = new ObjectMapper();


    static {
        DESERIALIZE_IGNORE_KEY_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DESERIALIZE_IGNORE_KEY_MAPPER.setDateFormat(new SimpleDateFormat(DATE_FORMAT_STR_ISO8601_CH));

        SimpleModule module = new SimpleModule();
        module.addDeserializer(ParamsSerializer.class, new ParamsDeserialize());
        module.addDeserializer(Embedding.class, new EmbeddingDeserialize());
        DESERIALIZE_IGNORE_KEY_MAPPER.registerModule(module);
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
                    "can't parse content=%s", jsonStr), e);
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
                    "can't serialize value=%s, type=%s", obj, obj.getClass().getName()), e);
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
                    "can't parse content=%s", jsonStr), e);
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
            return DESERIALIZE_IGNORE_KEY_MAPPER.readValue(jsonStr, clz);
        } catch (JsonProcessingException e) {
            throw new ParamException(String.format(
                    "can't parse content=%s", jsonStr), e);
        }
    }
}
