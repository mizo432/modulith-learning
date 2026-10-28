-- JBジョブ管理: job_executionテーブル作成

CREATE TABLE job_execution (
    id BIGINT NOT NULL,
    job_code VARCHAR(50) NOT NULL,
    job_version VARCHAR(20) NOT NULL,
    host VARCHAR(255) NOT NULL,
    port INTEGER NOT NULL,
    protocol VARCHAR(10) NOT NULL,
    execution_state VARCHAR(20) NOT NULL,
    execution_parameter TEXT,
    checkpoint_position TEXT,
    checkpoint_timestamp TIMESTAMP,
    processed_count BIGINT,
    error_code VARCHAR(50),
    error_message TEXT,
    error_location VARCHAR(255),
    error_occurred_at TIMESTAMP,
    started_at TIMESTAMP,
    finished_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX idx_job_execution_job_code ON job_execution(job_code);
CREATE INDEX idx_job_execution_state ON job_execution(execution_state);
CREATE INDEX idx_job_execution_started_at ON job_execution(started_at);

ALTER TABLE job_execution
    ADD CONSTRAINT fk_job_execution_job_code
    FOREIGN KEY (job_code) REFERENCES job_definition(job_code);
