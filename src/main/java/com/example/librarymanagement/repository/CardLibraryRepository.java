package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.CardLibrary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardLibraryRepository extends JpaRepository<CardLibrary, String> {

    Optional<CardLibrary> findById(String numberCard);
}
