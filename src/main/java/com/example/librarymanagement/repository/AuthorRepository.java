package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Override
    Optional<Author> findById(Long authorId );

    Optional<Author> findByName( String authorName);
}
