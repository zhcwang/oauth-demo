package com.felix.oauth2resource.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class Jwks {
    private List<Key> keys;

    @Data
    public static class Key {
        private String kty;
        private String e;
        private String n;
        private String content;
    }

}
