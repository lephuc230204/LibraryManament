package com.example.librarymanagement.service;

import com.example.librarymanagement.model.dto.BookDto;
import com.example.librarymanagement.payload.request.BookForm;
import com.example.librarymanagement.payload.response.ResponseData;

import java.util.List;

public interface BookService {
    ResponseData<List<BookDto>> getAll();
    ResponseData<Void> create(BookForm form);
}
