package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.BookReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookReservationRepository extends JpaRepository<BookReservation, Long> {
    @Override
    Optional<BookReservation> findById(Long reservationID);
}
