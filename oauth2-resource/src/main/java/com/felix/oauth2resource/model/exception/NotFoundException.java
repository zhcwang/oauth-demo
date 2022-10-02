package com.felix.oauth2resource.model.exception;

public class NotFoundException extends ClientException {
    private static final long serialVersionUID = 328973546877493422L;

    public NotFoundException(String message, String errorBody) {
        super(404, message == null ? "404 Not Found" : message, errorBody);
    }

}
