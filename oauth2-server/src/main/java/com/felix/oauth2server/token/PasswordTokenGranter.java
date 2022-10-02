package com.felix.oauth2server.token;

import com.felix.oauth2server.model.OAuth2Exception;
import com.felix.oauth2server.model.OauthClient;
import com.felix.oauth2server.model.UserInfo;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

public class PasswordTokenGranter implements TokenGranter {
    private final AuthenticationManager authenticationManager;
    KeyPair keyPair;
    String issuer;

    public PasswordTokenGranter(AuthenticationManager authenticationManager, KeyPair keyPair, String issuer) {
        this.authenticationManager = authenticationManager;
        this.keyPair = keyPair;
        this.issuer = issuer;
    }

    @Override
    public Map<String, Object> grant(OauthClient client, Map<String, String> parameters) {

        Map<String, Object> result = new HashMap<>();
        result.put("status", 0);

        String username = parameters.get("username");
        String password = parameters.get("password");
        String clientId = parameters.get("client_id");
        String scope = parameters.get("scope");

        Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException | BadCredentialsException ase) {
            //covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new OAuth2Exception(ase.getMessage(), HttpStatus.UNAUTHORIZED, "invalid_request");
        } // If the username/password are wrong the spec says we should send 400/invalid grant

        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new OAuth2Exception("Could not authenticate user: " + username, HttpStatus.UNAUTHORIZED, "invalid_request");
        }
        Date now = new Date();
        Date tokenExpiration = Date.from(LocalDateTime.now().plusSeconds(client.getAccessTokenValidity()).atZone(ZoneId.systemDefault()).toInstant());
        Date refreshTokenExpiration = Date.from(LocalDateTime.now().plusSeconds(client.getAccessTokenValidity()).atZone(ZoneId.systemDefault()).toInstant());

        UserInfo userInfo = (UserInfo) userAuth.getPrincipal();
        String tokenId = UUID.randomUUID().toString();
        String accessToken = generateAccessToken(clientId, now, tokenExpiration, userInfo, tokenId);
        String refreshToken = generateSecretToken(clientId, now, refreshTokenExpiration, userInfo, tokenId);

        result.put("access_token", accessToken);
        result.put("token_type", "bearer");
        result.put("refresh_token", refreshToken);
        result.put("expires_in", client.getAccessTokenValidity() - 1);
        result.put("accountOpenCode", userInfo.getAccountOpenCode());
        result.put("scope", scope);
        result.put("jti", tokenId);
        result.put("status", 1);
        return result;
    }

    private String generateSecretToken(String clientId, Date now, Date refreshTokenExpiration, UserInfo userInfo, String tokenId) {
        return Jwts.builder()
            .claim("jti", tokenId)
            .setHeaderParam("alg", TokenConstants.alg)
            .setHeaderParam("typ", TokenConstants.typ)
            .claim("accountOpenCode", userInfo.getAccountOpenCode())
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

    private String generateAccessToken(String clientId, Date now, Date tokenExpiration, UserInfo userInfo, String tokenId) {
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
