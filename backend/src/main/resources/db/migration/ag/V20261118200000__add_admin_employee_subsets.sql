-- 管理者（ADMINロール保持者）の従業員IDを保持するサブセット（射影）テーブルを作成します。
-- association 領域の AdminEmployeeChanged イベントで連携され、requirementBacklog 領域内でのみ参照します。
CREATE TABLE undecided_ag.admin_employee_subsets
(
    employee_id BIGINT       NOT NULL PRIMARY KEY,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE undecided_ag.admin_employee_subsets IS '管理者（ADMINロール保持者）のサブセット（イベント連携による射影）';
COMMENT ON COLUMN undecided_ag.admin_employee_subsets.employee_id IS '従業員ID（ADMINロール保持者）';
COMMENT ON COLUMN undecided_ag.admin_employee_subsets.updated_at IS 'サブセットが最後にイベントで更新された日時';
