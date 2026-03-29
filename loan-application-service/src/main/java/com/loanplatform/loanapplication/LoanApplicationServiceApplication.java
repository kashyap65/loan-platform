package com.loanplatform.loanapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LoanApplicationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoanApplicationServiceApplication.class,args);
    }
}
