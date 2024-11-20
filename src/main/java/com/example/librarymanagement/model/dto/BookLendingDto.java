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
    private Long bookId;
    private String bookName;
    private String image;
    private String author;
    @JsonFormat( pattern = "dd-MM-yyyy")
    private LocalDate creationDate;
    @JsonFormat( pattern = "dd-MM-yyyy")
    private LocalDate dueDate;
    @JsonFormat( pattern = "dd-MM-yyyy")
    private LocalDate returnDate;
    private String email;
    private Long staffid;

    public static BookLendingDto toDto(BookLending bookLending) {
        return BookLendingDto.builder()
                .lendingId(bookLending.getLendingId())
                .bookId(bookLending.getBook().getBookId())
                .bookName(bookLending.getBook().getBookName())
                .image(bookLending.getBook().getImage())
                .author(bookLending.getBook().getAuthor().getName())
                .creationDate(bookLending.getCreationDate())
                .dueDate(bookLending.getDueDate())
                .returnDate(bookLending.getReturnDate())
                .staffid(bookLending.getStaff().getId())
                .email(bookLending.getUser().getEmail())
                .build();
    }
}
