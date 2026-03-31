package com.loanplatform.loandecision.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanplatform.loandecision.messaging.dto.AssessmentResultMessage;
import com.loanplatform.loandecision.service.DecisionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class CreditAssessmentResultListener {

    private static final Logger log = LoggerFactory.getLogger(CreditAssessmentResultListener.class);

    private final DecisionService decisionService;
    private final ObjectMapper objectMapper;

    public CreditAssessmentResultListener(DecisionService decisionService,
                                          ObjectMapper objectMapper) {
        this.decisionService = decisionService;
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "${messaging.queues.assessment-result}",
            containerFactory = "jmsListenerContainerFactory")
    public void onAssessmentResult(String message) {
        log.info("Received assessment result message: {}", message);
        try {
            AssessmentResultMessage assessmentResult =
                    objectMapper.readValue(message, AssessmentResultMessage.class);
            decisionService.processAssessmentResult(assessmentResult);
        } catch (Exception e) {
            log.error("Failed to process assessment result message: {}", message, e);
            throw new RuntimeException("Message processing failed", e);
        }
    }
}