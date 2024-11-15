package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.dto.NotificationDto;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("api/v1/users/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/me")
    public ResponseEntity<ResponseData<List<NotificationDto>>> getMyNotifications(Principal principal) {
        return ResponseEntity.ok(notificationService.getMyNotifications(principal));
    }
}
