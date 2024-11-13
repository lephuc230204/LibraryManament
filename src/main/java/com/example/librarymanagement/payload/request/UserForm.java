package com.example.librarymanagement.payload.request;

import com.example.librarymanagement.model.entity.BookReservation;
import com.example.librarymanagement.model.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserForm {
    private String username;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;
    private String email;
    private String phone;
    private String status;
    private String role;
}