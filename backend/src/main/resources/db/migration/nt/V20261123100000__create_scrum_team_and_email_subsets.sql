CREATE TABLE undecided_nt.scrum_team_subsets (
    id BIGINT NOT NULL PRIMARY KEY,
    member_employee_ids BIGINT[] NOT NULL DEFAULT '{}',
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE undecided_nt.employee_email_subsets (
    id BIGINT NOT NULL PRIMARY KEY,
    email VARCHAR(200) NOT NULL,
    occurred_at TIMESTAMP NOT NULL,
    comment_id BIGINT NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_comment_participants_employee ON undecided_nt.comment_participants(employee_id);