package com.felix.oauth2resource.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
public class SecurityUtils {

    public static String getOwner() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();

        if (principal == null) {
            log.warn(
                "Found no principal in authentication implementation " + authentication.getClass().getName() + ".");
            return null;
        }
        // the principal should be normally a User object, but in unit test environments or in flowable, this can be
        // other implementations
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            return ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else if (principal instanceof Jwt) {
            Map<String, Object> claims = ((Jwt) principal).getClaims();
            return String.valueOf(claims.get("sub"));
        } else {
            // avoid class import of flowable security api, if we import the IDM API, then
            // we should remove this reflection code
            // org.flowable.idm.api.User interface uses getId()
            Method method = ReflectionUtils.findMethod(principal.getClass(), "getId");
            if (method != null && method.getReturnType().equals(String.class)) {
                String id;
                try {
                    id = (String) method.invoke(principal);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    log.error("Error invoking getId() method on principal.", e);
                    return null;
                }
                return id;
            }
            log.error("Error getting username, unsupported principal implementation " + principal.getClass().getName()
                + ".");
            return null;
        }
    }


    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
