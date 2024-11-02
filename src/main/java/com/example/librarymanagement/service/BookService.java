package com.example.librarymanagement.service;

import com.example.librarymanagement.model.dto.BookDto;
import com.example.librarymanagement.payload.request.BookForm;
import com.example.librarymanagement.payload.response.ResponseData;

import java.util.List;

public interface BookService {
    // THEM
    ResponseData<BookDto> create(BookForm form);
    // XOA
    ResponseData<Void> deleteBook(Long bookId);
    // UPDATE
    ResponseData<Void> updateBook(BookForm form,Long bookId);
    // GET ALL
    ResponseData<List<BookDto>> getAll();
    //
}
