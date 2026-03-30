package com.loanplatform.creditassessment.repository;

import com.loanplatform.creditassessment.model.CreditAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CreditAssessmentRepository extends JpaRepository<CreditAssessment, UUID> {

    Optional<CreditAssessment> findByApplicationId(UUID applicationId);
}