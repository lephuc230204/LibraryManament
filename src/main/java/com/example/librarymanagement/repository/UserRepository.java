package com.example.librarymanagement.repository;
import com.example.librarymanagement.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByEmail(String email);
   Optional<User> findByUsername(String username);

   boolean existsByEmail(String email);

   @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%'))")
   List<User> searchUsersByFullNameOrEmail(@Param("query") String query);

   @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.cardLibrary.cardNumber) LIKE LOWER(CONCAT('%', :query, '%'))")
   Page<User> searchUsers(@Param("query") String query, Pageable pageable);

   Page<User> findByRoleName(@Param("roleName") String roleName, Pageable pageable);
}
