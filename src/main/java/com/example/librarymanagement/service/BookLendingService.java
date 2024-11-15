package com.example.librarymanagement.service;

import com.example.librarymanagement.model.dto.BookLendingDto;
import com.example.librarymanagement.payload.request.BookLendingForm;
import com.example.librarymanagement.payload.response.ResponseData;

import javax.swing.*;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

public interface BookLendingService {

    ResponseData<BookLendingDto> create(BookLendingForm form, Principal principal);
    ResponseData<List<BookLendingDto>> getAllBookLending();
    ResponseData<List<BookLendingDto>> getMyBookLending(Principal principal);
    ResponseData<BookLendingDto> returnBook(String username,Long bookid);
    ResponseData<BookLendingDto> getBookLendingById(Long id);
    ResponseData<String> deleteBookLendingById(Long id);


}
