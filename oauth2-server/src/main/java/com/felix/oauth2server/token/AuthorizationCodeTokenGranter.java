package com.felix.oauth2server.token;

import com.felix.oauth2server.config.CachesEnum;
import com.felix.oauth2server.model.OAuth2Exception;
import com.felix.oauth2server.model.OauthClient;
import com.felix.oauth2server.model.UserInfo;
import io.jsonwebtoken.Jwts;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.KeyPair;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class AuthorizationCodeTokenGranter implements TokenGranter {
    private final AuthenticationManager authenticationManager;
    KeyPair keyPair;
    String issuer;
    CacheManager cacheManager;

    public AuthorizationCodeTokenGranter(AuthenticationManager authenticationManager, CacheManager cacheManager, KeyPair keyPair, String issuer) {
        this.authenticationManager = authenticationManager;
        this.cacheManager = cacheManager;
        this.keyPair = keyPair;
        this.issuer = issuer;
    }

    @Override
    public Map<String, Object> grant(OauthClient client, Map<String, String> parameters) {

        Map<String, Object> result = new HashMap<>();
        result.put("status", 0);

        String authorizationCode = parameters.get("code");
        String redirectUri = parameters.get("redirect_uri");
        String clientId = parameters.get("client_id");
        String scope = parameters.get("scope");
        if (authorizationCode == null) {
            throw new OAuth2Exception("An authorization code must be supplied.", HttpStatus.BAD_REQUEST, "invalid_request");
        }
        Cache cache = cacheManager.getCache(CachesEnum.Oauth2AuthorizationCodeCache.name());
        if (cache == null) {
            throw new OAuth2Exception("Invalidate authorization code.", HttpStatus.BAD_REQUEST, "invalid_request");

        }
        Cache.ValueWrapper storedCode = cache.get(authorizationCode);
        if (storedCode != null) {
            Authentication userAuth = (Authentication) (storedCode.get());
            assert userAuth != null;
            UserInfo userInfo = (UserInfo) userAuth.getPrincipal();

            Date now = new Date();
            Date tokenExpiration = Date.from(LocalDateTime.now().plusSeconds(client.getAccessTokenValidity()).atZone(ZoneId.systemDefault()).toInstant());
            Date refreshTokenExpiration = Date.from(LocalDateTime.now().plusSeconds(client.getRefreshTokenValidity()).atZone(ZoneId.systemDefault()).toInstant());


            String tokenId = UUID.randomUUID().toString();
            String accessToken = generteAccessToken(clientId, userInfo, now, tokenExpiration, tokenId);

            String refreshToken = generateRefreshToken(clientId, userInfo, now, refreshTokenExpiration, tokenId);
            cache.evict(authorizationCode);

            result.put("access_token", accessToken);
            result.put("token_type", "bearer");
            result.put("refresh_token", refreshToken);
            result.put("expires_in", client.getAccessTokenValidity() - 1);
            result.put("accountOpenCode", userInfo.getAccountOpenCode());
            result.put("scope", scope);
            result.put("jti", tokenId);
            result.put("status", 1);
            return result;
        } else {
            throw new OAuth2Exception("An authorization code must be supplied.", HttpStatus.BAD_REQUEST, "invalid_request");
        }
    }

    private String generateRefreshToken(String clientId, UserInfo userInfo, Date now, Date refreshTokenExpiration, String tokenId) {
        return Jwts.builder()
            .setHeaderParam("alg", TokenConstants.alg)
            .setHeaderParam("typ", TokenConstants.typ)
            .claim("accountOpenCode", userInfo.getAccountOpenCode())
            .claim("jti", tokenId)
            .setIssuer(issuer)
            .setSubject(userInfo.getUsername())
            .setAudience(clientId)
            .claim("roles", userInfo.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
            .setExpiration(refreshTokenExpiration)
            .setNotBefore(now)
            .setIssuedAt(now)
            .setId(UUID.randomUUID().toString())
            .signWith(keyPair.getPrivate())
            .compact();
    }

    private String generteAccessToken(String clientId, UserInfo userInfo, Date now, Date tokenExpiration, String tokenId) {
        return Jwts.builder()
            .setHeaderParam("alg", TokenConstants.alg)
            .setHeaderParam("typ", TokenConstants.typ)
            .claim("accountOpenCode", userInfo.getAccountOpenCode())
            .setIssuer(issuer)
            .setSubject(userInfo.getUsername())
            .setAudience(clientId)
            .claim("roles", userInfo.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
            .setExpiration(tokenExpiration)
            .setNotBefore(now)
            .setIssuedAt(now)
            .setId(tokenId)
            .signWith(keyPair.getPrivate())
            .compact();
    }
}
