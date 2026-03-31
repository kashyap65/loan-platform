package com.loanplatform.loandecision.repository;

import com.loanplatform.loandecision.entity.LoanDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanDecisionRepository extends JpaRepository<LoanDecision, Long> {

    Optional<LoanDecision> findByApplicationId(String applicationId);
}