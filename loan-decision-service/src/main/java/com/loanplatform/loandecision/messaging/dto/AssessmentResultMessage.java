package com.loanplatform.loandecision.messaging.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResultMessage {

    private String applicationId;
    private String assessmentId;
    private int rulesPassed;
    private int rulesTotal;
    private String creditScore;
    private String employmentStatus;
}