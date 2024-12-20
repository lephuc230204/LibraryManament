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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .orElseThrow(() -> new IllegalArgumentException("staff not found with email: " + principal.getName()));
        log.info("Retrieving book with ID: {}", form.getBookId());

        User user = userRepository.findByEmail(form.getEmail()).orElse(null);
        if(user == null) {
            log.error("User not found for ID: {}", form.getEmail());
            return new ResponseError<>(404, "User not found");
        }

        if (user.getCardLibrary() == null || user.getCardLibrary().getExpired().isBefore(LocalDate.now())) {
            log.error("Card expired or not found");
            return new ResponseError<>(404, "Card expired or not found");
        }

        Book book = bookRepository.findById(form.getBookId()).orElse(null);
        log.error("Book not found for ID: " + form.getBookId());
        if (book == null){
            return new ResponseError<>(404,"Book not found with ID: "+form.getBookId());
        }
        boolean alreadyBorrowed = bookLendingRepository.existsByUser_UserIdAndBook_BookIdAndReturnDateIsNull(user.getUserId(), book.getBookId());
        if (alreadyBorrowed) {
            log.warn("User has already borrowed this book");
            return new ResponseError<>(400, "You have already borrowed this book");
        }

        if (book.getQuantity() == 0) {
            log.error("Book is out of stock");
            return new ResponseError<>(404, "Book is out of stock");
        }

        book.setCurrentQuantity(book.getCurrentQuantity() - 1);
        bookRepository.save(book);

        BookLending newBookLending = new BookLending();
        newBookLending.setBook(book);
        newBookLending.setCreationDate(LocalDate.now());
        newBookLending.setDueDate(form.getDueDate());
        newBookLending.setUser(user);
        newBookLending.setStaff(Userstaff);

        // Lưu BookLending mới vào cơ sở dữ liệu
        bookLendingRepository.save(newBookLending);
        BookLendingDto data = BookLendingDto.toDto(newBookLending);
        log.info("Book created successfully");
        return new ResponseData<>(200, "Created successfully", data);
    }

    @Override
    public ResponseData<Page<BookLendingDto>> getAllBookLending(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Order.asc("lendingId")));
        Page<BookLending> bookLendingPage = bookLendingRepository.findAll(pageable);
        Page<BookLendingDto> data = bookLendingPage.map(BookLendingDto::toDto);

        return new ResponseData<>(200, "Retrieved all users successfully",data );
    }

    @Override
    public ResponseData<List<BookLendingDto>> getMyBookLending(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: "));
        List<BookLendingDto> listMyBookLending = bookLendingRepository.findByUserAndReturnDateIsNull(user).stream()
                .map(BookLendingDto::toDto)
                .collect(Collectors.toList());
        log.info("Get My Notifications");
        return new ResponseData<>(200, "Retrieved all users successfully", listMyBookLending);

    }

    @Override
    public ResponseData<BookLendingDto> returnBook(Long bookLendingId) {

        BookLending bookLending = bookLendingRepository.findById(bookLendingId).orElse(null);
        if (bookLending == null) {
            log.error("BookLending not found for ID: {}", bookLendingId);
            return new ResponseError<>(404, "BookLending not found");
        }

        Long bookId = bookLending.getBook().getBookId();
        Optional<Book> bookoptional = bookRepository.findById(bookId);
        Book book = bookoptional.get();
        book.setCurrentQuantity(book.getCurrentQuantity() + 1);
        bookRepository.save(book);

        bookLending.setReturnDate(LocalDate.now());
        bookLendingRepository.save(bookLending);
        BookLendingDto data = BookLendingDto.toDto(bookLending);
        return new ResponseData<>(200, "Returned book successfully",data);
    }

    @Override
    public ResponseData<BookLendingDto> getBookLendingById(Long id) {
        BookLending bookLending = bookLendingRepository.findById(id).orElse(null);
        if (bookLending == null) {
            log.error("BookLending not found for ID: {}", id);
            return new ResponseError<>(404, "BookLending not found");
        }
        return new ResponseData<>(200,"Retrieved book successfully",BookLendingDto.toDto(bookLending));
    }

    @Override
    public ResponseData<String> deleteBookLendingById(Long id) {
        BookLending bookLending = bookLendingRepository.findById(id).orElse(null);
        if (bookLending == null) {
            log.error("BookLending not found for ID: {}", id);
            return new ResponseError<>(404, "BookLending not found");
        }
        if (bookLending.getReturnDate() == null){
            log.error("BookLending returnDate is null");
            return new ResponseError<>(400, "Unreturned books cannot be deleted.");
        }
        bookLendingRepository.delete(bookLending);
        return new ResponseData<>(200, "Book Lending deleted successfully");
    }

    @Override
    public ResponseData<BookLendingDto> getBookLendingByEmailBookId(String email, Long bookId) {

        User user = userRepository.findByEmail(email).orElse(null);
        if(user==null){ return new ResponseError<>(404,"User not found with email: "+email);}

        BookLending bookLending  = bookLendingRepository.findByUser_UserIdAndBook_BookIdAndReturnDateIsNull(user.getUserId(), bookId);
        if(bookLending == null){
            log.error("BookLending not found for ID: {}", bookId);
            return new ResponseError<>(404, "BookLending not found");
        }
        return new ResponseData<>(200,"get book successfully",BookLendingDto.toDto(bookLending));
    }

    @Override
    public ResponseData<BookLendingDto> updateBookLending(Long bookLendingId,BookLendingForm form) {
        BookLending bookLending  = bookLendingRepository.findById(bookLendingId).orElse(null);;

        if(bookLending==null){
            log.error("BookLending not found for ID: {}", bookLendingId);
            return new ResponseError<>(404, "BookLending not found");
        }

        if (form.getEmail() != null) {
            User user = userRepository.findByEmail(form.getEmail()).orElse(null);
            if (user == null) {
                log.error("User not found with email: {}", form.getEmail());
                return new ResponseError<>(404, "User not found with email");
            }
            bookLending.setUser(user);
        }
        if (form.getDueDate() != null){
            bookLending.setDueDate(form.getDueDate());
        }

        bookLendingRepository.save(bookLending);

        return new ResponseData<>(200, "BookLending updated successfully",BookLendingDto.toDto(bookLending));
    }
}