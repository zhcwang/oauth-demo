package com.felix.oauth2resource.repository;

import com.felix.oauth2resource.repository.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository
    extends JpaRepository<BookEntity, Long> {

    Optional<BookEntity> findOneById(Long id);

}
