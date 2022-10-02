package com.felix.oauth2resource.model.exception;

public class NotAcceptableException extends ClientException {
    private static final long serialVersionUID = 1344399743535193659L;

    public NotAcceptableException(String message, String errorBody) {
        super(406, message == null ? "406 Not Acceptable" : message, errorBody);
    }

}
