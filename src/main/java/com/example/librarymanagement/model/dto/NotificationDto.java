package com.example.librarymanagement.model.dto;

import com.example.librarymanagement.model.entity.Notification;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDto {
    String title;
    String content;

    public static NotificationDto toDto(Notification notification) {
        return NotificationDto.builder()
                .title(notification.getTitle())
                .content(notification.getContent())
                .build();
    }
}
