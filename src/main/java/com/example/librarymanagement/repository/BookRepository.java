package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findById( Long bookId);
    boolean existsByCrackId(Long crackId);

}
