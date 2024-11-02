package com.example.librarymanagement.payload.request;

import com.example.librarymanagement.model.entity.BookReservation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookReservationForm {
    private Long userId;
    private Long bookId;
    private BookReservation.Status status;
    private LocalDate creationDate;
}