package com.example.librarymanagement.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpForm {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 11, message = "Phone number must be 11 digits")
    @Pattern(regexp = "^\\d{11}$", message = "Phone number must be numeric and exactly 11 digits")
    private String phone;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    @NotBlank(message = "Role is required")
    private String role;
}
