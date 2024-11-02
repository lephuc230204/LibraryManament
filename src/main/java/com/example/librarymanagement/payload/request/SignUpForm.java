package com.example.librarymanagement.payload.request;

import com.example.librarymanagement.model.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpForm {
    private String username;
    private String email;
    private String password;
    private String phone;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;
    private String confirmPassword;
    private String role;

}