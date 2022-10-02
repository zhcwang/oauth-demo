package com.felix.oauth2server.service;

import com.felix.oauth2server.model.ScopeDefinition;

public interface ScopeDefinitionService extends CommonServiceInterface<ScopeDefinition> {
    default ScopeDefinition findByScope(String scope) {
        throw new UnsupportedOperationException();
    }
}
