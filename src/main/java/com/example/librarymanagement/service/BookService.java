package com.example.librarymanagement.service;

import com.example.librarymanagement.model.dto.BookDto;
import com.example.librarymanagement.model.entity.Author;
import com.example.librarymanagement.model.entity.Category;
import com.example.librarymanagement.payload.request.BookForm;
import com.example.librarymanagement.payload.response.ResponseData;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    // THÊM SÁCH
    ResponseData<BookDto> create(BookForm form);

    ResponseData<BookDto> getBookById(Long id);

    // XÓA SÁCH
    ResponseData<Void> deleteBook(Long bookId);

    // CẬP NHẬT SÁCH
    ResponseData<Void> updateBook(BookForm form, Long bookId);

    // LẤY TẤT CẢ CÁC SÁCH
    ResponseData<Page<BookDto>> getAll(int page, int size);

    // KIỂM TRA HOẶC TẠO TÁC GIẢ
    Author checkOrCreateAuthor(String authorName);

    // KIỂM TRA HOẶC TẠO DANH MỤC
    Category checkOrCreateCategory(String categoryName);

    // TÌM KIẾM SÁCH
}
