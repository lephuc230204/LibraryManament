package com.example.librarymanagement.model.dto;

import com.example.librarymanagement.model.entity.Notification;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDto {
    Long id;
    String title;
    String content;

    public static NotificationDto toDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .build();
    }
}
