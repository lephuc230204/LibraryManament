package com.example.librarymanagement.model.dto;

import com.example.librarymanagement.model.entity.User;
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
    private String address;
    private String profilePicture;
    private String roleName;
    private LocalDate createdDate;
    private LocalDate dob;
    private String status;

    public static UserDto to(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .phone(user.getPhone())
                .address(user.getAddress().toString())
                .profilePicture(user.getProfilePicture())
                .roleName(user.getRole() != null ? user.getRole().getName() : null)
                .createdDate(user.getCreatedDate())
                .dob(user.getDob())
                .status(user.getStatus())
                .build();
    }


}
