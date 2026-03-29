package com.loanplatform.loanapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanplatform.loanapplication.dto.LoanApplicationRequest;
import com.loanplatform.loanapplication.dto.LoanApplicationResponse;
import com.loanplatform.loanapplication.enums.ApplicationStatus;
import com.loanplatform.loanapplication.event.LoanApplicationEvent;
import com.loanplatform.loanapplication.model.LoanApplication;
import com.loanplatform.loanapplication.model.OutboxEvent;
import com.loanplatform.loanapplication.repository.LoanApplicationRepository;
import com.loanplatform.loanapplication.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public LoanApplicationResponse submitApplication(LoanApplicationRequest request) {
        LoanApplication application = LoanApplication.builder()
                .applicantName(request.getApplicantName())
                .dateOfBirth(request.getDateOfBirth())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .employmentType(request.getEmploymentType())
                .monthlyIncome(request.getMonthlyIncome())
                .designation(request.getDesignation())
                .existingLoanAmount(request.getExistingLoanAmount())
                .loanType(request.getLoanType())
                .requestedAmount(request.getRequestedAmount())
                .purpose(request.getPurpose())
                .tenureInMonths(request.getTenureInMonths())
                .status(ApplicationStatus.RECEIVED)
                .build();

        LoanApplication saved = loanApplicationRepository.save(application);
        log.info("Loan application submitted with ID: {}", saved.getApplicationId());

        LoanApplicationEvent loanApplicationEvent = LoanApplicationEvent.builder()
                .applicationId(saved.getApplicationId())
                .applicantName(saved.getApplicantName())
                .email(saved.getEmail())
                .monthlyIncome(saved.getMonthlyIncome())
                .existingLoanAmount(saved.getExistingLoanAmount())
                .employmentType(saved.getEmploymentType())
                .loanType(saved.getLoanType())
                .requestedAmount(saved.getRequestedAmount())
                .tenureInMonths(saved.getTenureInMonths())
                .status(saved.getStatus().name())
                .build();

        String loanAppEvent;

        try {
            loanAppEvent = objectMapper.writeValueAsString(loanApplicationEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize loan application event", e);
        }

        OutboxEvent outboxEvent = OutboxEvent.builder().eventType("LOAN_APPLICATION_SUBMITTED")
                .aggregateId(saved.getApplicationId())
                .payload(loanAppEvent)
                .status("PENDING")
                .build();

        OutboxEvent savedOutboxEvent = outboxEventRepository.save(outboxEvent);
        log.info("savedOutboxEvent with status: {}", savedOutboxEvent.getStatus());

        return LoanApplicationResponse.builder()
                .applicationId(saved.getApplicationId())
                .applicantName(saved.getApplicantName())
                .email(saved.getEmail())
                .loanType(saved.getLoanType())
                .requestedAmount(saved.getRequestedAmount())
                .tenureInMonths(saved.getTenureInMonths())
                .status(saved.getStatus())
                .createdAt(saved.getCreatedAt())
                .message("Loan application submitted successfully")
                .build();
    }
}
