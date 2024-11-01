package com.example.librarymanagement.model.dto;

import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.BookLending;
import com.example.librarymanagement.model.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class BookLendingDto {
    private Long lendingId;
    private Book book;
    @JsonFormat( pattern = "dd-MM-yyyy")
    private LocalDate creationDate;
    @JsonFormat( pattern = "dd-MM-yyyy")
    private LocalDate dueDate;
    @JsonFormat( pattern = "dd-MM-yyyy")
    private LocalDate returnDate;
    private User user;
    private User staff;

    public static BookLendingDto toDto(BookLending bookLending) {
        return BookLendingDto.builder()
                .lendingId(bookLending.getLendingId())
                .book(bookLending.getBook())
                .creationDate(bookLending.getCreationDate())
                .dueDate(bookLending.getDueDate())
                .staff(bookLending.getStaff())
                .user(bookLending.getUser())
                .build();
    }
}
