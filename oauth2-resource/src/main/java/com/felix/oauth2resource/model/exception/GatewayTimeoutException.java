package com.felix.oauth2resource.model.exception;

public class GatewayTimeoutException extends ServerException {
    private static final long serialVersionUID = 6670710682924636984L;

    public GatewayTimeoutException(String message, String errorBody) {
        super(504, message == null ? "504 Gateway Timeout" : message, errorBody);
    }

}
