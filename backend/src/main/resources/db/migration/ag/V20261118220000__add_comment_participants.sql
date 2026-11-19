CREATE TABLE undecided_ag.comment_participants (
    id BIGINT NOT NULL,
    backlog_item_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    employee_name VARCHAR(100) NOT NULL,
    employee_email VARCHAR(200) NOT NULL DEFAULT '',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (backlog_item_id, employee_id)
);

CREATE INDEX idx_ag_comment_participants_backlog_item ON undecided_ag.comment_participants(backlog_item_id);
