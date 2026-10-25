# Flyway マイグレーションスクリプト チェックリスト

Flyway マイグレーションスクリプトを作成・変更する際に、必ず以下のチェックリストで確認してください。
このチェックリストは、データ損失や本番環境での障害を防ぐための安全網です。

---

## 1. 基本チェック

| #   | チェック項目                                        | 確認方法                    | 必須/推奨 |
|-----|-----------------------------------------------------|-----------------------------|-----------|
| 1.1 | ファイル名が `V{timestamp}__description.sql` 形式か | ファイル名確認              | 必須      |
| 1.2 | タイムスタンプが一意で、既存ファイルと重複しないか  | `ls db/migration/` で確認   | 必須      |
| 1.3 | 記述言語が SQL か（`.sql` 拡張子）                  | ファイル拡張子確認          | 必須      |
| 1.4 | ファイルエンコーディングが UTF-8 か                 | IDE 設定確認                | 必須      |
| 1.5 | 末尾に改行があるか                                  | `tail -c 1 file.sql \| xxd` | 推奨      |

### 1.1-1.2 タイムスタンプの命名規則

```
V{YYYYMMDDHHmmss}__{description}.sql

例:
V20241226211235__change_column_name_simple_mame_to_simple_name.sql
V20261001110242__drop_table_public_cites.sql
V20261007120000__add_branches_and_branch_master_imports.sql
```

**ルール:**

- タイムスタンプはマイグレーションの作成日時（実行日時ではない）
- 記述部分は英小文字とアンダースコアのみ
- 記述部分は動詞から始める（`add_`, `change_`, `drop_`, `create_` など）

---

## 2. Schema 分離チェック

| #   | チェック項目                                    | 確認方法                                            | 必須/推奨 |
|-----|-------------------------------------------------|-----------------------------------------------------|-----------|
| 2.1 | Schema 名がプロジェクトの命名規則に従っているか | DDL 内の schema 名確認                              | 必須      |
| 2.2 | Schema が存在するか（初回使用時）               | `CREATE SCHEMA IF NOT EXISTS` が precede するか確認 | 必須      |
| 2.3 | クロススキーマ参照が正しいか                    | 参照先の schema 名確認                              | 必須      |

### 2.1 Schema 命名規則

プロジェクトでは以下の Schema を使用します。

| Schema         | 用途                | 例                    |
|----------------|---------------------|-----------------------|
| `undecided_as` | Application Service | parties, persons      |
| `undecided_ef` | Event Store         | events                |
| `undecided_do` | Domain Object       | business entities     |
| `public`       | 共通参照データ      | prefectures（移行中） |

```sql
-- ✓ 正しい: Schema 名を明示
CREATE TABLE undecided_as.parties (
    party_id bigint PRIMARY KEY,
    simple_name varchar(50)
);

-- ✗ 誤り: Schema を指定しない
CREATE TABLE parties (
    party_id bigint PRIMARY KEY,
    simple_name varchar(50)
);
```

---

## 3. DDL 文法チェック

| #   | チェック項目                                     | 確認方法                     | 必須/推奨 |
|-----|--------------------------------------------------|------------------------------|-----------|
| 3.1 | SQL 文の末尾にセミコロンがあるか                 | 各文の末尾確認               | 必須      |
| 3.2 | 大文字小文字の使い分けが一貫しているか           | 予約語=大文字、識別子=小文字 | 推奨      |
| 3.3 | 識別子にクォートが必要ないか                     | 予約語との競合確認           | 必須      |
| 3.4 | 文字列リテラルがシングルクォートで囲まれているか | `'value'` 形式確認           | 必須      |

### 3.2 大文字小文字の慣例

```sql
-- ✓ 推奨: 予約語は大文字、識別子は小文字
ALTER TABLE undecided_as.parties
    ADD COLUMN simple_name varchar(50);

-- 可読性のため、キーワードを大文字にする
CREATE TABLE undecided_as.parties (
    party_id   bigint PRIMARY KEY,
    simple_name varchar(50) NOT NULL,
    created_at  timestamp DEFAULT CURRENT_TIMESTAMP
);
```

---

## 4. データ保持チェック（最重要）

| #   | チェック項目                               | 確認方法           | 必須/推奨 |
|-----|--------------------------------------------|--------------------|-----------|
| 4.1 | 既存データが失くならないか                 | 変更内容の分析     | 必須      |
| 4.2 | カラム名変更は3段階手順か                  | 追加→コピー→削除   | 必須      |
| 4.3 | カラム型変更は後方互換性があるか           | 型変換表確認       | 必須      |
| 4.4 | NOT NULL 制約追加前に NULL 値を補完するか  | UPDATE 文確認      | 必須      |
| 4.5 | テーブル削除前にデータバックアップがあるか | バックアップ文確認 | 必須      |

### 4.1 データ損失リスクのある操作

以下の操作は **必ず**データ保持手順を確認してください。

| 操作                   | リスク           | 安全な手順                             |
|------------------------|------------------|----------------------------------------|
| カラム名変更           | データ消失       | 新カラム追加→データコピー→旧カラム削除 |
| カラム型変更（縮小系） | 桁落ち・切り捨て | USING 句で変換式指定                   |
| NOT NULL 制約追加      | エラー発生       | NULL 値補完→制約追加                   |
| テーブル削除           | データ消失       | バックアップ→DROP                      |
| CHECK 制約追加         | エラー発生       | 既存データ検証→制約追加                |

### 4.2 カラム名変更の3段階手順

```sql
-- ✓ 正しい: 3段階手順
-- ステップ1: 新しいカラムを追加
ALTER TABLE undecided_as.parties
    ADD COLUMN simple_name varchar(50);

-- ステップ2: 既存データをコピー
UPDATE undecided_as.parties
SET simple_name = simple_mame
WHERE true;

-- ステップ3: 制約を設定
ALTER TABLE undecided_as.parties
    ALTER COLUMN simple_name SET NOT NULL;

-- ステップ4: 古いカラムを削除
ALTER TABLE undecided_as.parties
    DROP COLUMN simple_mame;
```

### 4.3 カラム型変更の安全性確認

**後方互換性のある変更（安全）:**
| 変更前 | 変更後 | 備考 |
|--------|--------|------|
| `varchar(50)` | `varchar(100)` | 拡張系 |
| `integer` | `bigint` | 拡張系 |
| `smallint` | `integer` | 拡張系 |
| `varchar` | `text` | 拡張系 |
| `timestamp` | `timestamptz` | UTC変換 |

**データ損失のリスクがある変更（注意）:**
| 変更前 | 変更後 | 変換式 |
|--------|--------|--------|
| `varchar(100)` | `varchar(50)` | `USING substring(col, 1, 50)` |
| `bigint` | `integer` | `USING col::integer`（範囲外エラーのリスク） |
| `text` | `varchar(50)` | `USING substring(col, 1, 50)` |
| `numeric(10,2)` | `numeric(5,2)` | `USING col::numeric(5,2)`（桁落ちのリスク） |

---

## 5. 制約チェック

| #   | チェック項目                                           | 確認方法          | 必須/推奨 |
|-----|--------------------------------------------------------|-------------------|-----------|
| 5.1 | PRIMARY KEY が定義されているか                         | テーブル定義確認  | 必須      |
| 5.2 | NOT NULL 制約が適切に設定されているか                  | カラム定義確認    | 必須      |
| 5.3 | UNIQUE 制約が必要なカラムに設定されているか            | 業務ルール確認    | 必須      |
| 5.4 | CHECK 制約を追加する場合、既存データが違反していないか | 検証クエリ実行    | 必須      |
| 5.5 | 外部キー制約は Flyway で定義しない（プロジェクト方針） | FK 制約の不在確認 | 必須      |

### 5.1 主キーの定義

```sql
-- ✓ 推奨: 単一カラムの Snowflake ID を主キー
CREATE TABLE undecided_as.parties (
    party_id bigint PRIMARY KEY,
    simple_name varchar(50) NOT NULL
);

-- 複合主キーが必要な場合は @IdClass または @EmbeddedId で対応
CREATE TABLE undecided_as.branch_master_imports (
    branch_code varchar(3) NOT NULL,
    fiscal_year integer NOT NULL,
    month integer NOT NULL,
    sequence integer NOT NULL,
    PRIMARY KEY (branch_code, fiscal_year, month, sequence)
);
```

### 5.5 外部キー制約の定義（プロジェクト方針）

```sql
-- ✗ プロジェクトでは外部キー制約を Flyway で定義しない
-- 整合性はアプリケーション層で管理

CREATE TABLE undecided_as.parties (
    party_id bigint PRIMARY KEY,
    -- 外部キー制約は定義しない
    -- CONSTRAINT fk_parties_branch
    -- FOREIGN KEY (branch_code) REFERENCES undecided_as.branches(branch_code)
);
```

---

## 6. インデックスチェック

| #   | チェック項目                                     | 確認方法                         | 必須/推奨 |
|-----|--------------------------------------------------|----------------------------------|-----------|
| 6.1 | 検索頻度の高いカラムにインデックスがあるか       | クエリ分析                       | 推奨      |
| 6.2 | インデックス名が命名規則に従っているか           | `idx_`, `uq_` プレフィックス確認 | 必須      |
| 6.3 | 一意インデックスが必要なカラムに設定されているか | 業務ルール確認                   | 必須      |
| 6.4 | 複合インデックスの順序が適切か                   | クエリの WHERE 句確認            | 推奨      |

### 6.2 インデックス命名規則

| 種類             | プレフィックス | 例                        |
|------------------|----------------|---------------------------|
| 通常インデックス | `idx_`         | `idx_parties_simple_name` |
| 一意インデックス | `uq_`          | `uq_parties_email`        |
| 主キー           | `pk_`          | `pk_parties`（暗黙）      |

```sql
-- 通常インデックス
CREATE INDEX idx_parties_simple_name
    ON undecided_as.parties (simple_name);

-- 一意インデックス
CREATE UNIQUE INDEX uq_parties_email
    ON undecided_as.parties (email);

-- 複合インデックス
CREATE INDEX idx_parties_status_created
    ON undecided_as.parties (status_code, created_at);

-- 部分インデックス
CREATE INDEX idx_parties_active
    ON undecided_as.parties (party_id)
    WHERE status_code = 'ACTIVE';
```

---

## 7. コメントチェック

| #   | チェック項目                                  | 確認方法                 | 必須/推奨 |
|-----|-----------------------------------------------|--------------------------|-----------|
| 7.1 | テーブルコメントが設定されているか            | `COMMENT ON TABLE` 確認  | 必須      |
| 7.2 | カラムコメントが設定されているか              | `COMMENT ON COLUMN` 確認 | 必須      |
| 7.3 | コメントが日本語で記述されているか            | コメント内容確認         | 必須      |
| 7.4 | View / Materialized View にもコメントがあるか | `COMMENT ON VIEW` 確認   | 必須      |

### 7.1 テーブル・カラムコメント

```sql
-- テーブルコメント
CREATE TABLE undecided_as.parties (
    party_id bigint PRIMARY KEY,
    simple_name varchar(50) NOT NULL
);

COMMENT ON TABLE undecided_as.parties IS '取引先マスター';

-- カラムコメント
COMMENT ON COLUMN undecided_as.parties.party_id IS '取引先ID（Snowflake ID）';
COMMENT ON COLUMN undecided_as.parties.simple_name IS '取引先名（簡易名）';
```

### 7.4 View / Materialized View コメント

```sql
-- View コメント
COMMENT ON VIEW undecided_as.v_party_summary IS '取引先サマリービュー';
COMMENT ON COLUMN undecided_as.v_party_summary.party_name IS '取引先名';

-- Materialized View コメント
COMMENT ON MATERIALIZED VIEW undecided_as.mv_party_stats IS '取引先統計マテリアライズドビュー';
COMMENT ON COLUMN undecided_as.mv_party_stats.party_count IS '取引先数';
```

---

## 8. トランザクションチェック

| #   | チェック項目                   | 確認方法                    | 必須/推奨 |
|-----|--------------------------------|-----------------------------|-----------|
| 8.1 | DDL 操作がアトミックか         | Flyway のデフォルト動作確認 | 必須      |
| 8.2 | DML と DDL の混在が適切か      | 文の順序確認                | 推奨      |
| 8.3 | 失敗時のロールバックが可能なか | トランザクション境界確認    | 必須      |

### 8.1 Flyway のトランザクション動作

Flyway はデフォルトで各マイグレーションをトランザクション内で実行します。

```sql
-- Flyway はこのスクリプト全体を1つのトランザクションで実行
-- 途中でエラーが発生すると、すべての変更がロールバックされる

ALTER TABLE undecided_as.parties
    ADD COLUMN simple_name varchar(50);

UPDATE undecided_as.parties
SET simple_name = simple_mame
WHERE true;

-- ここでエラーが発生すると、ADD COLUMN もロールバックされる
ALTER TABLE undecided_as.parties
    ALTER COLUMN simple_name SET NOT NULL;
```

**注意:** PostgreSQL の一部の DDL 文（`CREATE INDEX CONCURRENTLY` など）はトランザクション内で実行できません。

---

## 9. 命名規則チェック

| #   | チェック項目                                      | 確認方法                  | 必須/推奨 |
|-----|---------------------------------------------------|---------------------------|-----------|
| 9.1 | テーブル名が蛇_case（snake_case）か               | 名前確認                  | 必須      |
| 9.2 | カラム名が蛇_case（snake_case）か                 | 名前確認                  | 必須      |
| 9.3 | インデックス名がプレフィックス付きか              | `idx_`, `uq_` 確認        | 必須      |
| 9.4 | 制約名がプレフィックス付きか                      | `chk_`, `uq_`, `fk_` 確認 | 必須      |
| 9.5 | View 名が `v_` プレフィックス付きか               | 名前確認                  | 必須      |
| 9.6 | Materialized View 名が `mv_` プレフィックス付きか | 名前確認                  | 必須      |

### 9.1-9.6 命名規則一覧

| オブジェクト      | 命名規則                       | 例                        |
|-------------------|--------------------------------|---------------------------|
| テーブル          | `snake_case`（複数形推奨）     | `parties`, `branches`     |
| カラム            | `snake_case`                   | `party_id`, `simple_name` |
| インデックス      | `idx_` + テーブル名 + カラム名 | `idx_parties_simple_name` |
| 一意インデックス  | `uq_` + テーブル名 + カラム名  | `uq_parties_email`        |
| CHECK 制約        | `chk_` + テーブル名 + カラム名 | `chk_parties_status_code` |
| UNIQUE 制約       | `uq_` + テーブル名 + カラム名  | `uq_parties_email`        |
| View              | `v_` + 名前                    | `v_party_summary`         |
| Materialized View | `mv_` + 名前                   | `mv_party_stats`          |

---

## 10. 型定義チェック

| #    | チェック項目                                   | 確認方法            | 必須/推奨 |
|------|------------------------------------------------|---------------------|-----------|
| 10.1 | 文字列型に長さ指定があるか                     | `varchar(n)` 確認   | 必須      |
| 10.2 | 数値型に桁数指定があるか                       | `numeric(p,s)` 確認 | 必須      |
| 10.3 | 日時型が `timestamp` または `timestamptz` か   | 型確認              | 必須      |
| 10.4 | ID カラムが `bigint` か（Snowflake ID 使用時） | 型確認              | 必須      |
| 10.5 | UUID カラムが `uuid` 型か                      | 型確認              | 必須      |

### 10.1 推奨データ型

| 用途                   | 推奨型               | 例                            |
|------------------------|----------------------|-------------------------------|
| 主キー（Snowflake ID） | `bigint`             | `party_id bigint PRIMARY KEY` |
| 主キー（UUID v7）      | `uuid`               | `thing_id uuid PRIMARY KEY`   |
| 文字列（固定長）       | `varchar(n)`         | `simple_name varchar(50)`     |
| 文字列（可変長）       | `text`               | `description text`            |
| 数値（整数）           | `integer` / `bigint` | `quantity integer`            |
| 数値（小数）           | `numeric(p,s)`       | `price numeric(10,2)`         |
| 日時                   | `timestamp`          | `created_at timestamp`        |
| 論理値                 | `boolean`            | `is_active boolean`           |
| JSON                   | `jsonb`              | `metadata jsonb`              |

---

## 11. 監査フィールドチェック

| #    | チェック項目                                     | 確認方法                           | 必須/推奨 |
|------|--------------------------------------------------|------------------------------------|-----------|
| 11.1 | 監査フィールドが必要なテーブルに設定されているか | カラム定義確認                     | 推奨      |
| 11.2 | `created_at` が `DEFAULT CURRENT_TIMESTAMP` か   | デフォルト値確認                   | 必須      |
| 11.3 | `updated_at` が適切に更新される仕組みがあるか    | トリガーまたはアプリケーション確認 | 推奨      |

### 11.1 監査フィールドの定義

```sql
CREATE TABLE undecided_as.parties (
    party_id    bigint PRIMARY KEY,
    simple_name varchar(50) NOT NULL,
    -- 監査フィールド
    created_at  timestamp DEFAULT CURRENT_TIMESTAMP,
    created_by  varchar(50),
    updated_at  timestamp,
    updated_by  varchar(50)
);

COMMENT ON COLUMN undecided_as.parties.created_at IS '作成日時';
COMMENT ON COLUMN undecided_as.parties.created_by IS '作成者';
COMMENT ON COLUMN undecided_as.parties.updated_at IS '更新日時';
COMMENT ON COLUMN undecided_as.parties.updated_by IS '更新者';
```

**注意:** プロジェクトでは `created_at`, `created_by`, `updated_at`, `updated_by` は JPA エンティティの
`@CreatedDate`, `@LastModifiedDate` などで管理され、Flyway DDL ではカラム定義のみ行います。

---

## 12. View / Materialized View チェック

| #    | チェック項目                                                  | 確認方法         | 必須/推奨 |
|------|---------------------------------------------------------------|------------------|-----------|
| 12.1 | View の参照元テーブルが存在するか                             | 依存関係確認     | 必須      |
| 12.2 | Materialized View に UNIQUE INDEX があるか（CONCURRENTLY 用） | インデックス確認 | 推奨      |
| 12.3 | View / Materialized View にコメントが設定されているか         | コメント確認     | 必須      |
| 12.4 | View のカラム名が明確か                                       | SELECT 句確認    | 推奨      |

### 12.1 View の定義

```sql
-- View の定義
CREATE OR REPLACE VIEW undecided_as.v_party_summary AS
SELECT
    p.party_id,
    p.simple_name,
    p.status_code,
    p.created_at
FROM undecided_as.parties p
WHERE p.status_code = 'ACTIVE';

-- View コメント
COMMENT ON VIEW undecided_as.v_party_summary IS 'アクティブな取引先のサマリー';
```

### 12.2 Materialized View の定義

```sql
-- Materialized View の定義
CREATE MATERIALIZED VIEW undecided_as.mv_party_stats AS
SELECT
    p.status_code,
    count(*) AS party_count,
    max(p.created_at) AS last_created_at
FROM undecided_as.parties p
GROUP BY p.status_code;

-- CONCURRENTLY リフレッシュ用の UNIQUE INDEX
CREATE UNIQUE INDEX uq_mv_party_stats_status
    ON undecided_as.mv_party_stats (status_code);

-- Materialized View コメント
COMMENT ON MATERIALIZED VIEW undecided_as.mv_party_stats IS '取引先ステータス別統計';
```

---

## 13. 履歴テーブルチェック

| #    | チェック項目                                                     | 確認方法                      | 必須/推奨 |
|------|------------------------------------------------------------------|-------------------------------|-----------|
| 13.1 | 履歴テーブル名に `_history` サフィックスを付けているか           | テーブル名確認                | 必須      |
| 13.2 | 履歴テーブルに期間カラムがあるか                                 | `valid_from`, `valid_to` 確認 | 必須      |
| 13.3 | 履歴テーブルにインデックスが設定されているか                     | インデックス確認              | 必須      |
| 13.4 | 履歴テーブルにコメントが設定されているか                         | コメント確認                  | 必須      |
| 13.5 | トリガーによる自動履歴管理の場合、トリガー関数が定義されているか | トリガー確認                  | 必須      |

### 13.1 履歴テーブルの命名規約

**履歴テーブルのテーブル名は、必ず `_history` サフィックスを付けること。**

```sql
-- ✓ 正しい: _history サフィックス付き
CREATE TABLE undecided_as.parties_history (
    party_id    bigint NOT NULL,
    simple_name varchar(50) NOT NULL,
    valid_from  timestamp NOT NULL,
    valid_to    timestamp
);

-- ✗ 誤り: サフィックスなし
CREATE TABLE undecided_as.party_hist (
    ...
);

-- ✗ 誤り: プレフィックス使用
CREATE TABLE undecided_as.history_parties (
    ...
);
```

**命名規則:**

| 項目             | 規則                                  |
|------------------|---------------------------------------|
| **サフィックス** | `_history`                            |
| **命名形式**     | `{元のテーブル名}_history`            |
| **例**           | `parties` → `parties_history`         |
| **例**           | `address_use` → `address_use_history` |

### 13.2 履歴テーブルの定義

```sql
-- 履歴テーブルの定義
CREATE TABLE undecided_as.parties_history (
    party_id    bigint NOT NULL,
    simple_name varchar(50) NOT NULL,
    status_code varchar(20),
    -- 期間カラム（SCD Type 2）
    valid_from  timestamp NOT NULL,
    valid_to    timestamp,
    -- 変更情報
    changed_at  timestamp DEFAULT CURRENT_TIMESTAMP,
    changed_by  varchar(50)
);

-- 複合インデックス（期間検索用）
CREATE INDEX idx_parties_history_party_valid
    ON undecided_as.parties_history (party_id, valid_from, valid_to);

-- コメント
COMMENT ON TABLE undecided_as.parties_history IS '取引先履歴';
COMMENT ON COLUMN undecided_as.parties_history.valid_from IS '有効開始日時';
COMMENT ON COLUMN undecided_as.parties_history.valid_to IS '有効終了日時（NULL=現在有効）';
```

---

## 14. TRUNCATE チェック

| #    | チェック項目                                       | 確認方法         | 必須/推奨 |
|------|----------------------------------------------------|------------------|-----------|
| 14.1 | TRUNCATE の対象テーブルが正しいか                  | テーブル名確認   | 必須      |
| 14.2 | CASCADE の影響範囲を理解しているか                 | 外部キー参照確認 | 必須      |
| 14.3 | テストデータ用か本番データ用か明確か               | コメント確認     | 必須      |
| 14.4 | JPA から呼び出す場合、EntityManager の使用が適切か | コード確認       | 必須      |

### 14.1 安全な TRUNCATE

```sql
-- ✓ 単一テーブルの TRUNCATE
TRUNCATE TABLE undecided_as.parties;

-- ✓ ID シーケンスをリセットする場合
TRUNCATE TABLE undecided_as.parties RESTART IDENTITY;

-- ✓ 依存テーブルも同時に TRUNCATEする場合
TRUNCATE TABLE undecided_as.parties,
           undecided_as.party_addresses
    RESTART IDENTITY
    CASCADE;

-- ✗ 本番環境での無条件 TRUNCATE は避ける
-- TRUNCATE TABLE undecided_as.parties; -- データがすべて消失する
```

---

## 15. REINDEX チェック

| #    | チェック項目                                | 確認方法             | 必須/推奨 |
|------|---------------------------------------------|----------------------|-----------|
| 15.1 | REINDEX の対象インデックスが正しいか        | インデックス名確認   | 必須      |
| 15.2 | CONCURRENTLY オプションの必要性を確認したか | 運用時間帯確認       | 推奨      |
| 15.3 | VACUUM ANALYZE の実行タイミングを考慮したか | メンテナンス計画確認 | 推奨      |

### 15.1 REINDEX の使用

```sql
-- インデックスの再構築
REINDEX INDEX idx_parties_simple_name;

-- テーブルの全インデックスを再構築
REINDEX TABLE undecided_as.parties;

-- Schema の全インデックスを再構築
REINDEX SCHEMA undecided_as;

-- 排他ロックなしで再構築（CONCURRENTLY）
REINDEX INDEX CONCURRENTLY idx_parties_simple_name;
```

---

## 16. Flyway 設定チェック

| #    | チェック項目                                   | 確認方法               | 必須/推奨 |
|------|------------------------------------------------|------------------------|-----------|
| 16.1 | `ddl-auto: none` が設定されているか            | `application.yml` 確認 | 必須      |
| 16.2 | `baseline-on-migrate: true` が設定されているか | `application.yml` 確認 | 推奨      |
| 16.3 | `create_namespaces: true` が設定されているか   | `application.yml` 確認 | 推奨      |
| 16.4 | `locations` が正しいパスを指しているか         | `application.yml` 確認 | 必須      |

### 16.1 推奨 Flyway 設定

```yaml
# application.yml
spring:
  flyway:
    enabled: true
    baseline-on-migrate: true
    create-namespaces: true
    locations: classpath:db/migration
  jpa:
    hibernate:
      ddl-auto: none  # Flyway が DDL の唯一のソース
```

---

## 17. テスト環境での検証チェック

| #    | チェック項目                             | 確認方法             | 必須/推奨 |
|------|------------------------------------------|----------------------|-----------|
| 17.1 | テスト環境でマイグレーションを実行したか | 実行ログ確認         | 必須      |
| 17.2 | 既存データの整合性を確認したか           | 検証クエリ実行       | 必須      |
| 17.3 | アプリケーションの起動テストを行ったか   | 起動ログ確認         | 必須      |
| 17.4 | ロールバック手順を準備したか             | 復旧ドキュメント確認 | 必須      |

### 17.1 テスト環境での検証手順

1. テスト環境のデータベースを本番環境の最新状態に同期
2. マイグレーションスクリプトを適用
3. 以下の検証クエリを実行
   ```sql
   -- テーブル構造の確認
   \d+ undecided_as.parties

   -- データ件数の確認
   SELECT count(*) FROM undecided_as.parties;

   -- NULL 値の確認
   SELECT count(*) FROM undecided_as.parties WHERE simple_name IS NULL;

   -- コメントの確認
   SELECT obj_description('undecided_as.parties'::regclass);
   ```
4. アプリケーションを起動して動作確認
5. 問題がないことを確認してから本番環境に適用

---

## 18. マイグレーションスクリプトの修正時チェック

マイグレーションスクリプトを **修正**する場合は、以下の点に注意してください。

| #    | チェック項目                                                           | 確認方法                      | 必須/推奨 |
|------|------------------------------------------------------------------------|-------------------------------|-----------|
| 18.1 | 修正するスクリプトがまだ本番環境に適用されていないか                   | `schema_version` テーブル確認 | 必須      |
| 18.2 | 既に適用済みのスクリプトを修正する場合は、新しいスクリプトを作成するか | 適用済み確認                  | 必須      |
| 18.3 | チェックサムの変更が意図的か                                           | Flyway のエラーメッセージ確認 | 必須      |

### 18.1 適用済みスクリプトの修正

**重要なルール:** 既に本番環境に適用済みのマイグレーションスクリプトは **修正しないでください**。

```
-- ✓ 正しい: 新しいスクリプトを作成
V20261025071200__fix_party_name_length.sql

-- ✗ 誤り: 適用済みのスクリプトを修正
V20241226211235__change_column_name_simple_mame_to_simple_name.sql
（このスクリプトは既に本番環境に適用済み）
```

### 18.2 チェックサム不一致の処理

Flyway はマイグレーションスクリプトのチェックサムを管理しています。適用済みのスクリプトを修正すると、以下のエラーが発生します。

```
MismatchedChecksumException: Migration checksum mismatch
for migration V20241226211235__change_column_name_simple_mame_to_simple_name.sql
```

**解決方法:**

1. **推奨:** 新しいマイグレーションスクリプトを作成して修正を適用
2. **開発環境のみ:** `flyway repair` でチェックサムを再計算（本番環境では使用禁止）

---

## 19. 最終確認チェックリスト

マイグレーションスクリプトをコミットする前に、以下の項目をすべて確認してください。

- [ ] **基本:** ファイル名、タイムスタンプ、拡張子が正しい
- [ ] **Schema:** Schema 名が命名規則に従っている
- [ ] **DDL:** SQL 文法が正しい
- [ ] **データ保持:** 既存データが失くならない
- [ ] **制約:** 主キー、NOT NULL、UNIQUE が適切
- [ ] **インデックス:** 必要なインデックスが設定されている
- [ ] **コメント:** テーブル・カラム・View コメントが設定されている
- [ ] **トランザクション:** アトミックな操作が保証されている
- [ ] **命名規則:** すべての識別子が命名規則に従っている
- [ ] **型定義:** データ型が適切に指定されている
- [ ] **監査フィールド:** 必要な監査フィールドが定義されている
- [ ] **View/MV:** 参照元テーブルが存在し、コメントが設定されている
- [ ] **履歴テーブル:** `_history` サフィックス付き、期間カラムとインデックスが設定されている
- [ ] **TRUNCATE:** 対象テーブルと CASCADE の影響範囲が明確
- [ ] **REINDEX:** 対象インデックスと CONCURRENTLY の必要性を確認
- [ ] **Flyway 設定:** `ddl-auto: none` が設定されている
- [ ] **テスト:** テスト環境で検証済み
- [ ] **修正:** 適用済みスクリプトを修正していない

---

## 参考リンク

- [Flyway 公式ドキュメント](https://documentation.red-gate.com/flyway)
- [PostgreSQL ALTER TABLE](https://www.postgresql.org/docs/current/sql-altertable.html)
- [PostgreSQL CREATE INDEX](https://www.postgresql.org/docs/current/sql-createindex.html)
- [JPA ガイドライン - Flyway と JPA の連携](./jpa.guidelines.md#16-flyway-と-jpa-の連携)
