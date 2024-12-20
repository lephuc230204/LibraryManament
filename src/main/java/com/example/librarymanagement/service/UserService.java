package com.example.librarymanagement.service;


import com.example.librarymanagement.model.dto.CardLibraryDto;
import com.example.librarymanagement.model.dto.UserBasic;
import com.example.librarymanagement.model.dto.UserDto;
import com.example.librarymanagement.payload.request.ChangePasswordForm;
import com.example.librarymanagement.payload.request.UserForm;
import com.example.librarymanagement.payload.response.ResponseData;
import org.springframework.data.domain.Page;

import java.security.Principal;

public interface UserService {
    ResponseData<Page<UserDto>> getAll(int page, int size);
    ResponseData<Page<UserDto>> searchUser(int page, int size, String query);
    ResponseData<UserDto> getById(Long id);
    ResponseData<String> changePassword(ChangePasswordForm request, Principal connectedUser);
    ResponseData<String> update(Long id, UserForm form);
    ResponseData<String> delete(Long id);
    ResponseData<String> restore(Long id);
    ResponseData<UserBasic> getMe(Principal principal);
    ResponseData<String> updateMe(Principal principal, UserForm form);
    ResponseData<CardLibraryDto> getMyCardLibrary(Principal principal);
}
