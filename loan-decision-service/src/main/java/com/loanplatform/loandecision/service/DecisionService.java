package com.loanplatform.loandecision.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanplatform.loandecision.entity.DecisionOutbox;
import com.loanplatform.loandecision.entity.DecisionStatus;
import com.loanplatform.loandecision.entity.LoanDecision;
import com.loanplatform.loandecision.messaging.dto.AssessmentResultMessage;
import com.loanplatform.loandecision.repository.DecisionOutboxRepository;
import com.loanplatform.loandecision.repository.LoanDecisionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class DecisionService {

    private static final Logger log = LoggerFactory.getLogger(DecisionService.class);

    private final LoanDecisionRepository loanDecisionRepository;
    private final DecisionOutboxRepository decisionOutboxRepository;
    private final ObjectMapper objectMapper;

    public DecisionService(LoanDecisionRepository loanDecisionRepository,
                           DecisionOutboxRepository decisionOutboxRepository,
                           ObjectMapper objectMapper) {
        this.loanDecisionRepository = loanDecisionRepository;
        this.decisionOutboxRepository = decisionOutboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void processAssessmentResult(AssessmentResultMessage message) {

        // Step 1: Idempotency check
        if (loanDecisionRepository.findByApplicationId(message.getApplicationId()).isPresent()) {
            log.warn("Duplicate message received for applicationId: {}. Skipping.",
                    message.getApplicationId());
            return;
        }

        // Step 2: Determine decision
        DecisionStatus status = determineDecision(message.getRulesPassed(), message.getRulesTotal());
        String reason = buildDecisionReason(status, message);

        // Step 3: Build and save LoanDecision
        LoanDecision decision = new LoanDecision();
        decision.setApplicationId(message.getApplicationId());
        decision.setAssessmentId(message.getAssessmentId());
        decision.setDecision(status);
        decision.setDecisionReason(reason);
        decision.setRulesPassed(message.getRulesPassed());
        decision.setRulesTotal(message.getRulesTotal());
        decision.setDecidedAt(LocalDateTime.now());

        LoanDecision savedDecision = loanDecisionRepository.save(decision);

        // Step 4: Build and save outbox row
        String payload = buildPayload(savedDecision);
        DecisionOutbox outbox = new DecisionOutbox();
        outbox.setLoanDecisionId(savedDecision.getId());
        outbox.setApplicationId(savedDecision.getApplicationId());
        outbox.setPayload(payload);

        decisionOutboxRepository.save(outbox);

        log.info("Decision {} saved for applicationId: {}", status, message.getApplicationId());
    }

    private DecisionStatus determineDecision(int rulesPassed, int rulesTotal) {
        if (rulesPassed == rulesTotal) {
            return DecisionStatus.APPROVED;
        } else if (rulesPassed >= rulesTotal - 1) {
            return DecisionStatus.REFERRED;
        } else {
            return DecisionStatus.REJECTED;
        }
    }

    private String buildDecisionReason(DecisionStatus status, AssessmentResultMessage message) {
        return switch (status) {
            case APPROVED -> String.format(
                    "Application approved: all %d credit rules passed successfully.",
                    message.getRulesTotal());
            case REFERRED -> String.format(
                    "Application referred for manual review: %d of %d rules passed.",
                    message.getRulesPassed(), message.getRulesTotal());
            case REJECTED -> String.format(
                    "Application rejected: only %d of %d rules passed, below minimum threshold.",
                    message.getRulesPassed(), message.getRulesTotal());
        };
    }

    private String buildPayload(LoanDecision decision) {
        try {
            return objectMapper.writeValueAsString(Map.of(
                    "applicationId", decision.getApplicationId(),
                    "assessmentId", decision.getAssessmentId(),
                    "decision", decision.getDecision().name(),
                    "decisionReason", decision.getDecisionReason(),
                    "decidedAt", decision.getDecidedAt().toString()
            ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize decision payload", e);
        }
    }
}