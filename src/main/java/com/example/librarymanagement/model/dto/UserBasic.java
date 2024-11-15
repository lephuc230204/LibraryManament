package com.example.librarymanagement.model.dto;

import com.example.librarymanagement.model.entity.CardLibrary;
import com.example.librarymanagement.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasic {
    private String username;
    private String email;
    private String phone;
    private String address;
    private String profilePicture;
    private LocalDate createdDate;
    private LocalDate dob;
    private String cardLibrary;

    public static UserBasic to(User user) {
        return UserBasic.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .phone(user.getPhone())
                .createdDate(user.getCreatedDate())
                .cardLibrary(user.getCardLibrary().getCardNumber())
                .dob(user.getDob())
                .build();
    }

}
