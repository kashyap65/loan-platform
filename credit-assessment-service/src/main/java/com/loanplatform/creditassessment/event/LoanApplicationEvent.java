package com.loanplatform.creditassessment.event;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationEvent {

    private UUID applicationId;
    private String applicantName;
    private String email;
    private BigDecimal monthlyIncome;
    private BigDecimal existingLoanAmount;
    private String employmentType;
    private String loanType;
    private BigDecimal requestedAmount;
    private Integer tenureInMonths;
    private String status;
}