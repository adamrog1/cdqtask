package com.example.demo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode parseStringToJson(String json) {
        if (json == null) return null;

        try {
            return mapper.readTree(json);
        } catch (JsonProcessingException e) {
            LoggerFactory.getLogger(JsonUtils.class)
                    .warn("Could not parse JSON: {}", json, e);
            return null;
        }
    }

    public static String safeStringify(Object object) {
        if (object == null) return null;
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LoggerFactory.getLogger(JsonUtils.class)
                    .warn("Could not stringify JSON to string: {}", object, e);
            return null;
        }
    }
}
