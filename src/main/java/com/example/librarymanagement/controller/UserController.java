package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.dto.CardLibraryDto;
import com.example.librarymanagement.model.dto.UserBasic;
import com.example.librarymanagement.payload.request.ChangePasswordForm;
import com.example.librarymanagement.payload.request.UserForm;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    // Cập nhật thông tin cá nhân
    @PutMapping("/update/me")
    public ResponseEntity<ResponseData<String>> updateCurrentUser(@RequestBody UserForm form, Principal principal) {return ResponseEntity.ok(userService.updateMe(principal, form));
    }

    // Cập nhật mật khẩu
    @PatchMapping("/change-password")
    public ResponseEntity<ResponseData<String> > changePassword(@RequestBody ChangePasswordForm form, Principal principal) {return ResponseEntity.ok(userService.changePassword(form, principal));
    }

    // Lấy thông tin người dùng hiện tại
    @GetMapping("/me")
    public ResponseEntity<ResponseData<UserBasic> > getCurrentUser(Principal principal) {return ResponseEntity.ok(userService.getMe(principal));
    }
    // lay card library
    @GetMapping("/card-library/me")
    public ResponseEntity<ResponseData<CardLibraryDto>> getMyCardLibrary(Principal principal) {
        return ResponseEntity.ok(userService.getMyCardLibrary(principal));
    }
}
