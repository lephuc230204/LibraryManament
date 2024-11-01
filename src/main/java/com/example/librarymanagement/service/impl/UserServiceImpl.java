package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.model.dto.UserBasic;
import com.example.librarymanagement.model.dto.UserDto;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.payload.request.ChangePasswordForm;
import com.example.librarymanagement.payload.request.UserForm;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.payload.response.ResponseError;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseData<List<UserDto>> getAll() {
        List<UserDto> listUsers = userRepository.findAll().stream()
                .map(UserDto::to)
                .collect(Collectors.toList());
        return new ResponseData<>(200, "Retrieved all users successfully", listUsers);
    }

    @Override
    public ResponseData<List<UserDto>> searchUser(String query) {
        List<User> users = userRepository.searchUsersByFullNameOrEmail(query);
        List<UserDto> userDtos = users.stream()
                .map(UserDto::to)
                .collect(Collectors.toList());
        return new ResponseData<>(200, "Search results retrieved successfully", userDtos);
    }

    @Override
    public ResponseData<String> changePassword(ChangePasswordForm request, Principal connectedUser) {
        User user = userRepository.findByEmail(connectedUser.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return new ResponseError<>(400, "Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return new ResponseError<>(400, "New password and confirm password do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return new ResponseData<>(200, "Password changed successfully");
    }

    @Override
    public ResponseData<String> update(Long id, UserForm form) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setUsername(form.getUsername());
        user.setPhone(form.getPhone());

        userRepository.save(user);
        return new ResponseData<>(200, "User updated successfully");
    }

    @Override
    public ResponseData<String> updateMe(Principal principal, UserForm form) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setUsername(form.getUsername());
        user.setPhone(form.getPhone());
        user.setDob(form.getDob());

        userRepository.save(user);
        return new ResponseData<>(200, "User updated successfully");
    }

    @Override
    public ResponseData<String> delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setStatus("DELETED");
        userRepository.save(user);
        return new ResponseData<>(200, "User marked as deleted successfully");
    }

    @Override
    public ResponseData<UserBasic> getMe(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserBasic userBasic = UserBasic.to(user);
        return new ResponseData<>(200, "User information retrieved successfully", userBasic);
    }

    @Override
    public ResponseData<UserDto> getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserDto userDto = UserDto.to(user); // Sử dụng UserDto.to(user)
        return new ResponseData<>(200, "User retrieved successfully", userDto);
    }
}
