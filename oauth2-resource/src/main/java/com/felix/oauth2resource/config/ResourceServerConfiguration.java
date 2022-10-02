package com.felix.oauth2resource.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felix.oauth2resource.config.auth.CustomAccessDeniedHandler;
import com.felix.oauth2resource.config.auth.CustomTokenResolver;
import com.felix.oauth2resource.config.auth.CustomTokenValidator;
import com.felix.oauth2resource.config.auth.GrantedAuthoritiesConverter;
import com.felix.oauth2resource.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

/**
 * Resource Server (default) will attempt to coerce these scopes into a list of granted authorities,
 * prefixing each scope with the string "SCOPE_".
 * To use own custom attribute, may need to adapt the attribute or a composition of attributes into internalized authorities.
 * implements Converter<Jwt, AbstractAuthenticationToken>,or extends JwtAuthenticationConverter;
 * https://docs.spring.io/spring-security/site/docs/current/reference/html5/#oauth2resourceserver
 */
@Configuration
public class ResourceServerConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Autowired
    CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .cors()
            .and()
            .csrf().disable()
            .authorizeRequests()
            .mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .mvcMatchers("/cat/list").hasAnyAuthority("ROLE_ABC", "ROLE_USER")
//            .mvcMatchers("/v2/api-docs").permitAll()
//            .mvcMatchers("/webjars/**").permitAll()
//            .mvcMatchers("/swagger-resources/**").permitAll()
//            .withObjectPostProcessor(new MyObjectPostProcessor())
//            .mvcMatchers("/coupon/**").hasAnyAuthority("ROLE_SUPER")
//            .mvcMatchers("/product/**").hasAnyAuthority("ROLE_USER", "ROLE_SUPER")
            .anyRequest().authenticated()
            .and()
            .oauth2ResourceServer()
            .accessDeniedHandler(customAccessDeniedHandler)
            .bearerTokenResolver(new CustomTokenResolver())
            .jwt().jwtAuthenticationConverter(jwtAuthenticationConverter()).decoder(jwtDecoder());
    }


    Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        GrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new GrantedAuthoritiesConverter(objectMapper);
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @SuppressWarnings("unchecked")
    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(issuerUri);
        OAuth2TokenValidator<Jwt> customTokenValidator = new CustomTokenValidator(issuerUri);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> delegatingOAuth2TokenValidator = new DelegatingOAuth2TokenValidator<>(withIssuer, customTokenValidator);
        jwtDecoder.setJwtValidator(delegatingOAuth2TokenValidator);
        return jwtDecoder;
    }


}
