package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.dto.BookDto;
import com.example.librarymanagement.model.dto.BookReservationDto;
import com.example.librarymanagement.payload.request.BookForm;
import com.example.librarymanagement.payload.request.BookReservationForm;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.service.BookReservationService;
import com.example.librarymanagement.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
public class AdminController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookReservationService bookReservationService;

    @GetMapping("/books")
    public ResponseEntity<ResponseData<List<BookDto>>> getAll(){
        return ResponseEntity.ok(bookService.getAll());
    }

    @PostMapping("/books/create")
    public  ResponseEntity<ResponseData<BookDto>> create(@RequestBody BookForm form){
        return ResponseEntity.ok(bookService.create(form));
    }

    @DeleteMapping("/books/delete/{bookId}")
    public  ResponseEntity<ResponseData<Void>> deleteBook(@PathVariable Long bookId){
        return ResponseEntity.ok(bookService.deleteBook(bookId));
    }

    @PutMapping("/books/update/{bookId}")
    public  ResponseEntity<ResponseData<Void>> updateBook(@RequestBody BookForm form, @PathVariable Long bookId){
        return ResponseEntity.ok(bookService.updateBook(form,bookId));
    }

    // book reservation
    // Xem chi tiết đặt sách
    @GetMapping("/book-reservations/detail/{id}")
    public ResponseEntity<ResponseData<BookReservationDto>> getBookReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(bookReservationService.getBookReservationById(id));
    }

    // Xóa đặt sách
    @DeleteMapping("/book-reservations/delete/{id}")
    public ResponseEntity<ResponseData<String>> deleteBookReservation(@PathVariable Long id) {
        return ResponseEntity.ok(bookReservationService.deleteBookReservation(id));
    }

    // Cập nhật đặt sách (xác nhận đặt sách)
    @PutMapping("/book-reservations/update/{id}")
    public ResponseEntity<ResponseData<BookReservationDto>> updateBookReservation(
            @PathVariable Long id,
            @RequestBody BookReservationForm bookReservationForm) {
        return ResponseEntity.ok(bookReservationService.updateBookReservation(id, bookReservationForm));
    }

    // Lấy tất cả đặt sách
    @GetMapping("/book-reservations")
    public ResponseEntity<ResponseData<List<BookReservationDto>>> getAllBookReservations() {
        return ResponseEntity.ok(bookReservationService.getAllBookReservation());
    }
}