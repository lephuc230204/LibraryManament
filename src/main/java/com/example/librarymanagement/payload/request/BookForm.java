package com.example.librarymanagement.payload.request;

import com.example.librarymanagement.model.entity.Author;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.Category;
import com.example.librarymanagement.model.entity.Crack;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookForm {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private String bookName;
    private Long quantity;
    private Long currentQuantity;
    private Long categoryId;
    private String authorName;
    private String publisher;
    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate postingDate;
    private Long crackId;

}
