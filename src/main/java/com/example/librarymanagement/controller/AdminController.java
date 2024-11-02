package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.dto.BookDto;
import com.example.librarymanagement.payload.request.BookForm;
import com.example.librarymanagement.payload.response.ResponseData;
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

}
