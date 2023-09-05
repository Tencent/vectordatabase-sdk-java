package com.tencent.tcvectordb.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {

    private JSONUtil() {
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJSONString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
