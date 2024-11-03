package com.example.librarymanagement.model.entity;

import com.example.librarymanagement.service.BookLendingService;
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
@Table( name = "request_renewal")
public class RequestRenewal {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "bookLending_id", nullable = false)
    private BookLending bookLending;

    @JsonFormat( pattern = "dd-MM-yyyy")
    private LocalDate renewalDate;

    @Enumerated(EnumType.STRING)
    private RequestRenewalStatus status = RequestRenewalStatus.PENDING;

    public enum RequestRenewalStatus {
        PENDING, APPROVED, REJECTED
    }
}

