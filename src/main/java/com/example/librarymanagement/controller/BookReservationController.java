package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.dto.BookReservationDto;
import com.example.librarymanagement.payload.request.BookReservationForm;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.service.BookReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users/book-reservations")
@RequiredArgsConstructor
public class BookReservationController {
    private final BookReservationService bookReservationService;

    // lịch sử đặt sách
    @GetMapping("/history")
    public ResponseEntity<ResponseData<List<BookReservationDto>>> getBookReservationByUserId(Principal principal){
        return ResponseEntity.ok(bookReservationService.getBookReservationByUserId(principal));
    };
    // Xem chi tiết đặt sách của người dùng
    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseData<BookReservationDto>> getBookReservationById(@PathVariable("id") Long id, Principal principal){
        return ResponseEntity.ok(bookReservationService.getBookReservationById(id, principal));
    };
    // Hủy đặt sách
    @PutMapping("/cancel/{id}")
    public ResponseEntity<ResponseData<BookReservationDto>> cancelBookReservation(@PathVariable("id") Long id,@RequestBody BookReservationForm bookReservationForm, Principal principal){
        return ResponseEntity.ok(bookReservationService.cancelBookReservation(id, bookReservationForm, principal));
    };
}
