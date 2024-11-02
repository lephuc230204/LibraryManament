package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.exception.InvalidRefreshTokenException;
import com.example.librarymanagement.model.dto.AuthDto;
import com.example.librarymanagement.model.dto.UserDto;
import com.example.librarymanagement.model.entity.Role;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.payload.request.SignInForm;
import com.example.librarymanagement.payload.request.SignUpForm;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.payload.response.ResponseError;
import com.example.librarymanagement.repository.RoleRepository;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.security.JwtTokenProvider;
import com.example.librarymanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Log4j2

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public AuthDto login(SignInForm form) {
        // Kiểm tra xem người dùng có tồn tại không
        User user = userRepository.findByEmail(form.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + form.getEmail()));

        if (!user.getStatus().equals("ACTIVE")) {
            throw new IllegalArgumentException("Account is not active");
        }
        // Thực hiện xác thực
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(form.getEmail(), form.getPassword())
            );
        } catch (AuthenticationException e) {
            log.error("Authentication failed for email: {} with exception: {}", form.getEmail(), e.getMessage());
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Nếu xác thực thành công
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        log.info("User {} logged in successfully with ", user.getEmail());

        // Xác định trạng thái và thông điệp kết quả
        String status = "success";
        String result = "Login successful";

        return AuthDto.from(user, accessToken, refreshToken, status, result);
    }

    @Override
    public ResponseData<UserDto> register(SignUpForm form) {
        log.info("User registration is in progress");
        log.info("Checking email exist");
        User oldUser = userRepository.findByEmail(form.getEmail()).orElse(null);
        if (oldUser != null) {return new ResponseError<>(400,"User is exits");};

        log.info("Checking confirm password");
        if ( !form.getConfirmPassword().equals(form.getPassword()) ){ return new ResponseError<>(400,"Passwords do not match");}

        Role role = roleRepository.findByName(form.getRole()).orElse(null);
        if (role == null ){return new ResponseError<>(400,"Role not found");}
        User newUser = User.builder()
                .username(form.getUsername())
                .email(form.getEmail())
                .phone(form.getPhone())
                .dob(form.getDob())
                .password(passwordEncoder.encode(form.getPassword()))
                .role(role)
                .createdDate(LocalDate.now())
                .status("ACTIVE")
                .build();

        userRepository.save(newUser);
        UserDto data = UserDto.to(newUser);
        return new ResponseData<>(200,"",data);
    }




    @Override
    public AuthDto refreshJWT(String refreshToken) {
        if (refreshToken != null) {
            refreshToken = refreshToken.replaceFirst("Bearer ", "");
            if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
                Authentication auth = jwtTokenProvider.createAuthentication(refreshToken);

                User user = userRepository.findByEmail(auth.getName())
                        .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + auth.getName()));

                log.info("User {} refreshed token", user.getUsername());

                // Xác định trạng thái và thông điệp kết quả
                String status = "success";
                String result = "Token refreshed successfully";

                return AuthDto.from(user, jwtTokenProvider.generateAccessToken(auth), refreshToken, status, result);
            }
        }
        throw new InvalidRefreshTokenException(refreshToken);
    }

    @Override
    public String confirmUser(long userId, String otpCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Check if the OTP matches
        if (!otpCode.equals(user.getOtpCode())) {
            log.error("OTP does not match for userId={}", userId);
            throw new IllegalArgumentException("OTP is incorrect");
        }

        user.setStatus("ACTIVE");
        user.setCreatedDate(LocalDate.now());
        userRepository.save(user);
        return "Confirmed!";
    }

}