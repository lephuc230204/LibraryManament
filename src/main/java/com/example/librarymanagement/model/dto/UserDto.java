package com.example.librarymanagement.model.dto;

import com.example.librarymanagement.model.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String roleName;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdDate;
    @JsonFormat( pattern = "dd-MM-yyyy")
    private LocalDate dob;
    private String status;
    private String cardLibrary;

    public static UserDto to(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .phone(user.getPhone())
                .roleName(user.getRole() != null ? user.getRole().getName() : null)
                .createdDate(user.getCreatedDate())
                .cardLibrary(user.getCardLibrary().getCardNumber())
                .dob(user.getDob())
                .status(user.getStatus())
                .build();
    }


}
