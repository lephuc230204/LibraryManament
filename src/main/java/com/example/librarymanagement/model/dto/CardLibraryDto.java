package com.example.librarymanagement.model.dto;

import com.example.librarymanagement.model.entity.CardLibrary;
import com.example.librarymanagement.model.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CardLibraryDto {

    private String cardNumber;
    private String userName;
    private LocalDate issued;
    private LocalDate expired;

    public static CardLibraryDto toDto(CardLibrary cardLibrary) {
        return  CardLibraryDto.builder()
                .cardNumber(cardLibrary.getCardNumber())
                .userName(cardLibrary.getUser().getUsername())
                .issued(cardLibrary.getIssued())
                .expired(cardLibrary.getExpired())
                .build();
    }

}
