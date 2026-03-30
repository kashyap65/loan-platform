package com.loanplatform.creditassessment.model;

import com.loanplatform.creditassessment.enums.RiskLevel;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "credit_assessments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "assessment_id", updatable = false, nullable = false)
    private UUID assessmentId;

    @Column(name = "application_id", nullable = false)
    private UUID applicationId;

    @Column(name = "applicant_name", nullable = false)
    private String applicantName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "monthly_income", nullable = false)
    private BigDecimal monthlyIncome;

    @Column(name = "existing_loan_amount", nullable = false)
    private BigDecimal existingLoanAmount;

    @Column(name = "requested_amount", nullable = false)
    private BigDecimal requestedAmount;

    @Column(name = "employment_type", nullable = false)
    private String employmentType;

    @Column(name = "debt_to_income_ratio", nullable = false)
    private BigDecimal debtToIncomeRatio;

    @Column(name = "loan_to_income_ratio", nullable = false)
    private BigDecimal loanToIncomeRatio;

    @Enumerated(EnumType.STRING)
    @Column(name = "debt_risk_level", nullable = false)
    private RiskLevel debtRiskLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_risk_level", nullable = false)
    private RiskLevel loanRiskLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_risk_level", nullable = false)
    private RiskLevel employmentRiskLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "final_risk_level", nullable = false)
    private RiskLevel finalRiskLevel;

    @Column(name = "assessed_at", nullable = false, updatable = false)
    private LocalDateTime assessedAt;

    @PrePersist
    private void prePersist() {
        assessedAt = LocalDateTime.now();
    }
}