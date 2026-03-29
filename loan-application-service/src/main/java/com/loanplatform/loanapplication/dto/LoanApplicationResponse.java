package com.loanplatform.loanapplication.dto;

import com.loanplatform.loanapplication.enums.ApplicationStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationResponse {

    private UUID applicationId;
    private String applicantName;
    private String email;
    private String loanType;
    private BigDecimal requestedAmount;
    private Integer tenureInMonths;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
    private String message;
}