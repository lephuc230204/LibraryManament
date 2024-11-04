package com.example.librarymanagement.controller;


import com.example.librarymanagement.model.dto.BookLendingDto;
import com.example.librarymanagement.payload.request.BookLendingForm;
import com.example.librarymanagement.payload.request.OTPForm;
import com.example.librarymanagement.payload.request.SignInForm;
import com.example.librarymanagement.payload.request.SignUpForm;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.payload.response.ResponseError;
import com.example.librarymanagement.service.AuthService;
import com.example.librarymanagement.service.BookLendingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/users/book-lending")
public class BookLendingController {

    @Autowired
    private final BookLendingService bookLendingService;

    @GetMapping
    public ResponseEntity getMyBookLending(Principal principal){
        return  ResponseEntity.ok(bookLendingService.getMyBookLending(principal));
    }
}
