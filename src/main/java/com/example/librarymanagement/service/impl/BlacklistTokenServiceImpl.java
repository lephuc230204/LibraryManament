package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.model.entity.BlacklistToken;
import com.example.librarymanagement.repository.BlacklistTokenRepository;
import com.example.librarymanagement.service.BlacklistTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BlacklistTokenServiceImpl implements BlacklistTokenService {

    @Autowired
    private BlacklistTokenRepository blacklistTokenRepository;

    @Override
    public void addTokenToBlacklist(String token, LocalDateTime expiryDate) { // Update the parameter type
        BlacklistToken blacklistToken = new BlacklistToken(token, expiryDate);
        blacklistTokenRepository.save(blacklistToken);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return blacklistTokenRepository.existsByToken(token);
    }
}
