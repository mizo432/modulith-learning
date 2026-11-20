-- フィーチャーテーブルを作成します。
-- スクラムチームのバックログアイテムを論理的にグループ化するフィーチャーを管理します。
CREATE TABLE undecided_ag.features
(
    id            BIGINT       NOT NULL PRIMARY KEY,
    name          VARCHAR(200) NOT NULL,
    description   TEXT,
    status        VARCHAR(20)  NOT NULL DEFAULT 'PLANNED',
    scrum_team_id BIGINT       NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version       INTEGER,
    CONSTRAINT ck_features_status CHECK (status IN ('PLANNED', 'IN_PROGRESS', 'DONE')),
    CONSTRAINT fk_features_scrum_team_subsets FOREIGN KEY (scrum_team_id) REFERENCES undecided_ag.scrum_team_subsets (id)
);

CREATE INDEX idx_features_scrum_team_id ON undecided_ag.features (scrum_team_id);

COMMENT ON TABLE undecided_ag.features IS 'フィーチャー（バックログアイテムの論理的グループ化）';
COMMENT ON COLUMN undecided_ag.features.id IS 'フィーチャーの一意な識別子';
COMMENT ON COLUMN undecided_ag.features.name IS 'フィーチャー名（最大200文字）';
COMMENT ON COLUMN undecided_ag.features.description IS '説明（TEXT型で上限なし）';
COMMENT ON COLUMN undecided_ag.features.status IS '状態（PLANNED／IN_PROGRESS／DONE）';
COMMENT ON COLUMN undecided_ag.features.scrum_team_id IS '所属するスクラムチームのID';
COMMENT ON COLUMN undecided_ag.features.created_at IS '作成日時';
COMMENT ON COLUMN undecided_ag.features.updated_at IS '最終更新日時';
COMMENT ON COLUMN undecided_ag.features.version IS '楽観ロック用のバージョン番号';
