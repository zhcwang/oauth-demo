package com.felix.oauth2resource.model.exception;

public class NotImplementedException extends ServerException {
    private static final long serialVersionUID = -5905966579169215779L;

    public NotImplementedException(String message, String errorBody) {
        super(501, message == null ? "501 Not Implemented" : message, errorBody);
    }

}
