package com.example.librarymanagement.model.dto;

import com.example.librarymanagement.model.entity.Author;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.Category;
import com.example.librarymanagement.model.entity.Crack;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
public class BookDto {
    private Long bookId;
    private String image;
    private String bookName;
    private Long quantity;
    private Long currentQuantity;
    private String categoryName;
    private String authorName;
    private String publisher;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate postingDate;

    private Long crackId;

    public static BookDto toDto(Book book) {
        return BookDto.builder()
                .bookId(book.getBookId())
                .image(book.getImage())
                .bookName(book.getBookName())
                .quantity(book.getQuantity())
                .currentQuantity(book.getCurrentQuantity())
                .categoryName(book.getCategory().getCategoryName())
                .authorName(book.getAuthor().getName())
                .publisher(book.getPublisher())
                .postingDate(book.getPostingDate())
                .crackId(book.getCrack().getId())
                .build();
    }
}
