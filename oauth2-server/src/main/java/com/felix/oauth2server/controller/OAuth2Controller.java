package com.felix.oauth2server.controller;

import com.felix.oauth2server.config.CachesEnum;
import com.felix.oauth2server.model.GrantType;
import com.felix.oauth2server.model.OauthClient;
import com.felix.oauth2server.model.ScopeDefinition;
import com.felix.oauth2server.service.OauthClientService;
import com.felix.oauth2server.service.ScopeDefinitionService;
import com.felix.oauth2server.token.GranterFactory;
import com.felix.oauth2server.token.TokenGranter;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URISyntaxException;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/oauth")
public class OAuth2Controller {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OauthClientService oauthClientService;

    @Autowired
    private ScopeDefinitionService scopeDefinitionService;

    CacheManager cacheManager;

    private AuthenticationManager authenticationManager;
    private KeyPair keyPair;

    @Autowired
    private GranterFactory factory;

    public OAuth2Controller(AuthenticationManager authenticationManager, KeyPair keyPair, CacheManager cacheManager) {
        this.authenticationManager = authenticationManager;
        this.keyPair = keyPair;
        this.cacheManager = cacheManager;
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> getAccessToken(@RequestParam(value = "client_id", required = false) String client_id,
                                                              @RequestParam(value = "client_secret", required = false) String client_secret,
                                                              @RequestParam(value = "grant_type") GrantType grant_type,
                                                              @RequestParam(value = "scope", required = false) String scope,
                                                              @RequestParam(value = "redirect_uri", required = false) String redirect_uri,
                                                              @RequestParam(value = "refresh_token", required = false) String refresh_token,
                                                              @RequestParam(value = "code", required = false) String code,
                                                              @RequestParam(value = "username", required = false) String username,
                                                              @RequestParam(value = "password", required = false) String password) throws URISyntaxException {
        Map<String, Object> result = new HashMap<>(16);
        OauthClient client = oauthClientService.findByClientId(client_id);
        HttpHeaders headers = new HttpHeaders();

        if (client == null) {
            result.put("status", 0);
            result.put("code", "invalid_client");
            result.put("message", "invalid_client");
            return new ResponseEntity<>(
                result, headers, HttpStatus.OK);
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", client_id);
        parameters.put("client_secret", client_secret);
        parameters.put("grant_type", grant_type.name());
        parameters.put("scope", scope);
        parameters.put("redirect_uri", redirect_uri);
        parameters.put("refresh_token", refresh_token);
        parameters.put("code", code);
        parameters.put("username", username);
        parameters.put("password", password);
        TokenGranter granter = factory.getGranter(grant_type);
        if (granter == null) {
            result.put("status", 0);
            result.put("message", "Unsupported grant type.");
        } else if (GrantType.authorization_code == grant_type) {
            
            if (StringUtils.isEmpty(redirect_uri) || !StringUtils.equalsIgnoreCase(client.getWebServerRedirectUri(), redirect_uri)) {
                result.put("status", 0);
                result.put("code", "invalid_redirect_uri");
                result.put("message", "invalid_redirect_uri");
                return new ResponseEntity<>(
                    result, headers, HttpStatus.OK);
            }
        }
        result = granter.grant(client, parameters);
        return new ResponseEntity<>(
            result, headers, HttpStatus.OK);
    }

    @GetMapping("/authorize")
    public String getAccessToken(ModelMap model,
                                 Authentication authentication,
                                 @RequestHeader(name = "referer", required = false) String referer,
                                 @RequestParam(value = "client_id") String client_id,
                                 @RequestParam(value = "response_type") String response_type,
                                 @RequestParam(value = "state", required = false) String state,
                                 @RequestParam(value = "scope", required = false) String scopes,
                                 @RequestParam(value = "redirect_uri") String redirect_uri) {
        OauthClient client = oauthClientService.findByClientId(client_id);

        if (client == null || !StringUtils.equalsIgnoreCase(client.getWebServerRedirectUri(), redirect_uri)) {
            if (redirect_uri.indexOf("?") > 0) {
                return "redirect:" + redirect_uri + "&error=invalid_client";
            } else {
                return "redirect:" + redirect_uri + "?error=invalid_client";
            }
        }

        if ("1".equals(client.getAutoApprove())) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            cacheManager.getCache(CachesEnum.Oauth2AuthorizationCodeCache.name()).put(uuid, authentication);
            if (client.getWebServerRedirectUri().indexOf("?") > 0) {
                return "redirect:" + client.getWebServerRedirectUri() + "&code=" + uuid + "&state=" + state;
            } else {
                return "redirect:" + client.getWebServerRedirectUri() + "?code=" + uuid + "&state=" + state;
            }
        } else {
            model.put("client_id", client_id);
            model.put("applicationName", client.getApplicationName());
            model.put("from", referer);
            model.put("state", state);
            model.put("redirect_uri", redirect_uri);
            Map<String, String> scopeMap = new LinkedHashMap<>();
            if (StringUtils.isNotEmpty(scopes)){
                for (String scope : scopes.split(",")) {
                    ScopeDefinition scopeDefinition = scopeDefinitionService.findByScope(scope);
                    if (scopeDefinition != null) {
                        scopeMap.put("scope." + scope, scopeDefinition.getDefinition());
                    } else {
                        scopeMap.put("scope." + scope, scope);
                    }
                }
                
            }
            model.put("scopeMap", scopeMap);

            return "accessConfirmation";
        }
    }

    @PostMapping("/authorize")
    public String postAccessToken(ModelMap model,
                                  Authentication authentication,
                                  @RequestParam(name = "referer", required = false) String referer,
                                  @RequestParam(value = "client_id") String client_id,
                                  @RequestParam(value = "response_type", required = false) String response_type,
                                  @RequestParam(value = "state", required = false) String state,
                                  @RequestParam(value = "scope", required = false) String scope,
                                  @RequestParam(value = "user_oauth_approval", required = false, defaultValue = "false") boolean userOauthApproval,
                                  @RequestParam(value = "redirect_uri") String redirect_uri) {
        OauthClient client = oauthClientService.findByClientId(client_id);
        model.put("client_id", client_id);
        model.put("applicationName", client.getApplicationName());
        model.put("from", referer);

        if (userOauthApproval) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            cacheManager.getCache(CachesEnum.Oauth2AuthorizationCodeCache.name()).put(uuid, authentication);
            if (client.getWebServerRedirectUri().indexOf("?") > 0) {
                return "redirect:" + client.getWebServerRedirectUri() + "&code=" + uuid + "&state=" + state;
            } else {
                return "redirect:" + client.getWebServerRedirectUri() + "?code=" + uuid + "&state=" + state;
            }
        } else {
            if (redirect_uri.indexOf("?") > 0) {
                return "redirect:" + redirect_uri + "&state=" + state + "&error=not_approval";
            } else {
                return "redirect:" + redirect_uri + "&state=" + state + "?error=not_approval";
            }
        }
    }

    @ResponseBody
    @PostMapping("/check_token")
    public Map<String, Object> checkToken(@RequestParam(value = "access_token") String access_token) {
        Map<String, Object> result = new HashMap<>(16);
        try {
            Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().parseClaimsJws(access_token).getBody();
            result.put("status", 1);
        } catch (Exception e) {
            result.put("status", 0);
            result.put("message", "Invalidate access_token.");
            if (log.isErrorEnabled()) {
                log.error("Failed to verify token", e);
            }
        }
        return result;
    }
    
}
