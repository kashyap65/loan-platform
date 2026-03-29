CREATE TABLE outbox_events (
    id               UUID            NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    aggregate_id     UUID            NOT NULL,
    event_type       VARCHAR(100)    NOT NULL,
    payload          JSONB           NOT NULL,
    status           VARCHAR(20)     NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PUBLISHED')),
    created_at       TIMESTAMP       NOT NULL DEFAULT now(),
    published_at     TIMESTAMP
);

CREATE INDEX idx_outbox_events_status ON outbox_events (status);
CREATE INDEX idx_outbox_events_aggregate_id ON outbox_events (aggregate_id);