package com.felix.oauth2resource.model.exception;

public class HttpVersionNotSupportedException extends ServerException {
    private static final long serialVersionUID = 4628190628234147681L;

    public HttpVersionNotSupportedException(String message, String errorBody) {
        super(505, message == null ? "505 HTTP Version Not Supported" : message, errorBody);
    }

}
