package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.dto.BookDto;
import com.example.librarymanagement.model.dto.BookReservationDto;
import com.example.librarymanagement.model.dto.UserDto;
import com.example.librarymanagement.payload.request.*;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/admin")
public class AdminController {

    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private BookReservationService bookReservationService;
    @Autowired
    private BookLendingService bookLendingService;
    @Autowired
    private RequestRenewalService requestRenewalService;

    @GetMapping("/books")
    public ResponseEntity<ResponseData<List<BookDto>>> getAll(){
        return ResponseEntity.ok(bookService.getAll());
    }

    @PostMapping("/books/create")
    public ResponseEntity<ResponseData<BookDto>> create(@Valid @ModelAttribute BookForm form) {
        return ResponseEntity.ok(bookService.create(form));  // Truyền file đúng cách vào service
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<ResponseData<BookDto>> getBookById(@PathVariable Long id) {
        return  ResponseEntity.ok(bookService.getBookById(id));
    }

    @DeleteMapping("/books/delete/{bookId}")
    public  ResponseEntity<ResponseData<Void>> deleteBook(@PathVariable Long bookId){
        return ResponseEntity.ok(bookService.deleteBook(bookId));
    }

    @PutMapping("/books/update/{bookId}")
    public  ResponseEntity<ResponseData<Void>> updateBook(@Valid @ModelAttribute BookForm form, @PathVariable Long bookId){
        return ResponseEntity.ok(bookService.updateBook(form,bookId));
    }

    // book reservation
    // tạo đặt sách
    @PostMapping("/book-reservations/create")
    public ResponseEntity<ResponseData<BookReservationDto>> createBookReservation(@RequestBody BookReservationForm bookReservationForm){
        return ResponseEntity.ok(bookReservationService.createBookReservation(bookReservationForm ));
    };
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

    // tạo 1 mươn sách
    @PostMapping("/book-lending/add")
    public ResponseEntity createBookLending(@RequestBody BookLendingForm form, Principal principal) {
        return ResponseEntity.ok(bookLendingService.create(form, principal));
    }

    // Lấy tất ca sách đã mươợn
    @GetMapping("/book-lending/getall")
    public ResponseEntity getAllBookLending() {
        return ResponseEntity.ok(bookLendingService.getAllBookLending());
    }

    // nguoi dung tra sách
    @PutMapping("/return-book")
    public ResponseEntity<?> returnBookLending(@RequestBody Map<String, Object> requestBody) {
        String username = (String) requestBody.get("username");
        Long bookId = ((Number) requestBody.get("bookId")).longValue(); // Chuyển đổi từ Number sang Long

        return ResponseEntity.ok(bookLendingService.returnBook(username, bookId));
    }

    // tra loi yeu cau gia haạn
    @PutMapping("/book-renewal/reply")
    public ResponseEntity<?> reply(@RequestBody Map<String, Object> requestBody) {
        String reply = (String) requestBody.get("reply");
        Long requestRenewalId = ((Number) requestBody.get("requestRenewalId")).longValue(); // Chuyển đổi từ Number sang Long
        return ResponseEntity.ok(requestRenewalService.reply(requestRenewalId, reply));
    }

    // lay tat ca yeu cau gia han
    @GetMapping("/book-renewal")
    public ResponseEntity getAllRequestRenewal() {
        return ResponseEntity.ok(requestRenewalService.getAllRequestRenewal());
    }

    @GetMapping ("/users")
    public ResponseEntity<ResponseData<List<UserDto>>> gelAllUser(){
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseData<UserDto>> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getById(userId));
    }

    @PutMapping("/users/update/{userId}")
    public ResponseEntity<ResponseData<String>> UpdateUser(@PathVariable Long userId, @RequestBody UserForm form){
        return ResponseEntity.ok(userService.update(userId, form));
    }

    @DeleteMapping("/users/delete/{userId}")
    public ResponseEntity<ResponseData<String>> deleteUser(@PathVariable Long userId){
        return ResponseEntity.ok(userService.delete(userId));
    }
    // chua xong
    @GetMapping("/users/search/")
    public ResponseEntity<ResponseData<List<UserDto>>> searchUser(@RequestParam String email){
        return ResponseEntity.ok(userService.searchUser(email));
    }


    @PostMapping("/users/register")
    public ResponseEntity<ResponseData<UserDto> > register(@RequestBody SignUpForm form){
        return ResponseEntity.ok(authService.register(form));
    }

}