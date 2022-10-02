package com.felix.oauth2resource.model.exception;

public class TooManyRequestsException extends ClientException {
    private static final long serialVersionUID = 5080772441309854145L;

    public TooManyRequestsException(String message, String errorBody) {
        super(429, message == null ? "429 Too Many Requests" : message, errorBody);
    }

}
