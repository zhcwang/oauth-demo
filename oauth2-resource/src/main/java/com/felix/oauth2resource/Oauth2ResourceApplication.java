package com.felix.oauth2resource;

import okhttp3.OkHttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;
import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class Oauth2ResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ResourceApplication.class, args);
    }

    /**
     * Work together with jpa audit to auto set created/modifiedBy fields from tokens.
     *
     * @return
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof Jwt) {
                    Map<String, Object> claims = ((Jwt) principal).getClaims();
                    return Optional.ofNullable(String.valueOf(claims.get("sub")));
                } else {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        };
    }


    @Bean
    @Lazy
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder().build();
    }

}
