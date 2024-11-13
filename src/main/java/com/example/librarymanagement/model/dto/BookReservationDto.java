package com.example.librarymanagement.model.dto;

import com.example.librarymanagement.model.entity.BookReservation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookReservationDto {
    private Long reservationId;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate creationDate;
    private String email;
    private Long bookId;
    private String bookName;
    private String image;
    private BookReservation.Status status;

    public static BookReservationDto toDto(BookReservation bookReservation){
        return BookReservationDto.builder()
                .reservationId(bookReservation.getReservationId())
                .bookId(bookReservation.getBook().getBookId())
                .bookName(bookReservation.getBook().getBookName())
                .email(bookReservation.getUser().getEmail())
                .image(bookReservation.getBook().getImage())
                .status(bookReservation.getStatus())
                .creationDate(LocalDate.now())
                .build();
    }
}
