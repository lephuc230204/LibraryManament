package com.example.librarymanagement.model.dto;

import com.example.librarymanagement.model.entity.BookLending;
import com.example.librarymanagement.model.entity.BookReservation;
import com.example.librarymanagement.model.entity.RequestRenewal;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RequestRenewalDto {

    private Long id;
    private Long  bookLendingId;
    private String bookName;
    private String image;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate renewalDate;
    private RequestRenewal.RequestRenewalStatus status;

    public static RequestRenewalDto toDto(RequestRenewal requestRenewal) {
        return RequestRenewalDto.builder()
                .id(requestRenewal.getId())
                .bookLendingId(requestRenewal.getBookLending().getLendingId())
                .bookName(requestRenewal.getBookLending().getBook().getBookName())
                .image(requestRenewal.getBookLending().getBook().getImage())
                .renewalDate(requestRenewal.getRenewalDate())
                .status(requestRenewal.getStatus())
                .build();
    }
}
