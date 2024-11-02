package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.BookLending;
import com.example.librarymanagement.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookLendingRepository extends JpaRepository<BookLending, Long> {
    Optional<BookLending> findById(Long lendingId);
    List<BookLending> findByUser(User user);
}