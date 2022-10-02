package com.felix.oauth2server.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        ///mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        JavaTimeModule timeModule = new JavaTimeModule();

        timeModule.addDeserializer(LocalDate.class,
            new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        timeModule.addDeserializer(LocalDateTime.class,
            new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        timeModule.addSerializer(LocalDate.class,
            new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        timeModule.addSerializer(LocalDateTime.class,
            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        mapper.registerModule(timeModule);
    }

    public static String objectToJsonString(Object object) throws JsonProcessingException {
        //Object to JSON in String
        return mapper.writeValueAsString(object);
    }

    public static String multiValueMapToJsonString(Map<String, String[]> object) throws JsonProcessingException {
        Map<String, String> newMap = new HashMap<>(16);
        if (object != null && object.size() > 0) {
            object.forEach((k, v) -> {
                if (v != null && v.length > 0) {
                    newMap.put(k, Arrays.toString(v));
                }
            });
        }
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //Object to JSON in String
        return mapper.writeValueAsString(newMap);
    }

    public static <T> T jsonStringToObject(String jsonString, Class<T> t) throws IOException {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        //JSON from String to Object
        return mapper.readValue(jsonString, t);
    }

    public static <T> T jsonStringToObject(String str, TypeReference<T> typeReference) throws IOException {
        return mapper.readValue(str, typeReference);
    }

    public static <T> T jsonStringToObject(String str, Class<?> collectionClass, Class<?>... elementClasses) throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        return mapper.readValue(str, javaType);
    }
}
