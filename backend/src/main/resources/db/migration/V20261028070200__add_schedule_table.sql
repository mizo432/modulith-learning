-- JBジョブ管理: scheduleテーブル作成

CREATE TABLE schedule (
    id BIGINT NOT NULL,
    job_code VARCHAR(50) NOT NULL,
    execution_date_time TIMESTAMP,
    cron_expression VARCHAR(100),
    timezone VARCHAR(50),
    start_date DATE NOT NULL,
    end_date DATE,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX idx_schedule_job_code ON schedule(job_code);
CREATE INDEX idx_schedule_enabled ON schedule(enabled);

ALTER TABLE schedule
    ADD CONSTRAINT fk_schedule_job_code
    FOREIGN KEY (job_code) REFERENCES job_definition(job_code);
