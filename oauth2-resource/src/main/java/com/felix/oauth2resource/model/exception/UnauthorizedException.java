package com.felix.oauth2resource.model.exception;

public class UnauthorizedException extends ClientException {
    private static final long serialVersionUID = -892581874265862725L;

    UnauthorizedException(String message, String errorBody) {
        super(401, message == null ? "401 Unauthorized" : message, errorBody);
    }

}
