package com.felix.oauth2resource.model.exception;

public class PayloadTooLargeException extends ClientException {
    private static final long serialVersionUID = 4035651413398506953L;

    public PayloadTooLargeException(String message, String errorBody) {
        super(413, message == null ? "413 Payload Too Large" : message, errorBody);
    }

}
