package com.example.librarymanagement.payload.request;

import lombok.Data;

@Data
public class BookLendingGetByUserAndBookForm {
    private Long bookId;
    private String email;
}
