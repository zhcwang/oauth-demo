package com.felix.oauth2resource.repository;

import com.felix.oauth2resource.repository.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository
    extends JpaRepository<BookEntity, Long> {

    Optional<BookEntity> findOneById(Long id);

    Page<BookEntity> findByCreatedBy(String createdBy, Pageable pageable);

    Page<BookEntity> findByNameContains(String name, Pageable pageable);

}
