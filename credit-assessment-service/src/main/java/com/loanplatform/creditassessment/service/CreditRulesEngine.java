package com.loanplatform.creditassessment.service;

import com.loanplatform.creditassessment.enums.RiskLevel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CreditRulesEngine {

    private static final BigDecimal DTI_LOW_THRESHOLD    = new BigDecimal("0.30");
    private static final BigDecimal DTI_MEDIUM_THRESHOLD = new BigDecimal("0.50");

    private static final BigDecimal LTI_LOW_THRESHOLD    = new BigDecimal("5");
    private static final BigDecimal LTI_MEDIUM_THRESHOLD = new BigDecimal("10");

    public BigDecimal calculateDebtToIncomeRatio(BigDecimal existingLoanAmount, BigDecimal monthlyIncome) {
        if (monthlyIncome.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ONE; // 100% ratio = HIGH risk
        }
        return existingLoanAmount.divide(monthlyIncome, 4, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateLoanToIncomeRatio(BigDecimal requestedAmount, BigDecimal monthlyIncome) {
        if (monthlyIncome.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ONE; // 100% ratio = HIGH risk
        }
        return requestedAmount.divide(monthlyIncome, 4, RoundingMode.HALF_UP);
    }

    public RiskLevel assessDebtRisk(BigDecimal debtToIncomeRatio) {
        if (debtToIncomeRatio.compareTo(DTI_LOW_THRESHOLD) < 0) {
            return RiskLevel.LOW;
        } else if (debtToIncomeRatio.compareTo(DTI_MEDIUM_THRESHOLD) <= 0) {
            return RiskLevel.MEDIUM;
        } else {
            return RiskLevel.HIGH;
        }
    }

    public RiskLevel assessLoanRisk(BigDecimal loanToIncomeRatio) {
        if (loanToIncomeRatio.compareTo(LTI_LOW_THRESHOLD) < 0) {
            return RiskLevel.LOW;
        } else if (loanToIncomeRatio.compareTo(LTI_MEDIUM_THRESHOLD) <= 0) {
            return RiskLevel.MEDIUM;
        } else {
            return RiskLevel.HIGH;
        }
    }

    public RiskLevel assessEmploymentRisk(String employmentType) {
        return switch (employmentType.toUpperCase()) {
            case "SALARIED"      -> RiskLevel.LOW;
            case "SELF_EMPLOYED" -> RiskLevel.MEDIUM;
            default              -> RiskLevel.HIGH;
        };
    }

    public RiskLevel calculateFinalRisk(RiskLevel debtRisk, RiskLevel loanRisk, RiskLevel employmentRisk) {
        RiskLevel highest = debtRisk;
        if (loanRisk.ordinal() > highest.ordinal())       highest = loanRisk;
        if (employmentRisk.ordinal() > highest.ordinal()) highest = employmentRisk;
        return highest;
    }
}