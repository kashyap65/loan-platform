package com.loanplatform.loanapplication.service;

import com.loanplatform.loanapplication.dto.LoanApplicationRequest;
import com.loanplatform.loanapplication.dto.LoanApplicationResponse;
import com.loanplatform.loanapplication.enums.ApplicationStatus;
import com.loanplatform.loanapplication.model.LoanApplication;
import com.loanplatform.loanapplication.repository.LoanApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;

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
