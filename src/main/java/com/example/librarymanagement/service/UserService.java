package com.example.librarymanagement.service;


import com.example.librarymanagement.model.dto.UserBasic;
import com.example.librarymanagement.model.dto.UserDto;
import com.example.librarymanagement.payload.request.ChangePasswordForm;
import com.example.librarymanagement.payload.request.RegisterForm;
import com.example.librarymanagement.payload.request.StatusUserForm;
import com.example.librarymanagement.payload.request.UserForm;
import com.example.librarymanagement.payload.response.ResponseData;

import java.security.Principal;
import java.util.List;

public interface UserService {
    ResponseData<List<UserDto>> getAll();
    ResponseData<List<UserDto>> searchUser(String query);
    ResponseData<UserDto> getById(Long id);
    ResponseData<String> changePassword(ChangePasswordForm request, Principal connectedUser);
    ResponseData<String> update(Long id, UserForm form);
    ResponseData<String> delete(Long id);
    ResponseData<UserBasic> getMe(Principal principal);
    ResponseData<String> updateMe(Principal principal, UserForm form);

    // CAP NHAT TRANG THAI NGUOI DUNG AVTICE-NONACTIVE
    ResponseData<Void> restoreUser(Long userId, StatusUserForm form);
    ResponseData<UserDto> register(RegisterForm form);
}
