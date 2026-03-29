package com.loanplatform.loanapplication.repository;

import com.loanplatform.loanapplication.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, UUID> {

    List<LoanApplication> findByEmail(String email);
}