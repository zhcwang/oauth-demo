package com.felix.oauth2resource.model.exception;

public class LengthRequiredException extends ClientException {
    private static final long serialVersionUID = 3661006701496467613L;

    public LengthRequiredException(String message, String errorBody) {
        super(411, message == null ? "411 Length Required" : message, errorBody);
    }

}
