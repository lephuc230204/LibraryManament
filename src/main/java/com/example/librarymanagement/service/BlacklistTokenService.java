package com.example.librarymanagement.service;

import java.time.LocalDateTime;

public interface BlacklistTokenService {
    void addTokenToBlacklist(String token, LocalDateTime expiryDate); // Change LocalDate to LocalDateTime
    boolean isTokenBlacklisted(String token);
}
