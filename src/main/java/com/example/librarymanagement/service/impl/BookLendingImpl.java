package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.model.dto.BookLendingDto;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.BookLending;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.payload.request.BookLendingForm;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.payload.response.ResponseError;
import com.example.librarymanagement.repository.BookLendingRepository;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.service.BookLendingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class BookLendingImpl implements BookLendingService {


    private final BookRepository bookRepository;
    private final BookLendingRepository bookLendingRepository;
    private final UserRepository userRepository;

    @Override// xet quan he lai bbook voi bklending
    public ResponseData<BookLendingDto> create(BookLendingForm form, Principal principal) {

        User Userstaff = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("staff not found with email: " + principal.getName()));
        log.info("Retrieving book with ID: {}", form.getBookid());

        Optional<User> User = userRepository.findByEmail(form.getUsername());
        if (User == null) {
            log.error("Author not found for ID: {}", form.getUsername());
            return new ResponseError<>(404, "User not found");
        }

        Book book  = bookRepository.findById(form.getBookid()).orElse(null);
        if (book == null) {
            log.error("book not found for ID: {}", form.getBookid());
            return new ResponseError<>(404, "Book not found");
        }

        boolean alreadyBorrowed = bookLendingRepository.existsByUser_UserIdAndBook_BookIdAndReturnDateIsNull(User.get().getUserId(), book.getBookId());
        if (alreadyBorrowed) {
            log.warn("User has already borrowed this book");
            return new ResponseError<>(400, "You have already borrowed this book");
        }

        if (book.getQuantity() == 0){
            log.error("Book is out off stock ");
            return new ResponseError<>(404, "Book is out off stock");
        }

        book.setCurrentQuantity(book.getCurrentQuantity() - 1);
        bookRepository.save(book);


        // Tạo đối tượng BookLending mới
        BookLending newBookLending = new BookLending();
        newBookLending.setBook(book);
        newBookLending.setCreationDate(LocalDate.now());
        newBookLending.setDueDate(form.getDueDate());
        newBookLending.setUser(User.get());
        newBookLending.setStaff(Userstaff);

        // Lưu BookLending mới vào cơ sở dữ liệu
        bookLendingRepository.save(newBookLending);
        BookLendingDto data = BookLendingDto.toDto(newBookLending);
        log.info("Book created successfully");
        return new ResponseData<>(200, " created successfully",data);
    }



    @Override
    public ResponseData<List<BookLendingDto>> getAllBookLending() {
        List<BookLendingDto> listBookLending = bookLendingRepository.findAll().stream()
                .map(BookLendingDto::toDto)
                .collect(Collectors.toList());
        return new ResponseData<>(200, "Retrieved all users successfully", listBookLending);
    }

    @Override
    public ResponseData<List<BookLendingDto>> getMyBookLending(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: "));
        List<BookLendingDto> listMyBookLending = bookLendingRepository.findByUserAndReturnDateIsNull(user).stream()
                .map(BookLendingDto::toDto)
                .collect(Collectors.toList());
        return new ResponseData<>(200, "Retrieved all users successfully", listMyBookLending);

    }

    @Override
    public ResponseData<BookLendingDto> returnBook(String username, Long bookid) {
        log.info("Returning book with ID: {}", bookid);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: "));

        BookLending bookLending  = bookLendingRepository.findByUser_UserIdAndBook_BookId(user.getUserId(), bookid);
        if(bookLending == null){
            log.error("BookLending not found for ID: {}", bookid);
            return new ResponseError<>(404, "BookLending not found");
        }

        Optional<Book> bookoptional = bookRepository.findById(bookid);
        Book book = bookoptional.get();
        book.setCurrentQuantity(book.getCurrentQuantity() + 1);
        bookRepository.save(book);

        bookLending.setReturnDate(LocalDate.now());
        bookLendingRepository.save(bookLending);
        BookLendingDto data = BookLendingDto.toDto(bookLending);
        return new ResponseData<>(200, "Returned book successfully",data);
    }
}