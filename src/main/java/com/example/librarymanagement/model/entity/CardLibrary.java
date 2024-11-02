package com.example.librarymanagement.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table( name = "card_library")
public class CardLibrary {
    @Id
    private String cardNumber;

    @OneToOne
    @JoinColumn( name = "user_id")
    private User user;
    @JsonFormat( pattern = "dd-MM-yyyy")
    private LocalDate issued;
    @Enumerated(EnumType.STRING)
    private Status status;
    public enum Status{
        ACTIVE,INACTIVE
    }

    @PrePersist
    public void prePersist() {
        if (cardNumber == null) { cardNumber = UUID.randomUUID().toString().substring(0,10);}
    }
}
