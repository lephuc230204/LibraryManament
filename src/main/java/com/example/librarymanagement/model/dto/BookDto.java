package com.example.librarymanagement.model.dto;

import com.example.librarymanagement.model.entity.Author;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.Category;
import com.example.librarymanagement.model.entity.Crack;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookDto {
    private Long bookId;
    private String bookName;
    private Long quantity;
    private String category;
    private String author;
    private String publisher;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate postingDate;

    private Book.Status status;
    private Long crackId;

    public static BookDto toDto(Book book) {
        return BookDto.builder()
                .bookId(book.getBookId())
                .bookName(book.getBookName())
                .quantity(book.getQuantity())
                .category(book.getCategory().getCategoryName())
                .author(book.getAuthor().getName())
                .publisher(book.getPublisher())
                .postingDate(book.getPostingDate())
                .status(book.getStatus())
                .crackId(book.getCrack().getId())
                .build();
    }
}
