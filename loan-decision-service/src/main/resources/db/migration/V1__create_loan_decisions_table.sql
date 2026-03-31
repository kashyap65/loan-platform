CREATE TABLE loan_decisions (
                                id                NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                application_id    VARCHAR2(100)  NOT NULL,
                                assessment_id     VARCHAR2(100)  NOT NULL,
                                decision          VARCHAR2(20)   NOT NULL,
                                decision_reason   VARCHAR2(1000) NOT NULL,
                                rules_passed      NUMBER(2)      NOT NULL,
                                rules_total       NUMBER(2)      NOT NULL,
                                decided_at        TIMESTAMP      NOT NULL,
                                created_at        TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                CONSTRAINT uk_loan_decisions_application_id UNIQUE (application_id),
                                CONSTRAINT chk_decision CHECK (decision IN ('APPROVED', 'REJECTED', 'REFERRED'))
);