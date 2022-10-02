package com.felix.oauth2resource.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public final class JacksonUtils {

    private JacksonUtils() {
    }

    public final static ObjectMapper MAPPER;
    public final static XmlMapper XML_MAPPER;

    static {
        MAPPER = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        XML_MAPPER = new XmlMapper();
    }

    public static String serialize(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Failed to serialize object '%s' to Json String.", obj));
        }
    }

    public static JsonNode objectToJson(Object obj) {
        try {
            return deserialize(MAPPER.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Failed to convert object '%s' to Json Object.", obj));
        }
    }

    public static <T> T deserialize(String jsonText, TypeReference<T> type) {
        try {
            return MAPPER.readValue(jsonText, type);
        } catch (Exception e) {
            throw new RuntimeException(
                String.format("Failed to deserialize String '%s' to Type '%s'.", jsonText, type.toString()));
        }
    }

    public static <T> T deserialize(String jsonText, Class<T> beanClass) {
        try {
            return MAPPER.readValue(jsonText, beanClass);
        } catch (Exception e) {
            throw new RuntimeException(
                String.format("Failed to deserialize String '%s' to Class '%s'.", jsonText, beanClass.getName()),
                e);
        }
    }

    public static JsonNode deserialize(String jsonText) {
        try {
            return MAPPER.readTree(jsonText);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to deserialize String '%s' to Json Node.", jsonText));
        }
    }

    public static <T> T deserializeXml(String xmlText, Class<T> beanClass) {
        try {
            return XML_MAPPER.readValue(xmlText, beanClass);
        } catch (Exception e) {
            throw new RuntimeException(
                String.format("Failed to deserialize XML string '%s' to class '%s'.", xmlText, beanClass.getName()),
                e);
        }
    }

    public static <T> T deserializeXmlWithJaxb(String xmlText, Class<T> beanClass) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(beanClass);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            @SuppressWarnings("unchecked")
            T result = (T) unmarshaller.unmarshal(new StringReader(xmlText));
            return result;
        } catch (Exception e) {
            throw new RuntimeException(
                String.format("Failed to deserialize XML string '%s' to class '%s'.", xmlText, beanClass.getName()),
                e);
        }
    }

}
