package com.example.librarymanagement.payload.request;

import lombok.Data;

@Data
public class LoginForm {
    private String email;
    private String password;
}
