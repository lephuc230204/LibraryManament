package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.BookLending;
import com.example.librarymanagement.model.entity.CardLibrary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardLibraryRepository extends JpaRepository<CardLibrary, String> {

    Optional<CardLibrary> findById(String numberCard);
    @Query("SELECT b FROM CardLibrary b WHERE b.expired BETWEEN :startDate AND :endDate")
    List<CardLibrary> findAllWithExpiredWithinNextTwoDays(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
