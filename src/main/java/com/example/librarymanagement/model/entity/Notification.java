package com.example.librarymanagement.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Transient // Đánh dấu không lưu trường này vào DB
    private Object relatedObject; // Đối tượng có thể là BookLending hoặc CardLibrary

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private LocalDate createDate;
    private NotificationStatus notificationStatus;

    public enum NotificationStatus{
        ACTIVE,
        NO_ACTIVE
    }

    public enum NotificationType {
        CARD_DUE_DATE,
        LENDING_DUE_DATE,
        RESERVATION_DONE
    }

    public void setRelatedObject(Object object) {
        if (type == NotificationType.CARD_DUE_DATE && object instanceof CardLibrary) {
            this.relatedObject = object;
        } else if (type == NotificationType.LENDING_DUE_DATE && object instanceof BookLending) {
            this.relatedObject = object;
        } else if (type == NotificationType.RESERVATION_DONE && object instanceof BookReservation) {
            this.relatedObject = object;
        } else {
            throw new IllegalArgumentException("Invalid object type for the given NotificationType");
        }
    }
}
