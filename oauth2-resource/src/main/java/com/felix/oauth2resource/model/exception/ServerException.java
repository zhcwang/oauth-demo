package com.felix.oauth2resource.model.exception;

public class ServerException extends WebserviceException {
    private static final long serialVersionUID = 1924224632726923407L;

    public static ServerException of(int code) {
        return of(code, null, null);
    }

    public static ServerException of(int code, String message) {
        return of(code, message, null);
    }

    public static ServerException of(int code, String message, String errorBody) {
        if (code < 500 || code > 599)
            throw new IllegalArgumentException("Code " + code + " is not a server error.");
        return (ServerException) WebserviceException.of(code, message, errorBody);
    }

    protected ServerException(int code, String message) {
        this(code, message, null);
    }

    protected ServerException(int code, String message, String errorBody) {
        super(code, message, errorBody);
    }
}
