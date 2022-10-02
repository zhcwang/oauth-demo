package com.felix.oauth2resource.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ErrorMsg implements Serializable {
    private static final long serialVersionUID = 9023908484166509245L;

    private int statusCode;
    private String title;
    private String description;

    public ErrorMsg(int statusCode, String title, String description) {
        this.statusCode = statusCode;
        this.title = title;
        this.description = description;
    }
}
