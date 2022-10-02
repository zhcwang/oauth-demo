package com.felix.oauth2resource.model.exception;

public class ForbiddenException extends ClientException {
    private static final long serialVersionUID = 2856420027532416276L;

    public ForbiddenException(String message, String errorBody) {
        super(403, message == null ? "403 Forbidden" : message, errorBody);
    }

}
