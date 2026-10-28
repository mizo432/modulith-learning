-- JBジョブ管理: job_deploymentテーブル作成

CREATE TABLE job_deployment (
    id BIGINT NOT NULL,
    job_code VARCHAR(50) NOT NULL,
    host VARCHAR(255) NOT NULL,
    port INTEGER NOT NULL,
    protocol VARCHAR(10) NOT NULL,
    deployment_version VARCHAR(20) NOT NULL,
    deployed_at TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX idx_job_deployment_job_code ON job_deployment(job_code);
CREATE INDEX idx_job_deployment_host ON job_deployment(host);

ALTER TABLE job_deployment
    ADD CONSTRAINT fk_job_deployment_job_code
    FOREIGN KEY (job_code) REFERENCES job_definition(job_code);
