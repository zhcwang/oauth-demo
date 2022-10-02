package com.felix.oauth2resource.model.exception;

public class ConflictException extends ClientException {
    private static final long serialVersionUID = -892581874265862725L;

    public ConflictException(String message, String errorBody) {
        super(409, message == null ? "409 Conflict" : message, errorBody);
    }

}
