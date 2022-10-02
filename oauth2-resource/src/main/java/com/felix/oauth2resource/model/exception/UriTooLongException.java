package com.felix.oauth2resource.model.exception;

public class UriTooLongException extends ClientException {
    private static final long serialVersionUID = -4842160051116598636L;

    public UriTooLongException(String message, String errorBody) {
        super(414, message == null ? "414 URI Too Long" : message, errorBody);
    }

}
