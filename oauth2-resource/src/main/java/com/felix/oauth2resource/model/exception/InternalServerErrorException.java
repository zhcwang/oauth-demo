package com.felix.oauth2resource.model.exception;

public class InternalServerErrorException extends ServerException {
    private static final long serialVersionUID = 9135182317345837646L;

    public InternalServerErrorException(String message, String errorBody) {
        super(500, message == null ? "500 Internal Server Error" : message, errorBody);
    }

}
