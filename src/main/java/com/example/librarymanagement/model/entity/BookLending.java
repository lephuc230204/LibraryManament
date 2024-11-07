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
@Table( name = "book_lending")
public class BookLending {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long lendingId;
    @ManyToOne
    @JoinColumn( name = "book_id")
    private Book book;
    @JsonFormat( pattern = "dd-MM-yyyy")
    private LocalDate creationDate;
    @JsonFormat( pattern = "dd-MM-yyyy")
    private LocalDate dueDate;
    @JsonFormat( pattern = "dd-MM-yyyy")
    private LocalDate returnDate;

    @ManyToOne
    @JoinColumn( name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn( name = "staff_id")
    private User staff;

}
