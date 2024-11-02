package com.example.librarymanagement.service;

import com.example.librarymanagement.model.dto.AuthDto;
import com.example.librarymanagement.model.dto.UserDto;
import com.example.librarymanagement.payload.request.SignInForm;
import com.example.librarymanagement.payload.request.SignUpForm;
import com.example.librarymanagement.payload.response.ResponseData;


public interface AuthService {
    AuthDto login(SignInForm form);
    // ADMIN TAO TAI KHOAN CHO NGUOI DUNG HOAC NHAN VIEN
    ResponseData<UserDto> register(SignUpForm form);

    AuthDto refreshJWT(String refreshToken);
    String confirmUser(long userId, String otpCode);


    //
}