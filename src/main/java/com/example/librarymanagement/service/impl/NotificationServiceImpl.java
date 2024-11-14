package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.model.dto.BookLendingDto;
import com.example.librarymanagement.model.dto.NotificationDto;
import com.example.librarymanagement.model.entity.BookLending;
import com.example.librarymanagement.model.entity.CardLibrary;
import com.example.librarymanagement.model.entity.Notification;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.repository.*;
import com.example.librarymanagement.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;
    private BookLendingRepository bookLendingRepository;
    private UserRepository userRepository;
    private CardLibraryRepository cardLibraryRepository;


    @Override
    public ResponseData<String> createNotificationWithLendingDueDateLessTwoDay() {
        log.info("Create notification with Lending Due Date Less Than Two Days");


        // Bước 1: Lấy danh sách các BookLending có ngày hết hạn trong vòng 2 ngày
        LocalDate startDate = LocalDate.now(); // Ngày hiện tại
        LocalDate endDate = startDate.plusDays(2);
        List<BookLending> bookLendings = bookLendingRepository.findAllWithDueDateWithinNextTwoDays(startDate,endDate);

        int notificationCount = 0;

        // Bước 2: Tạo thông báo cho mỗi BookLending
        for (BookLending bookLending : bookLendings) {
            // Tạo thông báo cho mỗi BookLending
            Notification notification = new Notification();
            notification.setTitle("Sắp đến hạn trả sách ");
            notification.setContent("Sách " + bookLending.getBook().getBookName() + " bạn đang mượn phải trả trong 2 ngày tới.");
            notification.setType(Notification.NotificationType.LENDING_DUE_DATE);
            notification.setUser(bookLending.getUser());  // Set user who borrowed the book
            notification.setCreateDate(LocalDate.now());
            notification.setNotificationStatus(Notification.NotificationStatus.ACTIVE);
            // Set relatedObject to the current BookLending
            notification.setRelatedObject(bookLending);

            // Bước 3: Lưu thông báo vào database
            notificationRepository.save(notification);
            notificationCount++;  // Tăng số lượng thông báo đã tạo
        }

        // Bước 4: Trả về số lượng thông báo đã tạo
        return new ResponseData<>(200, "Created " + notificationCount + " notifications.");
    }

    @Override
    public ResponseData<List<NotificationDto>> getMyNotifications(Principal principal) {
        log.info("Get My Notifications");

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: "));

        List<Notification> notifications = notificationRepository.findByUserId(user.getUserId());

        List<NotificationDto> listMyNotification = notifications.stream()
                .map(NotificationDto::toDto)
                .collect(Collectors.toList());

        return new ResponseData<>(200, "Get My Notifications", listMyNotification);
    }

    @Override
    public ResponseData<String> createNotificationWithCardLibraryExpiredLessTwoDay() {
        log.info("Create notification with Lending Due Date Less Than Two Days");

        // Lấy danh sách các BookLending có ngày hết hạn trong vòng 2 ngày
        LocalDate startDate = LocalDate.now(); // Ngày hiện tại
        LocalDate endDate = startDate.plusDays(2);
        List<CardLibrary> cardLibraries = cardLibraryRepository.findAllWithExpiredWithinNextTwoDays(startDate,endDate);

        int notificationCount = 0;

        // Tạo thông báo cho mỗi BookLending
        for (CardLibrary cardLibrary : cardLibraries) {
            // Tạo thông báo cho mỗi BookLending
            Notification notification = new Notification();
            notification.setTitle("Thẻ thư viện sắp hết hạn");
            notification.setContent("Thẻ số : " + cardLibrary.getCardNumber() + " chỉ còn hạn sử dụng trong 2 ngày ");
            notification.setType(Notification.NotificationType.LENDING_DUE_DATE);
            notification.setUser(cardLibrary.getUser());  // Set user who borrowed the book
            notification.setCreateDate(LocalDate.now());
            notification.setNotificationStatus(Notification.NotificationStatus.ACTIVE);

            // Set relatedObject to the current BookLending
            notification.setRelatedObject(cardLibrary);

            // Lưu thông báo vào database
            notificationRepository.save(notification);
            notificationCount++;  // Tăng số lượng thông báo đã tạo
        }

        //  Trả về số lượng thông báo đã tạo
        return new ResponseData<>(200, "Created " + notificationCount + " notifications.");

    }


}
