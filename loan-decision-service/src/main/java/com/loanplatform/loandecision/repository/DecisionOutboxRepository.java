package com.loanplatform.loandecision.repository;

import com.loanplatform.loandecision.entity.DecisionOutbox;
import com.loanplatform.loandecision.entity.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DecisionOutboxRepository extends JpaRepository<DecisionOutbox, Long> {

    List<DecisionOutbox> findTop10ByStatusOrderByCreatedAtAsc(OutboxStatus status);
}