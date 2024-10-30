package com.example.librarymanagement.service;

import com.example.librarymanagement.model.dto.AuthDto;
import com.example.librarymanagement.payload.request.SignInForm;
import com.example.librarymanagement.payload.request.SignUpForm;
import com.example.librarymanagement.payload.response.ResponseData;


public interface AuthService {
    AuthDto login(SignInForm form);
    ResponseData<String> register(SignUpForm form);
    AuthDto refreshJWT(String refreshToken);
    String confirmUser(long userId, String otpCode);

}