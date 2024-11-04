package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.dto.BookLendingDto;
import com.example.librarymanagement.model.dto.RequestRenewalDto;
import com.example.librarymanagement.model.entity.RequestRenewal;
import com.example.librarymanagement.payload.request.RequestRenewalForm;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.service.RequestRenewalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/users/request-renewal")
public class RequestRenewalController {
    private final RequestRenewalService requestRenewalService;


    @PostMapping("/creat")
    public ResponseEntity creat(Principal principal,@RequestBody RequestRenewalForm form) {
        return ResponseEntity.ok(requestRenewalService.creat(principal, form));
    }

    @GetMapping("/getall")
    public ResponseEntity getAllRequestRenewal() {
        return ResponseEntity.ok(requestRenewalService.getAllRequestRenewal());
    }

    @GetMapping("/getme")
    public ResponseEntity getMyRequestRenewal(Principal principal) {
        return ResponseEntity.ok(requestRenewalService.getMyRequestRenewal(principal));
    }
}
