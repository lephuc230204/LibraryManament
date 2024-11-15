package com.example.librarymanagement.service;

import com.example.librarymanagement.model.dto.BookLendingDto;
import com.example.librarymanagement.payload.request.BookLendingForm;
import com.example.librarymanagement.payload.response.ResponseData;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface BookLendingService {

    ResponseData<BookLendingDto> create(BookLendingForm form, Principal principal);
    ResponseData<Page<BookLendingDto>> getAllBookLending(int page, int size);
    ResponseData<List<BookLendingDto>> getMyBookLending(Principal principal);
    ResponseData<BookLendingDto> returnBook(String username,Long bookid);



}
