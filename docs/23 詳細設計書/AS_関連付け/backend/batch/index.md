# AS_関連付け バッチ設計

## 概要

AS関連付け事業領域のバッチ処理設計を定義します。
データ初期化、履歴アーカイブ、データ整合性チェックなどのバッチ処理を整理します。

## 参照

- データベース設計: [01 データベース設計](../01%20データベース設計/index.md)
- イベント定義: [イベント一覧](../../22%20要件定義書/AS%20関連付け/05%20実装準備/イベント一覧.tsv)

---

## 1. バッチ処理一覧

| バッチID | バッチ名 | 実行頻度 | 実行タイミング | 説明 |
|----------|---------|----------|---------------|------|
| BAT-AS-001 | データ初期化 | 手動 | システム起動時 | マスターデータ初期化 |
| BAT-AS-002 | 有効期限チェック | 日次 | 毎日00:00 | 有効期限切れのロールを無効化 |
| BAT-AS-003 | 履歴アーカイブ | 月次 | 毎月1日02:00 | 90日前の変更履歴をアーカイブ |
| BAT-AS-004 | データ整合性チェック | 日次 | 毎日01:00 | データ整合性を検証 |
| BAT-AS-005 | 統計情報集計 | 日次 | 毎日03:00 | 統計情報を集計 |
| BAT-AS-006 | 監査ログアーカイブ | 月次 | 毎月1日04:00 | 180日前の監査ログをアーカイブ |

---

## 2. バッチ処理詳細

### 2.1 データ初期化 (BAT-AS-001)

| 項目 | 説明 |
|------|------|
| 目的 | システム起動時にマスターデータを初期化 |
| 入力 | マスターデータCSVファイル |
| 出力 | 初期化結果ログ |
| 処理内容 | |

**処理手順:**

1. パーティー種類マスターを読み込み
2. ロール種類マスターを読み込み
3. 組織種類マスターを読み込み
4. マスターデータをデータベースに挿入
5. 初期化結果をログ出力

**エラー処理:**

| エラー種別 | 処理 |
|------------|------|
| ファイルNotFound | エラーログを出力し、バッチを中止 |
| データフォーマットエラー | エラー行をスキップし、続行 |
| データベースエラー | トランザクションをロールバックし、バッチを中止 |

### 2.2 有効期限チェック (BAT-AS-002)

| 項目 | 説明 |
|------|------|
| 目的 | 有効期限切れのロールを自動的に無効化 |
| 入力 | party_roleテーブル、role_validity_periodテーブル |
| 出力 | 無効化結果ログ |
| 処理内容 | |

**処理手順:**

1. 有効期限が切れたロールを検索
2. 各ロールを無効化（ステータス更新）
3. 変更履歴を記録
4. 無効化結果をログ出力
5. 外部システムに通知イベントを発行

**SQL例:**

```sql
-- 有効期限切れのロールを検索
SELECT pr.id, pr.party_id, pr.role_id, rvp.end_date
FROM party_role pr
JOIN role_validity_period rvp ON pr.id = rvp.party_role_id
WHERE rvp.end_date < CURRENT_DATE
  AND pr.status = 'ACTIVE';

-- 無効化
UPDATE party_role
SET status = 'INACTIVE',
    updated_at = CURRENT_TIMESTAMP
WHERE id IN (上記検索結果);
```

### 2.3 履歴アーカイブ (BAT-AS-003)

| 項目 | 説明 |
|------|------|
| 目的 | 90日前の変更履歴をアーカイブテーブルに移動 |
| 入力 | 各エンティティの変更履歴テーブル |
| 出力 | アーカイブ結果ログ |
| 処理内容 | |

**処理手順:**

1. 90日前の変更履歴を検索
2. アーカイブテーブルにコピー
3. 元テーブルから削除
4. アーカイブ結果をログ出力

**アーカイブ対象テーブル:**

| テーブル名 | アーカイブテーブル名 |
|------------|---------------------|
| party_audit_log | party_audit_log_archive |
| employee_audit_log | employee_audit_log_archive |
| organization_audit_log | organization_audit_log_archive |
| role_audit_log | role_audit_log_archive |

**SQL例:**

```sql
-- アーカイブテーブルにコピー
INSERT INTO party_audit_log_archive
SELECT * FROM party_audit_log
WHERE created_at < CURRENT_DATE - INTERVAL '90 days';

-- 元テーブルから削除
DELETE FROM party_audit_log
WHERE created_at < CURRENT_DATE - INTERVAL '90 days';
```

### 2.4 データ整合性チェック (BAT-AS-004)

| 項目 | 説明 |
|------|------|
| 目的 | データ整合性を検証し、不一致を報告 |
| 入力 | 全テーブル |
| 出力 | 整合性チェック結果レポート |
| 処理内容 | |

**チェック項目:**

| チェックID | チェック名 | 説明 |
|------------|-----------|------|
| CHK-001 | Party-Role整合性 | Partyが存在しないRoleを検出 |
| CHK-002 | Employee-Affiliation整合性 | 従業員が存在しない所属情報を検出 |
| CHK-003 | Organization-Hierarchy整合性 | 組織階層に循環参照がないか確認 |
| CHK-004 | Company-Store整合性 | 会社が存在しない店舗を検出 |
| CHK-005 | 有効期間整合性 | 開始日 > 終了日のレコードを検出 |

**CHK-001 例:**

```sql
-- Partyが存在しないRoleを検出
SELECT pr.id, pr.party_id
FROM party_role pr
LEFT JOIN party p ON pr.party_id = p.id
WHERE p.id IS NULL;
```

**CHK-003 例:**

```sql
-- 組織階層に循環参照がないか確認
WITH RECURSIVE hierarchy AS (
  SELECT id, parent_id, ARRAY[id] AS path
  FROM organization_hierarchy
  WHERE parent_id IS NULL
  
  UNION ALL
  
  SELECT oh.id, oh.parent_id, h.path || oh.id
  FROM organization_hierarchy oh
  JOIN hierarchy h ON oh.parent_id = h.id
)
SELECT * FROM hierarchy
WHERE id = ANY(path);
```

### 2.5 統計情報集計 (BAT-AS-005)

| 項目 | 説明 |
|------|------|
| 目的 | 統計情報を集計してダッシュボードで表示 |
| 入力 | 全テーブル |
| 出力 | 統計情報テーブル |
| 処理内容 | |

**集計項目:**

| 統計ID | 統計名 | 説明 |
|--------|--------|------|
| STAT-001 | 従業員数 | 有効な従業員の件数 |
| STAT-002 | 組織数 | 有効な組織の件数 |
| STAT-003 | 会社数 | 有効な会社の件数 |
| STAT-004 | 店舗数 | 有効な店舗の件数 |
| STAT-005 | ロール付与数 | 有効なロール付与の件数 |
| STAT-006 | 今日の新規登録数 | 今日登録された従業員の件数 |

**SQL例:**

```sql
-- 統計情報を集計
INSERT INTO statistics (stat_date, stat_key, stat_value)
VALUES
  (CURRENT_DATE, 'employee_count', 
   (SELECT COUNT(*) FROM employee WHERE status = 'ACTIVE')),
  (CURRENT_DATE, 'organization_count',
   (SELECT COUNT(*) FROM organization WHERE status = 'ACTIVE')),
  (CURRENT_DATE, 'company_count',
   (SELECT COUNT(*) FROM company WHERE status = 'ACTIVE')),
  (CURRENT_DATE, 'store_count',
   (SELECT COUNT(*) FROM store WHERE status = 'ACTIVE')),
  (CURRENT_DATE, 'role_assignment_count',
   (SELECT COUNT(*) FROM party_role WHERE status = 'ACTIVE')),
  (CURRENT_DATE, 'today_new_employee_count',
   (SELECT COUNT(*) FROM employee WHERE created_at >= CURRENT_DATE));
```

### 2.6 監査ログアーカイブ (BAT-AS-006)

| 項目 | 説明 |
|------|------|
| 目的 | 180日前の監査ログをアーカイブ |
| 入力 | 監査ログテーブル |
| 出力 | アーカイブ結果ログ |
| 処理内容 | |

**処理手順:**

1. 180日前の監査ログを検索
2. 圧縮してアーカイブファイルに保存
3. 元テーブルから削除
4. アーカイブ結果をログ出力

---

## 3. バッチ実行環境

### 3.1 実行方法

| 項目 | 設計 |
|------|------|
| スケジューラー | Spring Batch + Spring Scheduler |
| 実行方法 | 自動実行（スケジュールベース） |
| 手動実行 | 管理画面から手動実行可能 |
| リトライ | 失敗時に最大3回リトライ |

### 3.2 監視・ログ

| 項目 | 設計 |
|------|------|
| 実行ログ | 各バッチの実行開始・終了・結果をログ出力 |
| エラー通知 | 失敗時にメール通知 |
| 実行履歴 | バッチ実行履歴テーブルに記録 |
| パフォーマンス | 実行時間・処理件数をメトリクスとして収集 |

### 3.3 実行履歴管理

| テーブル名 | 説明 |
|------------|------|
| batch_execution_history | バッチ実行履歴 |

**カラム定義:**

| カラム名 | 型 | 説明 |
|----------|-----|------|
| id | BIGSERIAL | 実行ID |
| batch_id | VARCHAR(50) | バッチID |
| batch_name | VARCHAR(100) | バッチ名 |
| start_time | TIMESTAMP | 開始時刻 |
| end_time | TIMESTAMP | 終了時刻 |
| status | VARCHAR(20) | 実行結果（SUCCESS, FAILED） |
| processed_count | INTEGER | 処理件数 |
| error_message | TEXT | エラーメッセージ |

---

## 4. バッチ依存関係

### 4.1 実行順序

| 順序 | バッチID | バッチ名 | 依存関係 |
|------|----------|---------|----------|
| 1 | BAT-AS-001 | データ初期化 | なし |
| 2 | BAT-AS-004 | データ整合性チェック | BAT-AS-001 |
| 3 | BAT-AS-002 | 有効期限チェック | BAT-AS-004 |
| 4 | BAT-AS-005 | 統計情報集計 | BAT-AS-002 |
| 5 | BAT-AS-003 | 履歴アーカイブ | なし |
| 6 | BAT-AS-006 | 監査ログアーカイブ | なし |

### 4.2 依存関係図

```
┌─────────────┐
│ BAT-AS-001  │  データ初期化
└──────┬──────┘
       │
┌──────┴──────┐
│ BAT-AS-004  │  データ整合性チェック
└──────┬──────┘
       │
┌──────┴──────┐
│ BAT-AS-002  │  有効期限チェック
└──────┬──────┘
       │
┌──────┴──────┐
│ BAT-AS-005  │  統計情報集計
└─────────────┘

┌─────────────┐
│ BAT-AS-003  │  履歴アーカイブ（独立）
└─────────────┘

┌─────────────┐
│ BAT-AS-006  │  監査ログアーカイブ（独立）
└─────────────┘
```

---

## 5. エラーハンドリング

### 5.1 共通エラー処理

| エラー種別 | 処理 |
|------------|------|
| データベース接続エラー | リトライ（最大3回）、失敗時はメール通知 |
| データフォーマットエラー | エラー行をスキップし、続行 |
| 権限エラー | エラーログを出力し、バッチを中止 |
| ディスク容量不足 | エラーログを出力し、メール通知 |

### 5.2 リトライポリシー

| 項目 | 設定値 |
|------|--------|
| リトライ回数 | 最大3回 |
| リトライ間隔 | 指数バックオフ（1分、2分、4分） |
| リトライ対象 | 一時的なエラー（データベース接続タイムアウトなど） |

---

## 6. 設計チェックリスト

設計時・実装時に以下の項目を確認してください。

- [ ] バッチ処理一覧が適切に定義されている
- [ ] 各バッチの処理手順が明確である
- [ ] エラーハンドリングが適切に実装されている
- [ ] リトライポリシーが適切に設定されている
- [ ] 監視・ログが適切に実装されている
- [ ] バッチ依存関係が適切に管理されている
- [ ] 実行履歴が適切に記録されている
- [ ] パフォーマンスメトリクスが収集されている
