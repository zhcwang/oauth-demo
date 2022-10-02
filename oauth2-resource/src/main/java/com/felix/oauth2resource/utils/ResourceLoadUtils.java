package com.felix.oauth2resource.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ResourceLoadUtils {

    public static <T> T loadResource(String path, TypeReference<T> ref) {
        try {
            Resource resource = new ClassPathResource(path);
            byte[] bdata = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return JacksonUtils.deserialize(new String(bdata, StandardCharsets.UTF_8), ref);
        } catch (IOException e) {
            throw new RuntimeException("Fail to load " + path + ".", e);
        }
    }

    public static byte[] loadResourceAsBytes(String path) {
        try {
            Resource resource = new ClassPathResource(path);
            return FileCopyUtils.copyToByteArray(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Fail to load " + path + ".", e);
        }
    }

    public static String loadResourceToString(String path) {
        try {
            Resource resource = new ClassPathResource(path);
            byte[] bdata = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return new String(bdata, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Fail to load " + path + ".", e);
        }
    }
}
