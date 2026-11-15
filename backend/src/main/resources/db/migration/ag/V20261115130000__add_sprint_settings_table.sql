-- スプリント設定テーブルを作成します。
CREATE TABLE undecided_ag.sprint_settings
(
    id              BIGINT        NOT NULL PRIMARY KEY,
    product_id      UUID          NOT NULL,
    business_days   INT           NOT NULL DEFAULT 10
        CHECK (business_days >= 1 AND business_days <= 30),
    end_day_of_week VARCHAR(10)   NOT NULL DEFAULT 'FRIDAY'
        CHECK (end_day_of_week IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY')),
    created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP,
    CONSTRAINT uk_sprint_settings_product UNIQUE (product_id)
);

CREATE INDEX idx_sprint_settings_product_id ON undecided_ag.sprint_settings (product_id);

COMMENT ON TABLE undecided_ag.sprint_settings IS 'スプリント設定テーブル';
COMMENT ON COLUMN undecided_ag.sprint_settings.id IS 'スプリント設定の一意な識別子';
COMMENT ON COLUMN undecided_ag.sprint_settings.product_id IS '関連するプロダクトのID';
COMMENT ON COLUMN undecided_ag.sprint_settings.business_days IS 'スプリント期間（営業日数）デフォルト10';
COMMENT ON COLUMN undecided_ag.sprint_settings.end_day_of_week IS 'スプリント終了曜日 デフォルトFRIDAY';
COMMENT ON COLUMN undecided_ag.sprint_settings.created_at IS '作成日時';
COMMENT ON COLUMN undecided_ag.sprint_settings.updated_at IS '更新日時';
COMMENT ON COLUMN undecided_ag.sprint_settings.deleted_at IS '論理削除日時（null=未削除）';
