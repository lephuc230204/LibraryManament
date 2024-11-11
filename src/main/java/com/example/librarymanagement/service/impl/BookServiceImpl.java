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
import com.example.librarymanagement.repository.CrackRepository;
import com.example.librarymanagement.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.LocalDate;
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
    private CrackRepository crackRepository;

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

        // Tạo đối tượng Book mới và gán giá trị từ form
        Book newBook = new Book();
        newBook.setBookName(form.getBookName());
        newBook.setQuantity(form.getQuantity());
        newBook.setCurrentQuantity(form.getQuantity());
        newBook.setPublisher(form.getPublisher());
        newBook.setPostingDate(LocalDate.now());

        // Kiểm tra và tạo Category nếu chưa có
        Category category = checkOrCreateCategory(form.getCategoryName());
        newBook.setCategory(category);

        // Kiểm tra và tạo Author nếu chưa có
        Author author = checkOrCreateAuthor(form.getAuthorName());
        newBook.setAuthor(author);

        // Kiểm tra Crack theo ID
        Crack crack = crackRepository.findById(form.getCrackId()).orElse(null);
        if (crack == null) {
            log.error("Crack not found for ID: {}", form.getCrackId());
            return new ResponseError<>(404, "Crack not found");
        }

        // Kiểm tra nếu sách đã tồn tại với Crack ID này
        if (bookRepository.existsByCrackId(crack.getId())) {
            log.error("Book with crack_id {} already exists", crack.getId());
            return new ResponseError<>(409, "Book with this crack_id already exists");
        }
        newBook.setCrack(crack);

        // Xử lý upload ảnh
        if (form.getImage() != null && !form.getImage().isEmpty()) {
            log.info("Image received: {}", form.getImage().getOriginalFilename());
            try {
                // Xác định đường dẫn lưu file, sử dụng đường dẫn tuyệt đối từ thư mục gốc của dự án
                String uploadDir = System.getProperty("user.dir") + "/public/uploads";
                Path uploadPath = Paths.get(uploadDir);

                // Kiểm tra và tạo thư mục nếu chưa có
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);  // Tạo thư mục nếu chưa có
                    log.info("Directory created: {}", uploadDir);
                }

                // Đặt tên file theo tên sách hoặc tên gốc của file
                String fileName = newBook.getBookName() + "_" + form.getImage().getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                log.info("Saving image with filename: {}", fileName);

                // Lưu file vào thư mục
                form.getImage().transferTo(filePath.toFile());
                log.info("Image saved at path: {}", filePath.toString());

                // Chuyển đường dẫn file thành String và lưu vào Book entity
                newBook.setImage(fileName);
            } catch (IOException e) {
                log.error("Failed to upload image: {}", e.getMessage());
                return new ResponseError<>(500, "Failed to upload image");
            }
        } else {
            log.warn("No image provided in the form.");
        }

        // Lưu Book vào cơ sở dữ liệu
        bookRepository.save(newBook);
        BookDto bookDto = BookDto.toDto(newBook);
        log.info("Book created successfully with ID: {}", newBook.getBookId());
        return new ResponseData<>(200, "Book created successfully with ID: " + newBook.getBookId(), bookDto);
    }

    @Override
    public ResponseData<Void> deleteBook(Long bookId) {
        log.info("Retrieving delete book with ID= " + bookId);
        // LAY SACH VOI ID
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) return new ResponseError<>(400, "Book not found");
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
        book.setCurrentQuantity(form.getQuantity());
        book.setPublisher(form.getPublisher());
        book.setPostingDate(LocalDate.now());

        // Kiem tra tac giả , k có thì tạo
        Author author = checkOrCreateAuthor(form.getAuthorName());
        book.setAuthor(author);

        // Kiểm tra danh mục, không có thì tạo
        Category category = checkOrCreateCategory(form.getCategoryName());
        book.setCategory(category);

        // Kiểm tra vị trí sách
        Crack crack = crackRepository.findById(form.getCrackId()).orElse(null);
        if (crack == null) {
            log.error("Crack not found for ID: {}", form.getCrackId());
            return new ResponseError<>(404, "Crack not found");
        }

        // Chỉ kiểm tra vị trí nếu crack ID mới khác với crack ID hiện tại của sách
        if ((book.getCrack() == null || book.getCrack().getId() != form.getCrackId()) && bookRepository.existsByCrackId(form.getCrackId())) {
            log.error("Another book with crack_id {} already exists", form.getCrackId());
            return new ResponseError<>(409, "Vị trí đặt sách này đã có sách khác");
        }

        book.setCrack(crack);

        if (form.getImage() != null && !form.getImage().isEmpty()) {
            log.info("Image received: {}", form.getImage().getOriginalFilename());
            try {
                // Xác định đường dẫn lưu file, sử dụng đường dẫn tuyệt đối từ thư mục gốc của dự án
                String uploadDir = System.getProperty("user.dir") + "/public/uploads";
                Path uploadPath = Paths.get(uploadDir);

                // Kiểm tra và tạo thư mục nếu chưa có
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);  // Tạo thư mục nếu chưa có
                    log.info("Directory created: {}", uploadDir);
                }

                // Đặt tên file theo tên sách hoặc tên gốc của file
                String fileName = book.getBookName() + "_" + form.getImage().getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                log.info("Saving image with filename: {}", fileName);

                // Lưu file vào thư mục
                form.getImage().transferTo(filePath.toFile());
                log.info("Image saved at path: {}", filePath.toString());

                // Chuyển đường dẫn file thành String và lưu vào Book entity
                book.setImage(fileName);
            } catch (IOException e) {
                log.error("Failed to upload image: {}", e.getMessage());
                return new ResponseError<>(500, "Failed to upload image");
            }
        } else {
            log.warn("No image provided in the form.");
        }

        // Save the updated book to the repository
        bookRepository.save(book);
        log.info("Book updated successfully");
        return new ResponseData<>(200, "Book updated successfully", null);
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
