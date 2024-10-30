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
    private String publisher;
    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "dd-mm-yyyy")
    private LocalDate postingDate;
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        AVAILABLE,   // Chưa được mượn
        BORROWED    // Đã được mượn
    }
}
