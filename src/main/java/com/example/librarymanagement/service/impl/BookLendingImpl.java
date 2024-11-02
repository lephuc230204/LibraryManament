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

    @Override
    public ResponseData<BookLendingDto> create(BookLendingForm form, Principal principal) {

        User Userstaff = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + principal.getName()));
        log.info("Retrieving book with ID: {}", form.getBookid());

        Optional<Book> book  = bookRepository.findById(form.getBookid());
        if (!book.isPresent()) {
            log.error("Author not found for ID: {}", form.getUserid());
            return new ResponseError<>(404, "Book not found");
        }

        if (book.get().getQuantity() == 0){
            log.error("Book is out off stock ");
            return new ResponseError<>(404, "Book is out off stock");
        }
        Book Book = book.get();
        Book.setQuantity(Book.getQuantity() - 1);
        bookRepository.save(Book);

        Optional<User> User = userRepository.findById(form.getUserid());
        if (User == null) {
            log.error("Author not found for ID: {}", form.getUserid());
            return new ResponseError<>(404, "User not found");
        }

        // Tạo đối tượng BookLending mới
        BookLending newBookLending = new BookLending();
        newBookLending.setBook(Book);
        newBookLending.setCreationDate(LocalDate.now());
        newBookLending.setDueDate(form.getDueDate());
        newBookLending.setReturnDate(form.getReturnDate());
        newBookLending.setUser(User.get());
        newBookLending.setStaff(Userstaff);

        // Lưu BookLending mới vào cơ sở dữ liệu
        bookLendingRepository.save(newBookLending);
        BookLendingDto data = BookLendingDto.toDto(newBookLending);
        log.info("Book created successfully");
        return new ResponseData<>(200, " created successfully",data);
    }

    @Override
    public ResponseData<BookLendingDto> bookRenewal(Principal principal, LocalDate renewalDate, Long bookid) {
        User User = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + principal.getName()));

        Optional<BookLending> bookLendingOptional  = bookLendingRepository.findById(bookid);
        if(!bookLendingOptional.isPresent()) {
            log.error("Book not found for ID: {}", bookid);
            return new ResponseError<>(404, "Book not found");
        }
        BookLending bookLending = bookLendingOptional.get();
        if(bookLending.getUser().getId() != User.getId()) {
            log.error("You are not the owner of the transaction");
            return new ResponseError<>(404, "You are not the owner of the transaction");
        }
        BookLending bookRenewal = bookLendingOptional.get();
        bookRenewal.setDueDate(renewalDate);
        bookLendingRepository.save(bookLending);

        log.info("BookLending updated successfully");
        return new ResponseData<>(200, " created successfully");
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
        List<BookLendingDto> listMyBookLending = bookLendingRepository.findByUser(user).stream()
                .map(BookLendingDto::toDto)
                .collect(Collectors.toList());
        return new ResponseData<>(200, "Retrieved all users successfully", listMyBookLending);

    }
}