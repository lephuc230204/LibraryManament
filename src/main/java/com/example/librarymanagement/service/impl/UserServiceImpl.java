package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.model.dto.CardLibraryDto;
import com.example.librarymanagement.model.dto.UserBasic;
import com.example.librarymanagement.model.dto.UserDto;
import com.example.librarymanagement.model.entity.CardLibrary;
import com.example.librarymanagement.model.entity.Role;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.payload.request.ChangePasswordForm;
import com.example.librarymanagement.payload.request.UserForm;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.payload.response.ResponseError;
import com.example.librarymanagement.repository.RoleRepository;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public ResponseData<Page<UserDto>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Order.asc("userId")));

        Page<User> usersPage = userRepository.findByRoleName("ROLE_USER",pageable);
        Page<UserDto> usersDtoPage = usersPage.map(UserDto::to);

        return new ResponseData<>(200, "Retrieved users successfully", usersDtoPage);
    }
    public ResponseData<Page<UserDto>> searchUser(int page, int size, String query){
        log.info("Search user by "+query);
        Pageable pageable = PageRequest.of(page,size);

        Page<User> userPage = userRepository
                .searchUsers(query,pageable);
        Page<UserDto> userDtoPage = userPage.map(UserDto::to);

        return new ResponseData<>(200,"Retrieved search users successfully", userDtoPage);

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
        Role role = roleRepository.findByName(form.getRole())
                        .orElseThrow(()-> new RuntimeException("Role not found"));

        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setDob(form.getDob());
        user.setPhone(form.getPhone());
        user.setStatus(form.getStatus());
        user.setRole(role);

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
    public ResponseData<String> restore(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setStatus("ACTIVE");
        userRepository.save(user);
        return new ResponseData<>(200, "restore successfully");
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

    @Override
    public ResponseData<CardLibraryDto> getMyCardLibrary(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        CardLibrary cardLibrary = user.getCardLibrary();
        return new ResponseData<>(200,"get card successfully",CardLibraryDto.toDto(cardLibrary));
    }
}
