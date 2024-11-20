package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.dto.*;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.payload.request.*;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/books")
    public ResponseEntity<ResponseData<Page<BookDto>>> getBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(bookService.getAll(page,size));
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
    public ResponseEntity<ResponseData<Page<BookReservationDto>>> getAllBookReservation(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookReservationService.getAllBookReservation(page , size));
    }

    // tạo 1 mươn sách
    @PostMapping("/book-lending/add")
    public ResponseEntity createBookLending(@RequestBody BookLendingForm form, Principal principal) {
        return ResponseEntity.ok(bookLendingService.create(form, principal));
    }

    // Lấy tất ca sách đã mươợn
    @GetMapping("/book-lending/getall")
    public ResponseEntity<ResponseData<Page<BookLendingDto>>> getAllBookLending(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookLendingService.getAllBookLending(page, size));
    }

    // nguoi dung tra sách
    @PutMapping("/return-book/{bookLendingId}")
    public ResponseEntity<?> returnBookLending(@PathVariable Long bookLendingId) {
        return ResponseEntity.ok(bookLendingService.returnBook(bookLendingId));
    }

    // laays bookLiding theo email va bookId
    @PostMapping("/book-lending/get-email-booId")
    public ResponseEntity<ResponseData<BookLendingDto>> getBookLendingByEmailAndBookId(@RequestBody BookLendingGetByUserAndBookForm form) {
        return ResponseEntity.ok(bookLendingService.getBookLendingByEmailBookId(form.getEmail(),form.getBookId()));
    }

    @GetMapping("/book-lending/{id}")
    public ResponseEntity<ResponseData<BookLendingDto>> getBookLendingById(@PathVariable("id") Long id){
        return ResponseEntity.ok(bookLendingService.getBookLendingById(id));
    }

    @PutMapping("/book-lending/update/{bookLendingId}")
    public ResponseEntity<ResponseData<BookLendingDto>> updateBookLending(@PathVariable Long bookLendingId, @RequestBody BookLendingForm form) {
        return ResponseEntity.ok(bookLendingService.updateBookLending(bookLendingId,form));
    }

    @DeleteMapping("/book-lending/delete/{id}")
    public ResponseEntity<ResponseData<String>> deleteBookLendingById(@PathVariable("id") Long id){
        return ResponseEntity.ok(bookLendingService.deleteBookLendingById(id));
    }

    // tra loi yeu cau gia haạn
    @PutMapping("/book-renewal/reply/{requestRenewalId}")
    public ResponseEntity<?> reply(@RequestParam String status,@PathVariable Long requestRenewalId ) {
        return ResponseEntity.ok(requestRenewalService.reply(requestRenewalId, status));
    }

    // lay tat ca yeu cau gia han
    @GetMapping("/book-renewal")
    public ResponseEntity<ResponseData<Page<RequestRenewalDto>>> getAllRequestRenewal(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(requestRenewalService.getAllRequestRenewal(page, size));
    }

    @GetMapping("/book-renewal/{id}")
    public ResponseEntity<ResponseData<RequestRenewalDto>> getRequestRenewalById(@PathVariable("id") Long id){
        return ResponseEntity.ok(requestRenewalService.getRequestRenewalById(id));
    }
    @DeleteMapping("/book-renewal/delete/{id}")
    public ResponseEntity<ResponseData<String>> deleteRequestRenewalById(@PathVariable("id") Long id){
        return ResponseEntity.ok(requestRenewalService.deleteRequestRenewalById(id));
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseData<Page<UserDto>>> getAllUser(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAll(page, size));
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
    @PutMapping("/users/restore/{userId}")
    public ResponseEntity<ResponseData<String>> restoreUser(@PathVariable Long userId){
        return ResponseEntity.ok(userService.restore(userId));
    }
    // chua xong
    @GetMapping("/users/search/")
    public ResponseEntity<ResponseData<Page<UserDto>>> searchUser(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.searchUser(page, size, query));
    }


    @PostMapping("/users/register")
    public ResponseEntity<ResponseData<UserDto> > register(@RequestBody SignUpForm form){
        return ResponseEntity.ok(authService.register(form));
    }

    // add notification
    @PostMapping("/notification/lending/create")
    public ResponseEntity<ResponseData<String>> creatNotificationBookLending() {
        return ResponseEntity.ok(notificationService.createNotificationWithLendingDueDateLessTwoDay());
    }
    @PostMapping("/notification/card-library/create")
    public ResponseEntity<ResponseData<String>> creatNotificationCardLibrary() {
        return ResponseEntity.ok(notificationService.createNotificationWithCardLibraryExpiredLessTwoDay());
    }

}