package com.felix.oauth2resource.model.exception;

public class BadGatewayException extends ServerException {
    private static final long serialVersionUID = 8441674147287253651L;

    public BadGatewayException(String message, String errorBody) {
        super(502, message == null ? "502 Bad Gateway" : message, errorBody);
    }

}
