package com.felix.oauth2resource.model.exception;

public class MethodNotAllowedException extends ClientException {
    private static final long serialVersionUID = 4786156127981778847L;

    public MethodNotAllowedException(String message, String errorBody) {
        super(405, message == null ? "405 Method Not Allowed" : message, errorBody);
    }

}
