-- JBジョブ管理: job_definitionテーブル作成

CREATE TABLE job_definition (
    id BIGINT NOT NULL,
    job_code VARCHAR(50) NOT NULL UNIQUE,
    job_name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    parameter_spec TEXT NOT NULL,
    max_concurrent_executions INTEGER NOT NULL,
    timeout_seconds INTEGER NOT NULL,
    retry_count INTEGER NOT NULL,
    version VARCHAR(20) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX idx_job_definition_enabled ON job_definition(enabled);
