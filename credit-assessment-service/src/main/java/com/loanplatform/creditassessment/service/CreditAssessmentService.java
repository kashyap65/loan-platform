package com.loanplatform.creditassessment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanplatform.creditassessment.enums.RiskLevel;
import com.loanplatform.creditassessment.event.CreditAssessmentEvent;
import com.loanplatform.creditassessment.event.LoanApplicationEvent;
import com.loanplatform.creditassessment.model.CreditAssessment;
import com.loanplatform.creditassessment.model.OutboxEvent;
import com.loanplatform.creditassessment.repository.CreditAssessmentRepository;
import com.loanplatform.creditassessment.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditAssessmentService {

    private final CreditAssessmentRepository creditAssessmentRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final CreditRulesEngine creditRulesEngine;
    private final ObjectMapper objectMapper;

    @Transactional
    public void assess(LoanApplicationEvent event) {
        log.info("Starting credit assessment for applicationId: {}", event.getApplicationId());

        BigDecimal debtToIncomeRatio = creditRulesEngine.calculateDebtToIncomeRatio(
                event.getExistingLoanAmount(), event.getMonthlyIncome());

        BigDecimal loanToIncomeRatio = creditRulesEngine.calculateLoanToIncomeRatio(
                event.getRequestedAmount(), event.getMonthlyIncome());

        RiskLevel debtRisk       = creditRulesEngine.assessDebtRisk(debtToIncomeRatio);
        RiskLevel loanRisk       = creditRulesEngine.assessLoanRisk(loanToIncomeRatio);
        RiskLevel employmentRisk = creditRulesEngine.assessEmploymentRisk(event.getEmploymentType());
        RiskLevel finalRisk      = creditRulesEngine.calculateFinalRisk(debtRisk, loanRisk, employmentRisk);

        log.info("Risk assessment for applicationId: {} — debt={}, loan={}, employment={}, final={}",
                event.getApplicationId(), debtRisk, loanRisk, employmentRisk, finalRisk);

        CreditAssessment assessment = CreditAssessment.builder()
                .applicationId(event.getApplicationId())
                .applicantName(event.getApplicantName())
                .email(event.getEmail())
                .monthlyIncome(event.getMonthlyIncome())
                .existingLoanAmount(event.getExistingLoanAmount())
                .requestedAmount(event.getRequestedAmount())
                .employmentType(event.getEmploymentType())
                .debtToIncomeRatio(debtToIncomeRatio)
                .loanToIncomeRatio(loanToIncomeRatio)
                .debtRiskLevel(debtRisk)
                .loanRiskLevel(loanRisk)
                .employmentRiskLevel(employmentRisk)
                .finalRiskLevel(finalRisk)
                .build();

        CreditAssessment saved = creditAssessmentRepository.save(assessment);
        log.info("Credit assessment saved with ID: {}", saved.getAssessmentId());

        CreditAssessmentEvent assessmentEvent = CreditAssessmentEvent.builder()
                .assessmentId(saved.getAssessmentId())
                .applicationId(saved.getApplicationId())
                .applicantName(saved.getApplicantName())
                .email(saved.getEmail())
                .finalRiskLevel(saved.getFinalRiskLevel().name())
                .debtToIncomeRatio(saved.getDebtToIncomeRatio())
                .loanToIncomeRatio(saved.getLoanToIncomeRatio())
                .assessedAt(saved.getAssessedAt())
                .build();

        String payload;
        try {
            payload = objectMapper.writeValueAsString(assessmentEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize credit assessment event", e);
        }

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .aggregateId(saved.getAssessmentId())
                .eventType("CREDIT_ASSESSMENT_COMPLETED")
                .payload(payload)
                .status("PENDING")
                .build();

        OutboxEvent savedOutboxEvent = outboxEventRepository.save(outboxEvent);
        log.info("Outbox event saved with status: {}", savedOutboxEvent.getStatus());
    }
}