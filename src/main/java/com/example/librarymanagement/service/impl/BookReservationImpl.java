package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.model.dto.BookReservationDto;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.BookReservation;
import com.example.librarymanagement.model.entity.Notification;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.payload.request.BookReservationForm;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.payload.response.ResponseError;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.BookReservationRepository;
import com.example.librarymanagement.repository.NotificationRepository;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.service.BookReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookReservationImpl implements BookReservationService {

    private final BookReservationRepository bookReservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public ResponseData<BookReservationDto> createBookReservation(BookReservationForm bookReservationForm) {

        // Find the user by email
        User user = userRepository.findByEmail(bookReservationForm.getEmail())
                .orElse(null);

        if (user == null) {
            log.error("User not found for email: {}", bookReservationForm.getEmail());
            return new ResponseError<>(404, "User not found");
        }

        // Find the book by ID
        Book book = bookRepository.findById(bookReservationForm.getBookId()).orElse(null);
        if (book == null) {
            log.error("Book not found for ID: {}", bookReservationForm.getBookId());
            return new ResponseError<>(404, "Book not found");
        }

        // Check if the book's current quantity is zero
        if (book.getCurrentQuantity() > 0) {
            log.error("Book with ID: {} is still available for borrowing and cannot be reserved.", book.getBookId());
            return new ResponseError<>(400, "Book is still available for borrowing; reservation is allowed only when stock is zero.");
        }

        // Check if a reservation already exists for this user and book
        if (bookReservationRepository.existsByUserAndBook(user, book)) {
            log.error("Reservation already exists for User ID: {} and Book ID: {}", user.getId(), book.getBookId());
            return new ResponseError<>(400, "Reservation already exists for this book and user.");
        }

        // Create a new reservation with status PENDING
        BookReservation bookReservation = new BookReservation();
        bookReservation.setStatus(BookReservation.Status.PENDING);
        bookReservation.setCreationDate(LocalDate.now());
        bookReservation.setBook(book);
        bookReservation.setUser(user);
        bookReservationRepository.save(bookReservation);

        // Convert the reservation to a DTO
        BookReservationDto bookReservationDto = BookReservationDto.toDto(bookReservation);

        return new ResponseData<>(200, "Book reservation created successfully", bookReservationDto);
    }



    @Override
    public ResponseData<List<BookReservationDto>> getBookReservationByUserId(Principal principal) {
        // Lấy email của người dùng từ Principal
        String loggedInUserEmail = principal.getName();

        // Lấy người dùng từ cơ sở dữ liệu
        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(null);

        if(user == null){
            log.error("User not found");
            return new ResponseError<>(404, "Logged in user not found");
        }
        // Lấy danh sách đặt sách của người dùng
        List<BookReservation> bookReservations = bookReservationRepository.findByUserId(user.getId());

        // Chuyển đổi danh sách đặt sách thành danh sách DTO
        List<BookReservationDto> bookReservationDtos = bookReservations.stream()
                .map(BookReservationDto::toDto) // Giả sử bạn có phương thức chuyển đổi trong DTO
                .collect(Collectors.toList());

        // Trả về kết quả
        return new ResponseData<>(200, "Book reservations retrieved successfully", bookReservationDtos);
    }


    @Override
    public ResponseData<BookReservationDto> getBookReservationById(Long id, Principal principal) {
        String loggedInUserEmail = principal.getName();

        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(null);
        if(user == null){
            log.error("Logged in user not found");
            return new ResponseError<>(404, "Logged in user not found");
        }

        BookReservation bookReservation = bookReservationRepository.findById(id).orElse(null);

        if(bookReservation == null){
            return new ResponseError<>(404, "book Reservation not found");
        }
        // Kiểm tra nếu đặt sách thuộc về người dùng hiện tại
        if (!bookReservation.getUser().getId().equals(user.getId())) {
            return new ResponseError<>(403, "You do not have permission to access this reservation");
        }

        return new ResponseData<>(200, "Book reservations retrieved successfully", BookReservationDto.toDto(bookReservation));

    }

    @Override
    public ResponseData<BookReservationDto> cancelBookReservation(Long id, BookReservationForm bookReservationForm, Principal principal) {
        String loggedInUserEmail = principal.getName();

        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(null);

        if(user == null){
            log.error("Logged in user not found");
            return new ResponseError<>(404, "Logged in user not found");
        }

        BookReservation bookReservation = bookReservationRepository.findById(id).orElse(null);

        if(bookReservation == null){
            return new ResponseError<>(404, "book Reservation not found");
        }
        // Kiểm tra nếu đặt sách thuộc về người dùng hiện tại
        if (!bookReservation.getUser().getId().equals(user.getId())) {
            return new ResponseError<>(403, "You do not have permission to cancel this reservation");
        }

        bookReservation.setStatus(bookReservationForm.getStatus());
        bookReservationRepository.save(bookReservation);

        // Trả về kết quả
        return new ResponseData<>(200, "Book reservation cancelled successfully", BookReservationDto.toDto(bookReservation));
    }



    @Override
    public ResponseData<BookReservationDto> getBookReservationById(Long id) {

        BookReservation bookReservation = bookReservationRepository.findById(id).orElse(null);
        if (bookReservation == null) {
            log.error("Book Reservation not found for ID: {}", id);
            return new ResponseError<>(404, "Book Reservation not found");
        }
        BookReservationDto bookReservationDto = BookReservationDto.toDto(bookReservation);
        return new ResponseData<>(200, "Get bookReservation successfully", bookReservationDto);
    }

    @Override
    public ResponseData<String> deleteBookReservation(Long id) {
        BookReservation bookReservation = bookReservationRepository.findById(id).orElse(null);
        if (bookReservation == null) {
            log.error("Book Reservation not found for ID: {}", id);
            return new ResponseError<>(404, "Book Reservation not found");
        }

        bookReservationRepository.deleteById(id);
        return new ResponseData<>(200, "Book Reservation deleted successfully");
    }

    @Override
    public ResponseData<BookReservationDto> updateBookReservation(Long id, BookReservationForm bookReservationForm) {
        BookReservation bookReservation = bookReservationRepository.findById(id).orElse(null);
        if (bookReservation == null) {
            log.error("Book Reservation not found for ID: {}", id);
            return new ResponseError<>(404, "Book Reservation not found");
        }

        // Cập nhật status và ngày tạo
        bookReservation.setStatus(bookReservationForm.getStatus());
        bookReservation.setCreationDate(LocalDate.now());

        // Kiểm tra nếu status là CONFIRMED thì tạo thông báo
        Notification notification = new Notification();
        notification.setTitle("Book Reservation");

        if (bookReservation.getStatus() == BookReservation.Status.CONFIRMED) {
            notification.setContent(bookReservation.getBook().getBookName() + "is available, please come to the library to pick it up");
            notification.setType(Notification.NotificationType.RESERVATION_DONE);
        } else {
            notification.setContent(bookReservation.getBook().getBookName() + " is out of stock. We are very sorry for the inconvenience");
            notification.setType(Notification.NotificationType.RESERVATION_CANCELED);
        }

        notification.setUser(bookReservation.getUser());  // Set user who borrowed the book
        notification.setCreateDate(LocalDate.now());
        notification.setNotificationStatus(Notification.NotificationStatus.ACTIVE);
        notification.setRelatedObject(bookReservation);
        notificationRepository.save(notification);

        bookReservationRepository.save(bookReservation);

        // Chuyển đổi đối tượng BookReservation thành DTO và trả về
        BookReservationDto bookReservationDto = BookReservationDto.toDto(bookReservation);

        return new ResponseData<>(200, "Update bookReservation successfully", bookReservationDto);
    }



    @Override
    public ResponseData<Page<BookReservationDto>> getAllBookReservation(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Order.asc("reservationId")));

        Page<BookReservation> bookReservationPage = bookReservationRepository.findAll(pageable);
        Page<BookReservationDto> data = bookReservationPage.map(BookReservationDto::toDto);

        log.info("Get bookReservations successfully");
        return new ResponseData<>(200, "Get bookReservations successfully", data);
    }
}
