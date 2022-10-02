package com.felix.oauth2server.service;

import com.felix.oauth2server.model.OauthClient;

public interface OauthClientService extends CommonServiceInterface<OauthClient> {
    default OauthClient findByClientId(String clientId) {
        throw new UnsupportedOperationException();
    }
}
