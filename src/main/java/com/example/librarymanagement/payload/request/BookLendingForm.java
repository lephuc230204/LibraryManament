package com.example.librarymanagement.payload.request;

import com.example.librarymanagement.model.entity.User;
import java.time.LocalDate;
import lombok.Data;

@Data
public class BookLendingForm {
    private Long bookid;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Long userid;
    private User staff;
}
