package com.example.librarymanagement.controller;

import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/admin")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/notification/lending/create")
    public ResponseEntity<ResponseData<String>> creatNotificationBookLending() {
        return ResponseEntity.ok(notificationService.createNotificationWithLendingDueDateLessTwoDay());
    }
}
