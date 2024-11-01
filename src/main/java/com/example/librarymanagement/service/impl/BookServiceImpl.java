package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.model.dto.BookDto;
import com.example.librarymanagement.model.entity.Author;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.Category;
import com.example.librarymanagement.model.entity.Crack;
import com.example.librarymanagement.payload.request.BookForm;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.payload.response.ResponseError;
import com.example.librarymanagement.repository.AuthorRepository;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.CategoryRepository;
import com.example.librarymanagement.repository.CrackRepository; // Chắc chắn rằng bạn có repository này
import com.example.librarymanagement.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CrackRepository crackRepository; // Đúng repository cho Crack

    @Override
    public ResponseData<List<BookDto>> getAll() {
        log.info("Retrieving list of books");

        List<BookDto> books = bookRepository.findAll().stream()
                .map(BookDto::toDto)
                .collect(Collectors.toList());

        log.info("Get books successfully");
        return new ResponseData<>(200, "Get books successfully", books);
    }

    @Override
    public ResponseData<Void> create(BookForm form) {
        log.info("Creating book");

        // Tạo đối tượng Book mới và gán giá trị từ form
        Book newBook = new Book();
        newBook.setBookName(form.getBookName());
        newBook.setQuantity(form.getQuantity());
        newBook.setPublisher(form.getPublisher());
        newBook.setPostingDate(form.getPostingDate());

        // Tìm Category theo ID
        Category category = categoryRepository.findById(form.getCategoryId()).orElse(null);
        if (category == null) {
            log.error("Category not found for ID: {}", form.getCategoryId());
            return new ResponseError<>(404, "Category not found");
        }
        newBook.setCategory(category);

        // Tìm Author theo ID
        Author author = authorRepository.findById(form.getAuthorId()).orElse(null);
        if (author == null) {
            log.error("Author not found for ID: {}", form.getAuthorId());
            return new ResponseError<>(404, "Author not found");
        }
        newBook.setAuthor(author);

        // Tìm Crack theo ID
        Crack crack = crackRepository.findById(form.getCrackId()).orElse(null);
        if (crack == null) {
            log.error("Crack not found for ID: {}", form.getCrackId());
            return new ResponseError<>(404, "Crack not found");
        }
        newBook.setCrack(crack);

        // Kiểm tra xem đã có sách nào với cùng crack_id chưa
        if (bookRepository.existsByCrackId(crack.getId())) {
            log.error("Book with crack_id {} already exists", crack.getId());
            return new ResponseError<>(409, "Book with this crack_id already exists");
        }

        // Lưu đối tượng Book vào cơ sở dữ liệu
        bookRepository.save(newBook);
        log.info("Book created successfully");
        return new ResponseData<>(200, "Book created successfully", null);
    }

}
