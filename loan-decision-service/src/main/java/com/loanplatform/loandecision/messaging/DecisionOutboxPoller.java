package com.loanplatform.loandecision.messaging;

import com.loanplatform.loandecision.entity.DecisionOutbox;
import com.loanplatform.loandecision.entity.OutboxStatus;
import com.loanplatform.loandecision.repository.DecisionOutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DecisionOutboxPoller {

    private static final Logger log = LoggerFactory.getLogger(DecisionOutboxPoller.class);

    private final DecisionOutboxRepository decisionOutboxRepository;
    private final JmsTemplate jmsTemplate;

    @Value("${messaging.queues.decision-reply}")
    private String decisionReplyQueue;

    public DecisionOutboxPoller(DecisionOutboxRepository decisionOutboxRepository,
                                JmsTemplate jmsTemplate) {
        this.decisionOutboxRepository = decisionOutboxRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @Scheduled(fixedDelayString = "${messaging.outbox.poll-interval-ms:5000}")
    @Transactional
    public void pollAndPublish() {
        List<DecisionOutbox> pendingMessages =
                decisionOutboxRepository.findTop10ByStatusOrderByCreatedAtAsc(
                        OutboxStatus.PENDING);

        if (pendingMessages.isEmpty()) {
            return;
        }

        log.info("Found {} pending outbox messages to publish", pendingMessages.size());

        for (DecisionOutbox outbox : pendingMessages) {
            try {
                jmsTemplate.convertAndSend(decisionReplyQueue, outbox.getPayload());
                outbox.setStatus(OutboxStatus.SENT);
                outbox.setSentAt(LocalDateTime.now());
                decisionOutboxRepository.save(outbox);
                log.info("Published decision for applicationId: {}", outbox.getApplicationId());
            } catch (Exception e) {
                log.error("Failed to publish outbox message id: {}. Marking as FAILED.",
                        outbox.getId(), e);
                outbox.setStatus(OutboxStatus.FAILED);
                decisionOutboxRepository.save(outbox);
            }
        }
    }
}