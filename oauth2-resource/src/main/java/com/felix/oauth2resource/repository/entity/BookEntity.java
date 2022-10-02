package com.felix.oauth2resource.repository.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity(name = "book")
@Table(name = "demo_book")
public class BookEntity extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar", nullable = false)
    private String name;

    @Column(columnDefinition = "varchar", nullable = false)
    private String description;

    @Column(columnDefinition = "varchar", nullable = false)
    private String reason;

    @Column(columnDefinition = "varchar", nullable = false)
    private String image;

}
