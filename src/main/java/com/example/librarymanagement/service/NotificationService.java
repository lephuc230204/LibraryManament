package com.example.librarymanagement.service;

import com.example.librarymanagement.model.dto.BookLendingDto;
import com.example.librarymanagement.model.dto.NotificationDto;
import com.example.librarymanagement.payload.response.ResponseData;

import java.security.Principal;
import java.util.List;

public interface NotificationService {
    ResponseData<String> createNotificationWithLendingDueDateLessTwoDay();
    ResponseData<List<NotificationDto>> getMyNotifications(Principal principal);
    ResponseData<String> createNotificationWithCardLibraryExpiredLessTwoDay();
}
