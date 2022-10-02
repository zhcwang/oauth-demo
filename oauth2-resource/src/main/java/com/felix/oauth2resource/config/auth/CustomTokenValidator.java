package com.felix.oauth2resource.config.auth;

import com.felix.oauth2resource.model.dto.IDPCheckResult;
import com.felix.oauth2resource.model.dto.Jwks;
import com.felix.oauth2resource.utils.HttpUtils;
import com.felix.oauth2resource.utils.JsonUtil;
import com.felix.oauth2resource.utils.JwtUtils;
import com.felix.oauth2resource.utils.JwtVerifyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CustomTokenValidator implements OAuth2TokenValidator<Jwt> {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    OAuth2Error error = new OAuth2Error("invalid_token", "The required audience is invalid", null);

    private final Map<String, Object> idpMetadata;

    public CustomTokenValidator(String issuerUrl) {
        idpMetadata = JwtUtils.fetchIDPMetadata(issuerUrl);
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (checkTokenValid(jwt)) {
            return OAuth2TokenValidatorResult.success();
        } else {
            return OAuth2TokenValidatorResult.failure(error);
        }
    }

    boolean checkTokenValid(Jwt jwt) {
        log.debug("OAuth2TokenValidator: checking token 【" + jwt.getTokenValue() + "】 is valid...");
        // approach 1: checking by IDP
        String checkEndpoint = (String) idpMetadata.get("check_token");
        checkEndpoint = StringUtils.removeEnd(checkEndpoint, "/") + "?access_token=" + jwt.getTokenValue();
        String response = HttpUtils.post(checkEndpoint);
        try {
            IDPCheckResult idpCheckResult = JsonUtil.jsonStringToObject(response, IDPCheckResult.class);
            if (idpCheckResult.getStatus() == 1) {
                return true;
            }
        } catch (IOException e) {
            // TODO: ignore 
        }

        // approach 2: get parameters back and validate it locally
        String publicKeyEndPoint = (String) idpMetadata.get("jwks_uri");
        response = HttpUtils.get(publicKeyEndPoint);
        try {
            Jwks jwks = JsonUtil.jsonStringToObject(response, Jwks.class);
            List<Jwks.Key> keys = jwks.getKeys();
            for (Jwks.Key key : keys) {
                if ("RSA".equals(key.getKty())) {
                    try {
                        JwtVerifyUtils.verifyRSA(key.getContent(), jwt.getTokenValue());
                        return true;
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        } catch (IOException e) {
            return false;
        }

        return false;
    }


}
