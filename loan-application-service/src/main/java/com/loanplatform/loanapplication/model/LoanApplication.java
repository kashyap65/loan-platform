package com.loanplatform.loanapplication.model;

import com.loanplatform.loanapplication.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loan_applications")
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID applicationId;

    @Column(nullable = false)
    private String applicantName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String employmentType;

    @Column(nullable = false)
    private BigDecimal monthlyIncome;

    private String designation;

    @Column(precision = 19, scale = 2, columnDefinition = "DECIMAL(19,2) DEFAULT 0")
    private BigDecimal existingLoanAmount;

    @Column(nullable = false)
    private String loanType;

    @Column(nullable = false)
    private BigDecimal requestedAmount;

    @Column(nullable = false)
    private String purpose;

    @Column(nullable = false)
    private Integer tenureInMonths;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.existingLoanAmount == null) {
            this.existingLoanAmount = BigDecimal.ZERO;
        }
    }
}