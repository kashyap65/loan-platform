CREATE TABLE loan_applications (
                                   application_id      UUID            NOT NULL DEFAULT gen_random_uuid(),
                                   applicant_name      VARCHAR(255)    NOT NULL,
                                   date_of_birth       DATE            NOT NULL,
                                   email               VARCHAR(255)    NOT NULL,
                                   phone               VARCHAR(50)     NOT NULL,
                                   address             TEXT            NOT NULL,
                                   employment_type     VARCHAR(100)    NOT NULL,
                                   monthly_income      DECIMAL(19, 2)  NOT NULL,
                                   designation         VARCHAR(100),
                                   existing_loan_amount DECIMAL(19, 2) NOT NULL DEFAULT 0,
                                   loan_type           VARCHAR(100)    NOT NULL,
                                   requested_amount    DECIMAL(19, 2)  NOT NULL,
                                   purpose             TEXT            NOT NULL,
                                   tenure_in_months    INTEGER         NOT NULL,
                                   status              VARCHAR(20)     NOT NULL,
                                   created_at          TIMESTAMP       NOT NULL,

                                   CONSTRAINT pk_loan_applications PRIMARY KEY (application_id),
                                   CONSTRAINT chk_loan_applications_status CHECK (status IN ('RECEIVED', 'UNDER_REVIEW', 'APPROVED', 'REJECTED', 'DISBURSED'))
);
CREATE INDEX idx_loan_applications_email
    ON loan_applications(email);

CREATE INDEX idx_loan_applications_status
    ON loan_applications(status);