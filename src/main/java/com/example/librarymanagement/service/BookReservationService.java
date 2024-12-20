package com.example.librarymanagement.service;

import com.example.librarymanagement.model.dto.BookReservationDto;
import com.example.librarymanagement.payload.request.BookReservationForm;
import com.example.librarymanagement.payload.response.ResponseData;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface BookReservationService {
    // user
    // tạo đặt sách
    ResponseData<BookReservationDto> createBookReservation(BookReservationForm bookReservationForm);
    // lịch sử đặt sách
    ResponseData<List<BookReservationDto>> getBookReservationByUserId(Principal principal);
    // Xem chi tiết đặt sách của người dùng
    ResponseData<BookReservationDto> getBookReservationById(Long id, Principal principal);
    // Hủy đặt sách
    ResponseData<BookReservationDto> cancelBookReservation(Long id, BookReservationForm bookReservationForm, Principal principal);
    //manager
    // xem chi tiết đặt sách
    ResponseData<BookReservationDto> getBookReservationById(Long id);
    // xóa đặt sách
    ResponseData<String> deleteBookReservation(Long id);
    // xác nhận đặt sách
    ResponseData<BookReservationDto> updateBookReservation(Long id, BookReservationForm bookReservationForm);
    // lấy ra tất cả đặt sách
    ResponseData<Page<BookReservationDto>> getAllBookReservation(int page, int size);

}
