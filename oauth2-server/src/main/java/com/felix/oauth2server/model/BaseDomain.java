
package com.felix.oauth2server.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BaseDomain implements Serializable {

    private String id;
    private LocalDateTime dateCreated;
    private LocalDateTime lastModified;
    private int recordStatus;
    private int sortPriority;
    private int version;
    private String remarks;
    private String additionalData;
}
