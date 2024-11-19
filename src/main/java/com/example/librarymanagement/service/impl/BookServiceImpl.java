package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.model.dto.BookDto;
import com.example.librarymanagement.model.dto.UserDto;
import com.example.librarymanagement.model.entity.*;
import com.example.librarymanagement.payload.request.BookForm;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.payload.response.ResponseError;
import com.example.librarymanagement.repository.*;
import com.example.librarymanagement.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.LocalDate;

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
    private CrackRepository crackRepository;
    @Autowired
    private BookReservationRepository bookReservationRepository;
    @Autowired
    private BookLendingRepository bookLendingRepository;

    @Override
    public ResponseData<Page<BookDto>> getAll(int n, int size) {
        log.info("Retrieving list of books");
        Pageable pageable = PageRequest.of(n,size, Sort.by(Sort.Order.asc("bookName")));

        Page<Book> pageBook = bookRepository.findAll(pageable);
        Page<BookDto> data = pageBook.map(BookDto::toDto);

        log.info("Get books successfully");
        return new ResponseData<>(200, "Get books successfully", data);
    }
    @Override
    public ResponseData<BookDto> getBookById(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            log.error("Book not found with ID: {}", id);
            return new ResponseError<>(400, "Book not found");
        }
        return new ResponseData<>(200, "Get book successfully", BookDto.toDto(book));
    }
    @Override
    public ResponseData<BookDto> create(BookForm form) {
        log.info("Creating book");
        Book newBook = new Book();
        newBook.setBookName(form.getBookName());
        newBook.setQuantity(form.getQuantity());
        newBook.setCurrentQuantity(form.getQuantity());
        newBook.setPublisher(form.getPublisher());
        newBook.setPostingDate(LocalDate.now());

        // Check or create Category
        Category category = checkOrCreateCategory(form.getCategoryName());
        newBook.setCategory(category);

        // Check or create Author
        Author author = checkOrCreateAuthor(form.getAuthorName());
        newBook.setAuthor(author);

        // Check Crack by ID
        Crack crack = crackRepository.findById(form.getCrackId()).orElse(null);
        if (crack == null) {
            log.error("Crack not found for ID: {}", form.getCrackId());
            return new ResponseError<>(404, "Crack not found");
        }
        // Check if a book with the Crack ID already exists
        if (bookRepository.existsByCrackId(crack.getId())) {
            log.error("Book with crack_id {} already exists", crack.getId());
            return new ResponseError<>(409, "Book with this crack_id already exists");
        }
        newBook.setCrack(crack);
        // Save the book without an image to get the generated ID
        bookRepository.save(newBook);

        // Process and save image
        if (form.getImage() != null && !form.getImage().isEmpty()) {
            log.info("Image received: {}", form.getImage().getOriginalFilename());
            try {
                Path uploadPath = Paths.get(System.getProperty("user.dir") + "/public/uploads");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                    log.info("Directory created: {}",System.getProperty("user.dir") + "/public/uploads");
                }

                // Use the book's ID as the image filename
                String fileName = newBook.getBookId().toString()
                        + form.getImage().getOriginalFilename().substring(form.getImage().getOriginalFilename().lastIndexOf("."));
                Path filePath = uploadPath.resolve(fileName);
                form.getImage().transferTo(filePath.toFile());

                newBook.setImage(fileName);
                bookRepository.save(newBook); // Update book with image name
            } catch (IOException e) {
                log.error("Failed to upload image: {}", e.getMessage());
                return new ResponseError<>(500, "Failed to upload image");
            }
        } else {
            log.warn("No image provided in the form.");
        }

        BookDto bookDto = BookDto.toDto(newBook);
        log.info("Book created successfully with ID: {}", newBook.getBookId());
        return new ResponseData<>(200, "Book created successfully with ID: " + newBook.getBookId(), bookDto);
    }

    @Override
    public ResponseData<Void> updateBook(BookForm form, Long bookId) {
        log.info("Updating book with ID= " + bookId);

        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) {
            log.error("Book not found with ID: {}", bookId);
            return new ResponseError<>(400, "Book not found");
        }

        book.setBookName(form.getBookName());
        book.setQuantity(form.getQuantity());
        book.setCurrentQuantity(form.getQuantity());
        book.setPublisher(form.getPublisher());
        book.setPostingDate(LocalDate.now());

        Author author = checkOrCreateAuthor(form.getAuthorName());
        book.setAuthor(author);

        Category category = checkOrCreateCategory(form.getCategoryName());
        book.setCategory(category);

        Crack crack = crackRepository.findById(form.getCrackId()).orElse(null);
        if (crack == null) {
            log.error("Crack not found for ID: {}", form.getCrackId());
            return new ResponseError<>(404, "Crack not found");
        }
        if ((book.getCrack() == null || book.getCrack().getId() != form.getCrackId()) && bookRepository.existsByCrackId(form.getCrackId())) {
            log.error("Another book with crack_id {} already exists", form.getCrackId());
            return new ResponseError<>(409, "Vị trí đặt sách này đã có sách khác");
        }
        book.setCrack(crack);

        if (form.getImage() != null && !form.getImage().isEmpty()) {
            log.info("Image received: {}", form.getImage().getOriginalFilename());
            try {
                String uploadDir = System.getProperty("user.dir") + "/public/uploads";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                    log.info("Directory created: {}", uploadDir);
                }

                // Use the book ID as the image filename
                String fileName = bookId.toString() +
                        form.getImage().getOriginalFilename().substring(form.getImage().getOriginalFilename().indexOf("."));
                Path filePath = uploadPath.resolve(fileName);
                form.getImage().transferTo(filePath.toFile());

                book.setImage(fileName);
            } catch (IOException e) {
                log.error("Failed to upload image: {}", e.getMessage());
                return new ResponseError<>(500, "Failed to upload image");
            }
        } else {
            log.warn("No image provided in the form.");
        }

        bookRepository.save(book);
        log.info("Book updated successfully");
        return new ResponseData<>(200, "Book updated successfully", null);
    }


    @Override
    public ResponseData<Void> deleteBook(Long bookId) {
        log.info("Retrieving delete book with ID= " + bookId);
        // LAY SACH VOI ID
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) return new ResponseError<>(400, "Book not found");

        //check booklending
        BookLending currentBookLending = bookLendingRepository.findBookLendingByBookAndReturnDateIsNull(book).orElse(null);
        if (currentBookLending != null){
            log.error("Books are being borrowed and cannot be deleted");
            return new ResponseError<>(400,"Books are being borrowed and cannot be deleted");
        }
        //check book reservation
        BookReservation currentReservation = bookReservationRepository.findBookReservationByBook(book).orElse(null);
        if (currentReservation != null){
            log.error("Books are being Reservation and cannot be deleted");
            return new ResponseError<>(400,"Books are being Reservation and cannot be deleted");
        }
        // XOA SACH
        bookRepository.delete(book);

        log.info("Book deleted successfully");
        return new ResponseData<>(200, "Book deleted successfully", null);
    }

    @Override
    public Author checkOrCreateAuthor(String authorName) {
        Author author = authorRepository.findByName(authorName).orElse(null);
        if (author == null) {
            author = new Author();
            author.setName(authorName);
            authorRepository.save(author);
        }
        return author;
    }

    @Override
    public Category checkOrCreateCategory(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName).orElse(null);
        if (category == null) {
            category = new Category();
            category.setCategoryName(categoryName);
            categoryRepository.save(category);
        }
        return category;
    }

}
