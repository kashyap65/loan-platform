package com.loanplatform.creditassessment.messaging;

import com.loanplatform.creditassessment.model.OutboxEvent;
import com.loanplatform.creditassessment.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventPoller {

    private static final String QUEUE = "credit-assessments";

    private final OutboxEventRepository outboxEventRepository;
    private final JmsTemplate jmsTemplate;

    @Scheduled(fixedDelay = 5000)
    public void pollAndPublish() {
        List<OutboxEvent> pendingEvents = outboxEventRepository.findByStatus("PENDING");

        for (OutboxEvent event : pendingEvents) {
            try {
                jmsTemplate.convertAndSend(QUEUE, event.getPayload());
                event.setStatus("PUBLISHED");
                event.setPublishedAt(LocalDateTime.now());
                outboxEventRepository.save(event);
                log.info("Published outbox event [id={}] to queue '{}'", event.getId(), QUEUE);
            } catch (Exception e) {
                log.error("Failed to publish outbox event [id={}]: {}", event.getId(), e.getMessage());
            }
        }
    }
}