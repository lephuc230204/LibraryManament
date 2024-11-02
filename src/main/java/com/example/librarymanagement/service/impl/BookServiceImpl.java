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
    public ResponseData<BookDto> create(BookForm form) {
        log.info("Creating book");

        // Create a new Book and set values from form
        Book newBook = new Book();
        newBook.setBookName(form.getBookName());
        newBook.setQuantity(form.getQuantity());
        newBook.setCurrentQuantity(form.getQuantity());
        newBook.setPublisher(form.getPublisher());
        newBook.setPostingDate(form.getPostingDate());

        // kiem tra co trung category name hay khong
        // Find Category by ID
        Category category = categoryRepository.findById(form.getCategoryId()).orElse(null);
        if (category == null) {
            log.error("Category not found for ID: {}", form.getCategoryId());
            return new ResponseError<>(404, "Category not found");
        }
        newBook.setCategory(category);

        // Find or create Author by name
        Author author = authorRepository.findByName(form.getAuthorName()).orElse(null);
        if (author == null) {
            log.info("Creating new author: {}", form.getAuthorName());
            author = new Author();
            author.setName(form.getAuthorName());
            authorRepository.save(author);
        }
        newBook.setAuthor(author);

        // Find Crack by ID
        Crack crack = crackRepository.findById(form.getCrackId()).orElse(null);
        if (crack == null) {
            log.error("Crack not found for ID: {}", form.getCrackId());
            return new ResponseError<>(404, "Crack not found");
        }
        newBook.setCrack(crack);

        // Check if a book with the same crack ID already exists
        if (bookRepository.existsByCrackId(crack.getId())) {
            log.error("Book with crack_id {} already exists", crack.getId());
            return new ResponseError<>(409, "Book with this crack_id already exists");
        }

        // Save the Book object to the database
        bookRepository.save(newBook);
        BookDto bookDto = BookDto.toDto(newBook);
        log.info("Book created successfully");
        return new ResponseData<>(200, "Book created successfully with ID: ",  bookDto);
    }


    @Override
    public ResponseData<Void> deleteBook ( Long bookId){
        log.info("Retrieving delete book with ID= "+bookId);
        // LAY SACH VOI ID
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) return new ResponseError<>(400,"Book not found");
        // XOA SACH
        bookRepository.delete(book);

        log.info("Book deleted successfully");
        return new ResponseData<>(200, "Book deleted successfully", null);
    }

    @Override
    public ResponseData<Void> updateBook(BookForm form, Long bookId) {
        log.info("Updating book with ID= " + bookId);

        // Find the book by ID
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) {
            log.error("Book not found with ID: {}", bookId);
            return new ResponseError<>(400, "Book not found");
        }

        book.setBookName(form.getBookName());
        book.setQuantity(form.getQuantity());
        book.setPublisher(form.getPublisher());
        book.setPostingDate(form.getPostingDate());

        // Kiem tra tac giả , k có thì tạo
        Author author = authorRepository.findByName(form.getAuthorName()).orElse(null);
        if (author == null) {
            log.info("Author not found, creating new author: {}", form.getAuthorName());
            author = new Author();
            author.setName(form.getAuthorName());
            authorRepository.save(author); // Save new author to the repository
        }
        book.setAuthor(author);

        // Kiểm tra danh mục, không có thì tạo
        Category category = categoryRepository.findById(form.getCategoryId()).orElse(null);
        if (category == null) {
            log.error("Category not found for ID: {}", form.getCategoryId());
            return new ResponseError<>(404, "Category not found");
        }
        book.setCategory(category);

        // Kiểm tra vị trí sách
        Crack crack = crackRepository.findById(form.getCrackId()).orElse(null);
        if (crack == null) {
            log.error("Crack not found for ID: {}", form.getCrackId());
            return new ResponseError<>(404, "Crack not found");}

        // vị trí đặt sách đã được đặt sách chưa
        if (bookRepository.existsByCrackId(form.getCrackId())) {
            log.error("Another book with crack_id {} already exists", crack.getId());
            return new ResponseError<>(409, "Vị trí đặt sách này đã có sách khác");
        }
        book.setCrack(crack);

        // Save the updated book to the repository
        bookRepository.save(book);
        log.info("Book updated successfully");
        return new ResponseData<>(200, "Book updated successfully", null);
    }


}
