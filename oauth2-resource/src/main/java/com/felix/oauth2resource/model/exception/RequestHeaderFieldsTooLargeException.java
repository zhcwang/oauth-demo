package com.felix.oauth2resource.model.exception;

public class RequestHeaderFieldsTooLargeException extends ClientException {
    private static final long serialVersionUID = 2822147696860679153L;

    public RequestHeaderFieldsTooLargeException(String message, String errorBody) {
        super(431, message == null ? "431 Request Header Fields Too Large" : message, errorBody);
    }

}
