package com.example.librarymanagement.payload.request;

import lombok.Data;

@Data
public class SignInForm {
    private String email;
    private String password;
}