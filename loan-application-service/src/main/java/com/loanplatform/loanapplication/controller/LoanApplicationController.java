package com.loanplatform.loanapplication.controller;

import com.loanplatform.loanapplication.dto.LoanApplicationRequest;
import com.loanplatform.loanapplication.dto.LoanApplicationResponse;
import com.loanplatform.loanapplication.service.LoanApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    @PostMapping
    public ResponseEntity<LoanApplicationResponse> submitApplication(@RequestBody @Valid LoanApplicationRequest request) {
        LoanApplicationResponse response = loanApplicationService.submitApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
