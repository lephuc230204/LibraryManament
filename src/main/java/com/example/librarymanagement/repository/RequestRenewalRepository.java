package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.BookReservation;
import com.example.librarymanagement.model.entity.RequestRenewal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRenewalRepository extends JpaRepository<RequestRenewal, Long> {
    Optional<RequestRenewal> findById(Long requestRenewalId);
    List<RequestRenewal> findByUserId(Long id);
}