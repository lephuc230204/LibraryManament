package com.example.librarymanagement.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class BookForm {

    private String bookName;
    private MultipartFile image;
    private Long quantity;
    private Long currentQuantity;
    private String categoryName;
    private String authorName;
    private String publisher;
    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate postingDate;
    private Long crackId;

}
