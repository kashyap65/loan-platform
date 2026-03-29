package com.loanplatform.loanapplication.messaging;

import com.loanplatform.loanapplication.model.OutboxEvent;
import com.loanplatform.loanapplication.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventPoller {

    private static final String TOPIC = "loan-applications";

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    public void pollAndPublish() {
        List<OutboxEvent> pendingEvents = outboxEventRepository.findByStatus("PENDING");

        for (OutboxEvent event : pendingEvents) {
            try {
                kafkaTemplate.send(TOPIC, event.getAggregateId().toString(), event.getPayload()).get();
                event.setStatus("PUBLISHED");
                event.setPublishedAt(LocalDateTime.now());
                outboxEventRepository.save(event);
                log.info("Published outbox event [id={}] to topic '{}'", event.getId(), TOPIC);
            } catch (Exception e) {
                log.error("Failed to publish outbox event [id={}]: {}", event.getId(), e.getMessage());
            }
        }
    }
}
