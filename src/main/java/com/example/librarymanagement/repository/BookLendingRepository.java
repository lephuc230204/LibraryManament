package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.BookLending;
import com.example.librarymanagement.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookLendingRepository extends JpaRepository<BookLending, Long> {
    Optional<BookLending> findById(Long lendingId);

    List<BookLending> findByUserAndReturnDateIsNull(User user);
    // Phương thức kiểm tra xem có BookLending nào cho userId, bookId và returnDate là null không
    boolean existsByUser_UserIdAndBook_BookIdAndReturnDateIsNull(Long userId, Long bookId);
    BookLending findByUser_UserIdAndBook_BookIdAndReturnDateIsNull(Long userId, Long bookId);

    @Query("SELECT b FROM BookLending b WHERE b.dueDate BETWEEN :startDate AND :endDate")
    List<BookLending> findAllWithDueDateWithinNextTwoDays(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT b FROM BookLending b WHERE b.book = :book AND b.returnDate IS NULL")
    Optional<BookLending> findBookLendingByBookAndReturnDateIsNull(@Param("book") Book book);

}
