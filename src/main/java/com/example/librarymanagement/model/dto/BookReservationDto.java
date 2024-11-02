package com.example.librarymanagement.model.dto;

import com.example.librarymanagement.model.entity.BookReservation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookReservationDto {
    private Long reservationId;
    private LocalDate creationDate;
    private Long userId;
    private Long bookId;
    private BookReservation.Status status;

    public static BookReservationDto toDto(BookReservation bookReservation){
        return BookReservationDto.builder()
                .reservationId(bookReservation.getReservationId())
                .bookId(bookReservation.getBook().getBookId())
                .userId(bookReservation.getUser().getUserId())
                .status(bookReservation.getStatus())
                .creationDate(LocalDate.now())
                .build();
    }
}
