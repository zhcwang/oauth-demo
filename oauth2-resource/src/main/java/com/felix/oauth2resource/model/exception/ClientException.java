package com.felix.oauth2resource.model.exception;

public class ClientException extends WebserviceException {
    private static final long serialVersionUID = 1924224632726923407L;

    public static ClientException of(int code) {
        return of(code, null, null);
    }

    public static ClientException of(int code, String message) {
        return of(code, message, null);
    }

    public static ClientException of(int code, String message, String errorBody) {
        if (code < 400 || code > 499)
            throw new IllegalArgumentException("Code " + code + " is not a client error.");
        return (ClientException) WebserviceException.of(code, message, errorBody);
    }

    protected ClientException(int code, String message) {
        this(code, message, null);
    }

    protected ClientException(int code, String message, String errorBody) {
        super(code, message, errorBody);
    }

}
