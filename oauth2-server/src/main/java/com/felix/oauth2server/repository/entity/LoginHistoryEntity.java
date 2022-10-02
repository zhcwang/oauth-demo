package com.felix.oauth2server.repository.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;


@Entity
@Table(indexes = {@Index(name = "index_username", columnList = "username")})
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LoginHistoryEntity extends BaseEntity {

    private static final long serialVersionUID = -7088423724470075317L;
    private String clientId;
    @Column(nullable = false, columnDefinition = "varchar")
    private String username;
    private String ip;
    private String device;

  
}
