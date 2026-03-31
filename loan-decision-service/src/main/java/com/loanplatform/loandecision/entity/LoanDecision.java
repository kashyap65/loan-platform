package com.loanplatform.loandecision.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loan_decisions")
public class LoanDecision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_id", nullable = false, unique = true, length = 100)
    private String applicationId;

    @Column(name = "assessment_id", nullable = false, length = 100)
    private String assessmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision", nullable = false, length = 20)
    private DecisionStatus decision;

    @Column(name = "decision_reason", nullable = false, length = 1000)
    private String decisionReason;

    @Column(name = "rules_passed", nullable = false)
    private int rulesPassed;

    @Column(name = "rules_total", nullable = false)
    private int rulesTotal;

    @Column(name = "decided_at", nullable = false)
    private LocalDateTime decidedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (decidedAt == null) {
            decidedAt = LocalDateTime.now();
        }
    }
}