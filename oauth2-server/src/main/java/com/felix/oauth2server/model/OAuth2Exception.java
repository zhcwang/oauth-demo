package com.felix.oauth2server.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class OAuth2Exception extends RuntimeException {
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private String errorCode = "invalid_request";

    public OAuth2Exception(String msg) {
        super(msg);
    }

    public OAuth2Exception(String msg, Throwable t) {
        super(msg, t);
    }

    public OAuth2Exception(String msg, HttpStatus httpStatus, String errorCode) {
        super(msg);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public OAuth2Exception(String msg, Throwable t, HttpStatus httpStatus, String errorCode) {
        super(msg, t);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

}
