package com.felix.oauth2resource.config.auth;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felix.oauth2resource.utils.ClientIpUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response, AccessDeniedException e) throws IOException {

        String toUrl = ClientIpUtils.getFullRequestUrl(request);
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        Map<String, Object> responseMessage = new HashMap<>(16);
        responseMessage.put("status", HttpStatus.FORBIDDEN.value());
        responseMessage.put("message", "No sufficient permissions.");
        responseMessage.put("path", toUrl);
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(response.getOutputStream(),
            JsonEncoding.UTF8);
        objectMapper.writeValue(jsonGenerator, responseMessage);
    }
}
