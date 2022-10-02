package com.felix.oauth2resource.utils;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

public class JwtUtils {

    public static Map<String, Object> fetchIDPMetadata(String issuer) {
        URI uri = URI.create(issuer);
        return getConfiguration(issuer, oidc(uri), oidcRfc8414(uri), oauth(uri));
    }


    private static final String OIDC_METADATA_PATH = "/.well-known/openid-configuration";
    private static final String OAUTH_METADATA_PATH = "/.well-known/oauth-authorization-server";
    private static final RestTemplate rest = new RestTemplate();
    private static final ParameterizedTypeReference<Map<String, Object>> STRING_OBJECT_MAP = new ParameterizedTypeReference<Map<String, Object>>() {
    };
    
    static Map<String, Object> getConfigurationForOidcIssuerLocation(String oidcIssuerLocation) {
        return getConfiguration(oidcIssuerLocation, oidc(URI.create(oidcIssuerLocation)));
    }

    static Map<String, Object> getConfigurationForIssuerLocation(String issuer) {
        URI uri = URI.create(issuer);
        return getConfiguration(issuer, oidc(uri), oidcRfc8414(uri), oauth(uri));
    }

    static void validateIssuer(Map<String, Object> configuration, String issuer) {
        String metadataIssuer = getMetadataIssuer(configuration);
        Assert.state(issuer.equals(metadataIssuer), () -> {
            return "The Issuer \"" + metadataIssuer + "\" provided in the configuration did not match the requested issuer \"" + issuer + "\"";
        });
    }

    private static String getMetadataIssuer(Map<String, Object> configuration) {
        return configuration.containsKey("issuer") ? configuration.get("issuer").toString() : "(unavailable)";
    }

    private static Map<String, Object> getConfiguration(String issuer, URI... uris) {
        String errorMessage = "Unable to resolve the Configuration with the provided Issuer of \"" + issuer + "\"";
        URI[] var3 = uris;
        int var4 = uris.length;
        int var5 = 0;

        while (true) {
            if (var5 < var4) {
                URI uri = var3[var5];

                try {
                    RequestEntity<Void> request = RequestEntity.get(uri).build();
                    ResponseEntity<Map<String, Object>> response = rest.exchange(request, STRING_OBJECT_MAP);
                    Map<String, Object> configuration = (Map) response.getBody();
                    Assert.isTrue(configuration.get("jwks_uri") != null, "The public JWK set URI must not be null");
                    return configuration;
                } catch (IllegalArgumentException var10) {
                    throw var10;
                } catch (RuntimeException var11) {
                    if (var11 instanceof HttpClientErrorException && ((HttpClientErrorException) var11).getStatusCode().is4xxClientError()) {
                        ++var5;
                        continue;
                    }

                    throw new IllegalArgumentException(errorMessage, var11);
                }
            }

            throw new IllegalArgumentException(errorMessage);
        }
    }

    private static URI oidc(URI issuer) {
        return UriComponentsBuilder.fromUri(issuer).replacePath(issuer.getPath() + "/.well-known/openid-configuration").build(Collections.emptyMap());
    }

    private static URI oidcRfc8414(URI issuer) {
        return UriComponentsBuilder.fromUri(issuer).replacePath("/.well-known/openid-configuration" + issuer.getPath()).build(Collections.emptyMap());
    }

    private static URI oauth(URI issuer) {
        return UriComponentsBuilder.fromUri(issuer).replacePath("/.well-known/oauth-authorization-server" + issuer.getPath()).build(Collections.emptyMap());
    }
}
