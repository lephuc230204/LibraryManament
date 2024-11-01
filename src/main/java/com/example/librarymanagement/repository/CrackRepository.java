package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.Crack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrackRepository extends JpaRepository<Crack, Long> {
    Optional<Crack> findById(Long crackId);

}
