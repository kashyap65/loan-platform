CREATE TABLE outbox_events (
    id           UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID(),
    aggregate_id UNIQUEIDENTIFIER NOT NULL,
    event_type   NVARCHAR(100)    NOT NULL,
    payload      NVARCHAR(MAX)    NOT NULL,
    status       NVARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    created_at   DATETIME2        NOT NULL DEFAULT GETDATE(),
    published_at DATETIME2,

    CONSTRAINT pk_outbox_events         PRIMARY KEY (id),
    CONSTRAINT chk_outbox_events_status CHECK (status IN ('PENDING', 'PUBLISHED'))
);

CREATE INDEX idx_outbox_events_status       ON outbox_events (status);
CREATE INDEX idx_outbox_events_aggregate_id ON outbox_events (aggregate_id);