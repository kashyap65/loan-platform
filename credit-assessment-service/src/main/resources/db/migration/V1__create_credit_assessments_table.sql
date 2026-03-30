CREATE TABLE credit_assessments (
                                    assessment_id        UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID(),
                                    application_id       UNIQUEIDENTIFIER NOT NULL,
                                    applicant_name       NVARCHAR(255)    NOT NULL,
                                    email                NVARCHAR(255)    NOT NULL,
                                    monthly_income       DECIMAL(15, 2)   NOT NULL,
                                    existing_loan_amount DECIMAL(15, 2)   NOT NULL,
                                    requested_amount     DECIMAL(15, 2)   NOT NULL,
                                    employment_type      NVARCHAR(50)     NOT NULL,
                                    debt_to_income_ratio DECIMAL(10, 4)    NOT NULL,
                                    loan_to_income_ratio DECIMAL(10, 4)    NOT NULL,
                                    debt_risk_level      NVARCHAR(10)     NOT NULL,
                                    loan_risk_level      NVARCHAR(10)     NOT NULL,
                                    employment_risk_level NVARCHAR(10)    NOT NULL,
                                    final_risk_level     NVARCHAR(10)     NOT NULL,
                                    assessed_at          DATETIME2        NOT NULL DEFAULT GETDATE(),

                                    CONSTRAINT pk_credit_assessments
                                        PRIMARY KEY (assessment_id),
                                    CONSTRAINT chk_debt_risk_level
                                        CHECK (debt_risk_level IN ('LOW', 'MEDIUM', 'HIGH')),
                                    CONSTRAINT chk_loan_risk_level
                                        CHECK (loan_risk_level IN ('LOW', 'MEDIUM', 'HIGH')),
                                    CONSTRAINT chk_employment_risk_level
                                        CHECK (employment_risk_level IN ('LOW', 'MEDIUM', 'HIGH')),
                                    CONSTRAINT chk_final_risk_level
                                        CHECK (final_risk_level IN ('LOW', 'MEDIUM', 'HIGH'))
);

CREATE INDEX idx_credit_assessments_application_id
    ON credit_assessments (application_id);

CREATE INDEX idx_credit_assessments_assessed_at
    ON credit_assessments (assessed_at);