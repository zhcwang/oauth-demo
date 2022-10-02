package com.felix.oauth2server.token;

import com.felix.oauth2server.model.GrantType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

import java.security.KeyPair;

@Component
public class GranterFactory {

    private PasswordTokenGranter passwordTokenGranter;
    private RefreshTokenGranter refreshTokenGranter;
    private AuthorizationCodeTokenGranter authorizationCodeTokenGranter;


    public GranterFactory(AuthenticationManager authenticationManager, KeyPair keyPair, CacheManager cacheManager, @Value("${oauth2.issuer-uri:http://localhost:10380}") String issuerUri) {
        passwordTokenGranter = new PasswordTokenGranter(authenticationManager, keyPair, issuerUri);
        refreshTokenGranter = new RefreshTokenGranter(authenticationManager, keyPair, issuerUri);
        authorizationCodeTokenGranter = new AuthorizationCodeTokenGranter(authenticationManager, cacheManager, keyPair, issuerUri);
    }


    public TokenGranter getGranter(GrantType grantType) {
        switch (grantType) {
            case authorization_code:
                return authorizationCodeTokenGranter;
            case password:
                return passwordTokenGranter;
            case refresh_token:
                return refreshTokenGranter;
            default:
                return null;
        }
    }


}
