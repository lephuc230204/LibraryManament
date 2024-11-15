package com.example.librarymanagement.service;

import com.example.librarymanagement.model.dto.BookLendingDto;
import com.example.librarymanagement.model.dto.RequestRenewalDto;
import com.example.librarymanagement.model.entity.RequestRenewal;
import com.example.librarymanagement.payload.request.RequestRenewalForm;
import com.example.librarymanagement.payload.response.ResponseData;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

public interface RequestRenewalService {
    ResponseData<RequestRenewalDto> creat(Principal principal, RequestRenewalForm form);
    ResponseData<List<RequestRenewalDto>> getAllRequestRenewal();
    ResponseData<List<RequestRenewalDto>> getMyRequestRenewal(Principal principal);
    ResponseData<BookLendingDto> reply(Long requestRenewalId, String reply);
    ResponseData<RequestRenewalDto> getRequestRenewalById(Long id);
    ResponseData<String> deleteRequestRenewalById(Long id);

}
