
package com.felix.oauth2server.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = 613307369635832439L;
    private Long id;
    private int status;
    private String error;
    private String message;
    private String path;
    private Long timestamp;
    private T data;
    private Long total;
    private Integer ack;
    private Object additional;
}
