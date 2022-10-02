package com.felix.oauth2server.model;

import lombok.Data;

@Data
public class ScopeDefinition extends BaseDomain {

    private static final long serialVersionUID = 2862177859444895431L;
    private String scope;
    private String definition;
}
