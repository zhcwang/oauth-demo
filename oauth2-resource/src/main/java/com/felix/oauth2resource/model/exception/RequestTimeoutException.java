package com.felix.oauth2resource.model.exception;

public class RequestTimeoutException extends ClientException {
    private static final long serialVersionUID = 549012264797790854L;

    public RequestTimeoutException(String message, String errorBody) {
        super(408, message == null ? "408 Request Timeout" : message, errorBody);
    }

}
