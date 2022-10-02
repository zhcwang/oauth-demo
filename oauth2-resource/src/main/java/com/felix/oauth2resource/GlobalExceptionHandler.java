package com.felix.oauth2resource;

import com.felix.oauth2resource.model.ErrorMsg;
import com.felix.oauth2resource.model.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ResponseEntity<ErrorMsg> handleBadRequestException(BadRequestException e) {
        String stack = getStackTrace(e);
        ErrorMsg err = new ErrorMsg(e.getCode(), e.getMessage(), stack);
        return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
    }

    // for spring validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorMsg> handleIllegalMethodArgumentException(MethodArgumentNotValidException ex) {
        String stack = getStackTrace(ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        String errorMessage = String.join("\n", errors.values());
        ErrorMsg err = new ErrorMsg(400, errorMessage, stack);
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    // TODO: handle other known exception
    

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorMsg> handleException(Exception e) {
        String stack = getStackTrace(e);
        ErrorMsg err = new ErrorMsg(500, e.getMessage(), stack);
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private String getStackTrace(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
