package com.loanplatform.creditassessment.event;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditAssessmentEvent {

    private UUID assessmentId;
    private UUID applicationId;
    private String applicantName;
    private String email;
    private String finalRiskLevel;
    private BigDecimal debtToIncomeRatio;
    private BigDecimal loanToIncomeRatio;
    private LocalDateTime assessedAt;
}