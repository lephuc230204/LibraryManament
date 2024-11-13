package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.BookReservation;
import com.example.librarymanagement.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookReservationRepository extends JpaRepository<BookReservation, Long> {
    @Override
    Optional<BookReservation> findById(Long reservationID);
    boolean existsByUserAndBook(User user, Book book);

    List<BookReservation> findByUserId(Long id);
    boolean existsByBookAndStatus(Book book, BookReservation.Status status);
}
