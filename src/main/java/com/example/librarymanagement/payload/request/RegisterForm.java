package com.example.librarymanagement.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterForm {
    private String email;
    private String username;
    private String password;
    private String phone;
    private String confirmPassword;
    private String role;
    @JsonFormat(pattern ="dd-MM-yyyy")
    private LocalDate dob;
}
