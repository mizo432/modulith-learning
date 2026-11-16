-- スクラムチームのサブセット（射影）テーブルを作成します。
-- productDiscovery の ScrumTeam 変更イベントで連携され、requirementBacklog 領域内でのみ参照します。
CREATE TABLE undecided_ag.scrum_team_subsets
(
    id            BIGINT       NOT NULL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    po_employee_id UUID,
    po_name       VARCHAR(100),
    sm_employee_id UUID,
    sm_name       VARCHAR(100),
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE undecided_ag.scrum_team_subsets IS 'スクラムチーム情報のサブセット（イベント連携による射影）';
COMMENT ON COLUMN undecided_ag.scrum_team_subsets.id IS 'スクラムチームID（ScrumTeamのIDと同一）';
COMMENT ON COLUMN undecided_ag.scrum_team_subsets.name IS 'チーム名';
COMMENT ON COLUMN undecided_ag.scrum_team_subsets.po_employee_id IS 'PO（Product Owner）の従業員ID';
COMMENT ON COLUMN undecided_ag.scrum_team_subsets.po_name IS 'POの名前';
COMMENT ON COLUMN undecided_ag.scrum_team_subsets.sm_employee_id IS 'SM（Scrum Master）の従業員ID';
COMMENT ON COLUMN undecided_ag.scrum_team_subsets.sm_name IS 'SMの名前';
COMMENT ON COLUMN undecided_ag.scrum_team_subsets.updated_at IS 'サブセットが最後に更新された日時';

-- バックログアイテムテーブルを作成します。
CREATE TABLE undecided_ag.backlog_items
(
    id                  BIGINT       NOT NULL PRIMARY KEY,
    title               VARCHAR(200) NOT NULL,
    description         TEXT,
    status              VARCHAR(20)  NOT NULL DEFAULT 'SUGGESTED',
    priority            INTEGER,
    creator_employee_id UUID,
    creator_name        VARCHAR(100) NOT NULL,
    sprint_id           BIGINT,
    scrum_team_id       BIGINT       NOT NULL,
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_backlog_items_status CHECK (status IN ('SUGGESTED', 'ACCEPTED', 'REJECTED')),
    CONSTRAINT fk_backlog_items_scrum_team_subsets FOREIGN KEY (scrum_team_id) REFERENCES undecided_ag.scrum_team_subsets (id)
);

CREATE INDEX idx_backlog_items_scrum_team_id ON undecided_ag.backlog_items (scrum_team_id);
CREATE INDEX idx_backlog_items_status ON undecided_ag.backlog_items (status);

COMMENT ON TABLE undecided_ag.backlog_items IS 'バックログアイテム';
COMMENT ON COLUMN undecided_ag.backlog_items.id IS 'バックログアイテムの一意な識別子';
COMMENT ON COLUMN undecided_ag.backlog_items.title IS 'タイトル';
COMMENT ON COLUMN undecided_ag.backlog_items.description IS '説明';
COMMENT ON COLUMN undecided_ag.backlog_items.status IS '状態（SUGGESTED／ACCEPTED／REJECTED）';
COMMENT ON COLUMN undecided_ag.backlog_items.priority IS '優先度';
COMMENT ON COLUMN undecided_ag.backlog_items.creator_employee_id IS '作成者の従業員ID';
COMMENT ON COLUMN undecided_ag.backlog_items.creator_name IS '作成者名';
COMMENT ON COLUMN undecided_ag.backlog_items.sprint_id IS '関連スプリントのID';
COMMENT ON COLUMN undecided_ag.backlog_items.scrum_team_id IS '所属するスクラムチームのID';
COMMENT ON COLUMN undecided_ag.backlog_items.created_at IS '作成日時';
COMMENT ON COLUMN undecided_ag.backlog_items.updated_at IS '最終更新日時';

-- バックログアイテムのコメントテーブルを作成します。
CREATE TABLE undecided_ag.backlog_item_comments
(
    id                 BIGINT       NOT NULL PRIMARY KEY,
    backlog_item_id    BIGINT       NOT NULL,
    author_employee_id UUID,
    author_name        VARCHAR(100) NOT NULL,
    body               TEXT         NOT NULL,
    created_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_backlog_item_comments_items FOREIGN KEY (backlog_item_id) REFERENCES undecided_ag.backlog_items (id)
);

CREATE INDEX idx_backlog_item_comments_item_id ON undecided_ag.backlog_item_comments (backlog_item_id);

COMMENT ON TABLE undecided_ag.backlog_item_comments IS 'バックログアイテムへのコメント';
COMMENT ON COLUMN undecided_ag.backlog_item_comments.id IS 'コメントの一意な識別子';
COMMENT ON COLUMN undecided_ag.backlog_item_comments.backlog_item_id IS 'コメント対象のバックログアイテムID';
COMMENT ON COLUMN undecided_ag.backlog_item_comments.author_employee_id IS '投稿者の従業員ID';
COMMENT ON COLUMN undecided_ag.backlog_item_comments.author_name IS '投稿者名';
COMMENT ON COLUMN undecided_ag.backlog_item_comments.body IS 'コメント本文';
COMMENT ON COLUMN undecided_ag.backlog_item_comments.created_at IS '投稿日時';
