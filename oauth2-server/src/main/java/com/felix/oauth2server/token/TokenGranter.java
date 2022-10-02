package com.felix.oauth2server.token;

import com.felix.oauth2server.model.GrantType;
import com.felix.oauth2server.model.OauthClient;

import java.util.Map;

public interface TokenGranter {

    Map<String, Object> grant(OauthClient client, Map<String, String> parameters);

    default void validateGrantType(OauthClient client, GrantType grantType) {
        
    }
}
