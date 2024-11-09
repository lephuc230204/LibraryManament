package com.example.librarymanagement.controller;


import com.example.librarymanagement.model.dto.UserDto;
import com.example.librarymanagement.payload.request.OTPForm;
import com.example.librarymanagement.payload.request.SignInForm;
import com.example.librarymanagement.payload.request.SignUpForm;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.payload.response.ResponseError;
import com.example.librarymanagement.service.AuthService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody SignInForm form){return ResponseEntity.ok(authService.login(form));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseData<UserDto> > register(@RequestBody SignUpForm form){
        return ResponseEntity.ok(authService.register(form));
    }

    @GetMapping("/refresh")

    public ResponseEntity refreshToken(@RequestHeader("X-Refresh-Token") String refreshToken){return ResponseEntity.ok(authService.refreshJWT(refreshToken));}




}