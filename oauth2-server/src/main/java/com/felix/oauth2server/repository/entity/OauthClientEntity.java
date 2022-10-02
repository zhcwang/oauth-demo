package com.felix.oauth2server.repository.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"clientId"}))
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OauthClientEntity extends BaseEntity {
    private static final long serialVersionUID = -3042356579574873614L;
    @Column(nullable = false)
    private String clientId;
    private String applicationName;
    private String resourceIds;
    @Column(nullable = false)
    private String clientSecret;
    private String scope;
    @Column(nullable = false)
    private String authorizedGrantTypes;
    private String webServerRedirectUri;
    @Column(nullable = false)
    private String authorities;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String additionalInformation;
    private String autoApprove;
    private LocalDateTime expirationDate;
}
    
