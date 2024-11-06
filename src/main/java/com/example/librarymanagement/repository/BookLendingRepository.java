package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.BookLending;
import com.example.librarymanagement.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookLendingRepository extends JpaRepository<BookLending, Long> {
    Optional<BookLending> findById(Long lendingId);
    List<BookLending> findByUserAndReturnDateIsNull(User user);
    // Phương thức kiểm tra xem có BookLending nào cho userId, bookId và returnDate là null không
    boolean existsByUser_UserIdAndBook_BookIdAndReturnDateIsNull(Long userId, Long bookId);
    BookLending findByUser_UserIdAndBook_BookId(Long userId, Long bookId);
}
