-- 帳票テーブルを作成します。
-- ファイルメタデータ、バージョン管理、論理削除フラグを管理します。
-- バックログとの紐付けは requirementBacklog 側のテーブルで管理します（段階3で追加）。

CREATE TABLE undecided_ef.documents
(
    id                   BIGINT       NOT NULL,
    file_name            VARCHAR(255) NOT NULL,
    content_type         VARCHAR(100) NOT NULL,
    file_size            BIGINT       NOT NULL,
    file_type            VARCHAR(20)  NOT NULL,
    version              INTEGER      NOT NULL DEFAULT 1,
    s3_key               VARCHAR(1024) NOT NULL,
    uploader_employee_id BIGINT,
    uploader_name        VARCHAR(100),
    deleted              BOOLEAN      NOT NULL DEFAULT FALSE,
    deleted_at           TIMESTAMP,
    version_col          BIGINT,
    created_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT ck_documents_file_type CHECK (file_type IN ('IMAGE', 'PDF', 'TEXT', 'DOCUMENT')),
    CONSTRAINT ck_documents_version CHECK (version >= 1),
    CONSTRAINT ck_documents_file_size CHECK (file_size > 0)
);

CREATE INDEX idx_documents_file_name ON undecided_ef.documents (file_name);
CREATE INDEX idx_documents_uploader_employee_id ON undecided_ef.documents (uploader_employee_id);
CREATE INDEX idx_documents_deleted ON undecided_ef.documents (deleted);
CREATE INDEX idx_documents_deleted_at ON undecided_ef.documents (deleted_at);

COMMENT ON TABLE undecided_ef.documents IS '帳票テーブル。ファイルメタデータとバージョンを管理する';
COMMENT ON COLUMN undecided_ef.documents.id IS '帳票の一意な識別子（Snowflake ID）';
COMMENT ON COLUMN undecided_ef.documents.file_name IS 'ファイル名（元のファイル名）';
COMMENT ON COLUMN undecided_ef.documents.content_type IS 'Content-Type（MIME タイプ）';
COMMENT ON COLUMN undecided_ef.documents.file_size IS 'ファイルサイズ（バイト数）';
COMMENT ON COLUMN undecided_ef.documents.file_type IS 'ファイル種別区分値（IMAGE/PDF/TEXT/DOCUMENT）';
COMMENT ON COLUMN undecided_ef.documents.version IS 'バージョン番号（1 始まり）';
COMMENT ON COLUMN undecided_ef.documents.s3_key IS 'MinIO 上のオブジェクトキー。{documentId}/v{version}/{fileName} 形式';
COMMENT ON COLUMN undecided_ef.documents.uploader_employee_id IS 'アップローダーの従業員 ID';
COMMENT ON COLUMN undecided_ef.documents.uploader_name IS 'アップローダー名';
COMMENT ON COLUMN undecided_ef.documents.deleted IS '論理削除フラグ';
COMMENT ON COLUMN undecided_ef.documents.deleted_at IS '論理削除日時';
COMMENT ON COLUMN undecided_ef.documents.version_col IS '楽観ロック用バージョン列';
COMMENT ON COLUMN undecided_ef.documents.created_at IS '作成日時';
COMMENT ON COLUMN undecided_ef.documents.updated_at IS '更新日時';
