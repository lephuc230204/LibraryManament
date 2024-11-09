package com.example.librarymanagement.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table( name = "Books")
public class Book {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long bookId;
    private String image;
    private String bookName;
    private Long quantity;
    private Long currentQuantity;

    @ManyToOne
    @JoinColumn( name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    private String publisher;
    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate postingDate;
    @Enumerated(EnumType.STRING)

    @OneToOne
    @JoinColumn(name = "crack_id")
    private Crack crack;

}
