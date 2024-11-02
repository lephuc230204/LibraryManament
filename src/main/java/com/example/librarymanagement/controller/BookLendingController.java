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

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/users/book-lending")
public class BookLendingController {

    @Autowired
    private final BookLendingService bookLendingService;

    @PostMapping("/add")
    public ResponseEntity createBookLending(@RequestBody BookLendingForm form, Principal principal) {
        return ResponseEntity.ok(bookLendingService.create(form, principal));
    }

    @PutMapping("/renewal")
    public ResponseEntity renewBookLending(Principal principal, @RequestBody LocalDate renewalDate, @RequestBody Long bookid) {
        return ResponseEntity.ok(bookLendingService.bookRenewal(principal, renewalDate, bookid));
    }

    @GetMapping("/getall")
    public ResponseEntity getAllBookLending() {
        return ResponseEntity.ok(bookLendingService.getAllBookLending());
    }

    @GetMapping
    public ResponseEntity getBookLending(Principal principal){
        return  ResponseEntity.ok(bookLendingService.getMyBookLending(principal));
    }
}
