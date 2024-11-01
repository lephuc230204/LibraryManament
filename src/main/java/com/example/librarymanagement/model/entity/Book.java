package com.example.librarymanagement.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String bookName;
    private Long quantity;

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
    private Status status;

    @OneToOne
    @JoinColumn(name = "crack_id")
    private Crack crack;

    public enum Status {
        AVAILABLE,   // Chưa được mượn
        BORROWED    // Đã được mượn
    }
}
