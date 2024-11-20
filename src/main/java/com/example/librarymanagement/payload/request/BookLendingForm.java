package com.example.librarymanagement.payload.request;

import com.example.librarymanagement.model.entity.User;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class BookLendingForm {
    private Long bookId;
    @JsonFormat( pattern = "dd-MM-yyyy")
    private LocalDate dueDate;
    private String email;
}
