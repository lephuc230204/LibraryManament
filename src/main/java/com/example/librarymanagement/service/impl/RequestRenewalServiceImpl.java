package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.model.dto.BookLendingDto;
import com.example.librarymanagement.model.dto.BookReservationDto;
import com.example.librarymanagement.model.dto.RequestRenewalDto;
import com.example.librarymanagement.model.entity.*;
import com.example.librarymanagement.payload.request.RequestRenewalForm;
import com.example.librarymanagement.payload.response.ResponseData;
import com.example.librarymanagement.payload.response.ResponseError;
import com.example.librarymanagement.repository.BookLendingRepository;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.RequestRenewalRepository;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.service.BookLendingService;
import com.example.librarymanagement.service.RequestRenewalService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class RequestRenewalServiceImpl implements RequestRenewalService {

    private final BookLendingRepository bookLendingRepository;
    private final RequestRenewalRepository requestRenewalRepository;
    private final UserRepository userRepository;
    private final BookLendingService bookLendingService;
    private final BookRepository bookRepository;

    @Override
    public ResponseData<RequestRenewalDto> creat(Principal principal,RequestRenewalForm form) {
        if (form.getBookLendingId() == null){
            log.error("lending id null");
        }
        if (form.getRenewalDate() == null){
            log.error("renewal date null");
        }
        if (form.getDescription() == null){
            log.error("description null");
        }

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + principal.getName()));

        log.info("RequestRenewal creat");

        Optional<BookLending> bookLendingOptional  = bookLendingRepository.findById(form.getBookLendingId());
        if(!bookLendingOptional.isPresent()) {
            log.error("BookLending not found for ID: {}", form.getBookLendingId());
            return new ResponseError<>(404, "BookLending not found");
        }

        if ( user.getId() != bookLendingOptional.get().getUser().getId()) {
            log.error("User id is not the same");
            return new ResponseError<>(403, "This is not your bookLending");
        }
        BookLending bookLending = bookLendingOptional.get();

        RequestRenewal requestRenewal = new RequestRenewal();

        requestRenewal.setRenewalDate(form.getRenewalDate());
        requestRenewal.setBookLending(bookLending);
        requestRenewal.setUserId(bookLending.getUser().getUserId());
        requestRenewal.setStatus(RequestRenewal.RequestRenewalStatus.PENDING);

        requestRenewalRepository.save(requestRenewal);
        RequestRenewalDto data = RequestRenewalDto.toDto(requestRenewal);
        log.info("Book created successfully");
        return new ResponseData<>(200, " created successfully",data);
    }



    @Override
    public ResponseData<List<RequestRenewalDto>> getAllRequestRenewal() {
        log.info("RequestRenewal getAllRequestRenewal");
        List<RequestRenewalDto> requestRenewalDtoList = requestRenewalRepository.findAll().stream()
                .map(RequestRenewalDto::toDto)
                .collect(Collectors.toList());
        return new ResponseData<>(200, "Get all requests successfully", requestRenewalDtoList);

    }

    @Override
    public ResponseData<List<RequestRenewalDto>> getMyRequestRenewal(Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + principal.getName()));
        log.info("get request with ID: {}", user.getUserId());

        List<RequestRenewal> requestRenewal = requestRenewalRepository.findByUserId(user.getId());

        List<RequestRenewalDto> data = requestRenewal.stream()
                .map(RequestRenewalDto::toDto)
                .collect(Collectors.toList());
        return new ResponseData<>(200, "Get all requests successfully", data);

    }
    @Override
    public ResponseData<BookLendingDto> reply(Long requestRenewalId,String reply) {
        log.info(" employee reply request bookRenewal  ");

        Optional<RequestRenewal> requestRenewalOptional = requestRenewalRepository.findById(requestRenewalId);
        if(!requestRenewalOptional.isPresent()) {
            log.error("RequestRenewal not found for ID: {}", requestRenewalId);
            return new ResponseError<>(404, "RequestRenewal not found");
        }
        RequestRenewal requestRenewal = requestRenewalOptional.get();
        if ("APPROVED".equals(reply)) {
            requestRenewal.setStatus(RequestRenewal.RequestRenewalStatus.APPROVED);
            log.info("bookRenewal status changed to APPROVED");
        } else {
            requestRenewal.setStatus(RequestRenewal.RequestRenewalStatus.REJECTED);
            log.info("bookRenewal status changed to REJECTED");
        }

        requestRenewalRepository.save(requestRenewal);

        BookLending bookRenewal = requestRenewal.getBookLending();
        bookRenewal.setDueDate(requestRenewal.getRenewalDate());
        bookLendingRepository.save(bookRenewal);

        log.info("BookLending updated successfully");
        return new ResponseData<>(200, " update successfully");
    }
}
