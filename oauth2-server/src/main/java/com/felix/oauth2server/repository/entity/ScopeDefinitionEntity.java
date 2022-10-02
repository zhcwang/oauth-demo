package com.felix.oauth2server.repository.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"scope"}))
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ScopeDefinitionEntity extends BaseEntity {
    private static final long serialVersionUID = 1522239249392557103L;
    private String scope;
    private String definition;
}
