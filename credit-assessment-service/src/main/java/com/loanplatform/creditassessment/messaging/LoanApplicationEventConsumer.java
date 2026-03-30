package com.loanplatform.creditassessment.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanplatform.creditassessment.event.LoanApplicationEvent;
import com.loanplatform.creditassessment.service.CreditAssessmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanApplicationEventConsumer {

    private final CreditAssessmentService creditAssessmentService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "loan-applications", groupId = "credit-assessment-group")
    public void consume(String message) {
        try {
            LoanApplicationEvent event = objectMapper.readValue(message, LoanApplicationEvent.class);
            log.info("Received loan application event for applicationId: {}", event.getApplicationId());
            creditAssessmentService.assess(event);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialise loan application event: {}", e.getMessage());
        }
    }
}