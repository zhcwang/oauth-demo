package com.felix.oauth2resource.model.exception;

public class UnsupportedMediaTypeException extends ClientException {
    private static final long serialVersionUID = -8920526339910175663L;

    public UnsupportedMediaTypeException(String message, String errorBody) {
        super(415, message == null ? "415 Unsupproted Media Type" : message, errorBody);
    }

}
