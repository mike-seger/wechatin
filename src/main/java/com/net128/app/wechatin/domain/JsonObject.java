package com.net128.app.wechatin.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public interface JsonObject {
    ObjectMapper om=objectMapper();

    static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    default String toJson() {
        try {
            return om.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }
    default String toJsonPretty() {
        try {
            return om.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }


    default <T> T fromJson(String xml) {
        try {
            return om.readerForUpdating(this).readValue(xml);
        } catch (IOException e) {
            throw new RuntimeException("Error mapping XML to object", e);
        }
    }

}
