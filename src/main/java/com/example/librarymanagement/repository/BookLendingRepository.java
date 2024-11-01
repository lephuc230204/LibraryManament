package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.BookLending;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookLendingRepository extends JpaRepository<BookLending, Long> {
    Optional<BookLending> findById(Long lendingId);
    List<BookLending> findByUsername(String username);
}
