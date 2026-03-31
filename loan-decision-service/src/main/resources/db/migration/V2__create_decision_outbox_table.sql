CREATE TABLE decision_outbox (
                                 id                NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                 loan_decision_id  NUMBER         NOT NULL,
                                 application_id    VARCHAR2(100)  NOT NULL,
                                 payload           CLOB           NOT NULL,
                                 status            VARCHAR2(20)   DEFAULT 'PENDING' NOT NULL,
                                 created_at        TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 sent_at           TIMESTAMP,
                                 CONSTRAINT fk_outbox_loan_decision
                                     FOREIGN KEY (loan_decision_id) REFERENCES loan_decisions(id),
                                 CONSTRAINT chk_outbox_status
                                     CHECK (status IN ('PENDING', 'SENT', 'FAILED'))
);

CREATE INDEX idx_decision_outbox_status
    ON decision_outbox (status, created_at);