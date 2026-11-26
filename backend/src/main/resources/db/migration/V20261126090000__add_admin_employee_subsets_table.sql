-- JBジョブ管理: admin_employee_subsetsテーブル作成（ADMINロール保持者の射影）

CREATE SCHEMA IF NOT EXISTS job_management;

CREATE TABLE job_management.admin_employee_subsets (
    employee_id BIGINT NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (employee_id)
);
