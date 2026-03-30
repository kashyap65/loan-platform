package com.loanplatform.creditassessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CreditAssessmentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CreditAssessmentServiceApplication.class,args);
    }
}
