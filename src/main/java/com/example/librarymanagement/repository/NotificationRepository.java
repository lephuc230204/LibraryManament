package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

        Optional<Notification> findById(Long notificationId);
        boolean existsById(Long id);

        List<Notification> findByUserId(Long userId);

}
