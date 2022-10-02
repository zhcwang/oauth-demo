package com.felix.oauth2resource.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public class AuditedEntity implements Serializable {

    private static final long serialVersionUID = -3047239688070628623L;

    @CreatedDate
    @Column(name = "created_on", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant createdOn;

    @CreatedBy
    @Column(name = "created_by", columnDefinition = "varchar not null")
    private String createdBy;

    @LastModifiedDate
    @Column(name = "modified_on", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant modifiedOn;

    @LastModifiedBy
    @Column(name = "modified_by", columnDefinition = "varchar")
    private String modifiedBy;

    // These attributes are only technical -> not included in equals and hashcode
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    
}
