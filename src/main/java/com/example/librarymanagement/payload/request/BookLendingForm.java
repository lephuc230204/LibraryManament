package com.example.librarymanagement.payload.request;

import com.example.librarymanagement.model.entity.User;
import java.time.LocalDate;
import lombok.Data;

@Data
public class BookLendingForm {
    private Long bookId;
    private LocalDate dueDate;
    private String email;
}
