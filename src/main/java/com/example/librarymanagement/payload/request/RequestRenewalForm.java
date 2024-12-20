package com.example.librarymanagement.payload.request;

import com.example.librarymanagement.model.entity.BookReservation;
import com.example.librarymanagement.model.entity.RequestRenewal;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RequestRenewalForm {
    private Long bookLendingId;
    private LocalDate renewalDate;
    private String description;
}
