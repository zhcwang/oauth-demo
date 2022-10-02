package com.felix.oauth2resource.model.exception;

public class GoneException extends ClientException {
    private static final long serialVersionUID = 6709733017376102345L;

    public GoneException(String message, String errorBody) {
        super(410, message == null ? "410 Gone" : message, errorBody);
    }

}
