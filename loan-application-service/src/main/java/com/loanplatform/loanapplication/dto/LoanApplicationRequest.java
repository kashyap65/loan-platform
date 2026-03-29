package com.loanplatform.loanapplication.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationRequest {

    @NotBlank
    private String applicantName;

    @NotNull
    private LocalDate dateOfBirth;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    @NotBlank
    private String employmentType;

    @NotNull
    @Positive
    private BigDecimal monthlyIncome;

    private String designation;

    @PositiveOrZero
    private BigDecimal existingLoanAmount;

    @NotBlank
    private String loanType;

    @NotNull
    @Positive
    private BigDecimal requestedAmount;

    @NotBlank
    private String purpose;

    @NotNull
    @Min(6)
    @Max(360)
    private Integer tenureInMonths;
}