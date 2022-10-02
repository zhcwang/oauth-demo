package com.felix.oauth2resource.model.exception;

public class ServiceUnavailableException extends ServerException {
    private static final long serialVersionUID = -4381917469088616719L;

    public ServiceUnavailableException(String message, String errorBody) {
        super(503, message == null ? "503 Service Unavailable" : message, errorBody);
    }

}
