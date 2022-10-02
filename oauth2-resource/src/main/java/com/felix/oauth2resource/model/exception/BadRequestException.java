package com.felix.oauth2resource.model.exception;

public class BadRequestException extends ClientException {
    private static final long serialVersionUID = 7999905020883958286L;

    public BadRequestException(String message, String errorBody) {
        super(400, message == null ? "400 Bad Request" : message, errorBody);
    }

}
