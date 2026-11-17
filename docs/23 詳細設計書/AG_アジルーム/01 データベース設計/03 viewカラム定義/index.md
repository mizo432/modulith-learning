# Viewカラム定義

## 概要

このディレクトリには、アジルーム（Agiloom）事業領域の View 定義を格納します。
すべての View は `undecided_ag` スキーマに配置されます。

## 命名規則

- View 名は `v_<ビュー名>` の形式で命名
- ダッシュボード用集計ビューは `v_<対象>_dashboard` の形式

## 定義済み View

| No. | View 名               | 説明                                     |
|:----|:----------------------|:-----------------------------------------|
| 1   | `v_product_summary`   | プロダクト概要（チーム数、ストーリー数） |
| 2   | `v_sprint_burndown`   | スプリントバーンダウンデータ             |
| 3   | `v_product_dashboard` | プロダクトダッシュボード用集計           |
| 4   | `v_sprint_dashboard`  | スプリントダッシュボード用集計           |
| 5   | `v_epic_dashboard`    | エピックダッシュボード用集計             |
| 6   | `v_team_velocity`     | チームベロシティ推移                     |
| 7   | `v_backlog_summary`   | バックログサマリー                       |

---

## 1. v_product_summary（プロダクト概要）

プロダクトごとの概要情報を集計します。

| カラム名              | 型             | 説明                 |
|:----------------------|:---------------|:---------------------|
| `product_id`          | `BIGINT`       | プロダクトID         |
| `product_code`        | `VARCHAR(20)`  | プロダクトコード     |
| `name`                | `VARCHAR(100)` | プロダクト名         |
| `status`              | `VARCHAR(20)`  | 状態                 |
| `team_count`          | `INTEGER`      | チーム数             |
| `member_count`        | `INTEGER`      | メンバー数           |
| `epic_count`          | `INTEGER`      | エピック数           |
| `story_count`         | `INTEGER`      | ユーザーストーリー数 |
| `active_sprint_count` | `INTEGER`      | 進行中スプリント数   |
| `release_count`       | `INTEGER`      | リリース数           |
| `created_at`          | `TIMESTAMP`    | 作成日時             |

---

## 2. v_sprint_burndown（スプリントバーンダウン）

スプリントのバーンダウン計算用データを提供します。

| カラム名           | 型        | 説明                       |
|:-------------------|:----------|:---------------------------|
| `sprint_id`        | `BIGINT`  | スプリントID               |
| `product_id`       | `BIGINT`  | プロダクトID               |
| `sprint_number`    | `INTEGER` | スプリント番号             |
| `date`             | `DATE`    | 日付                       |
| `remaining_points` | `INTEGER` | 残りのストーリーポイント   |
| `completed_points` | `INTEGER` | 完了したストーリーポイント |
| `total_points`     | `INTEGER` | 総ストーリーポイント       |

---

## 3. v_product_dashboard（プロダクトダッシュボード）

プロダクトレベルのダッシュボード表示用集計データです。

| カラム名              | 型             | 説明                     |
|:----------------------|:---------------|:-------------------------|
| `product_id`          | `BIGINT`       | プロダクトID             |
| `product_code`        | `VARCHAR(20)`  | プロダクトコード         |
| `name`                | `VARCHAR(100)` | プロダクト名             |
| `total_stories`       | `INTEGER`      | 総ストーリー数           |
| `backlog_stories`     | `INTEGER`      | バックログ中ストーリー数 |
| `in_progress_stories` | `INTEGER`      | 進行中ストーリー数       |
| `done_stories`        | `INTEGER`      | 完了ストーリー数         |
| `total_points`        | `INTEGER`      | 総ストーリーポイント     |
| `completed_points`    | `INTEGER`      | 完了ストーリーポイント   |
| `completion_rate`     | `DECIMAL(5,1)` | 完了率（%）              |
| `active_sprints`      | `INTEGER`      | 進行中スプリント数       |

---

## 4. v_sprint_dashboard（スプリントダッシュボード）

スプリントレベルのダッシュボード表示用集計データです。

| カラム名           | 型             | 説明                      |
|:-------------------|:---------------|:--------------------------|
| `sprint_id`        | `BIGINT`       | スプリントID              |
| `product_id`       | `BIGINT`       | プロダクトID              |
| `sprint_number`    | `INTEGER`      | スプリント番号            |
| `name`             | `VARCHAR(100)` | スプリント名              |
| `status`           | `VARCHAR(20)`  | 状態                      |
| `start_date`       | `DATE`         | 開始日                    |
| `end_date`         | `DATE`         | 終了日                    |
| `total_tasks`      | `INTEGER`      | 総タスク数                |
| `completed_tasks`  | `INTEGER`      | 完了タスク数              |
| `total_points`     | `INTEGER`      | 総ストーリーポイント      |
| `completed_points` | `INTEGER`      | 完了ストーリーポイント    |
| `days_remaining`   | `INTEGER`      | 残り日数                  |
| `velocity`         | `DECIMAL(5,1)` | ベロシティ（ポイント/日） |

---

## 5. v_epic_dashboard（エピックダッシュボード）

エピックレベルのダッシュボード表示用集計データです。

| カラム名           | 型             | 説明           |
|:-------------------|:---------------|:---------------|
| `epic_id`          | `BIGINT`       | エピックID     |
| `product_id`       | `BIGINT`       | プロダクトID   |
| `epic_code`        | `VARCHAR(30)`  | エピックコード |
| `name`             | `VARCHAR(200)` | エピック名     |
| `status`           | `VARCHAR(20)`  | 状態           |
| `priority`         | `INTEGER`      | 優先度         |
| `feature_count`    | `INTEGER`      | フィーチャー数 |
| `story_count`      | `INTEGER`      | ストーリー数   |
| `estimated_points` | `INTEGER`      | 推定ポイント   |
| `completed_points` | `INTEGER`      | 完了ポイント   |
| `completion_rate`  | `DECIMAL(5,1)` | 完了率（%）    |

---

## 6. v_team_velocity（チームベロシティ推移）

チームのベロシティ推移を集計します。

| カラム名           | 型             | 説明             |
|:-------------------|:---------------|:-----------------|
| `product_id`       | `BIGINT`       | プロダクトID     |
| `sprint_number`    | `INTEGER`      | スプリント番号   |
| `committed_points` | `INTEGER`      | コミットポイント |
| `completed_points` | `INTEGER`      | 完了ポイント     |
| `velocity`         | `DECIMAL(5,1)` | ベロシティ       |
| `end_date`         | `DATE`         | スプリント終了日 |

---

## 7. v_backlog_summary（バックログサマリー）

プロダクトバックログのサマリー情報を提供します。

| カラム名             | 型        | 説明                 |
|:---------------------|:----------|:---------------------|
| `product_id`         | `BIGINT`  | プロダクトID         |
| `total_stories`      | `INTEGER` | 総ストーリー数       |
| `high_priority`      | `INTEGER` | 高優先度ストーリー数 |
| `medium_priority`    | `INTEGER` | 中優先度ストーリー数 |
| `low_priority`       | `INTEGER` | 低優先度ストーリー数 |
| `total_points`       | `INTEGER` | 総ストーリーポイント |
| `assigned_stories`   | `INTEGER` | 担当者割り当て済み数 |
| `unassigned_stories` | `INTEGER` | 未割り当て数         |

---

## 参照

- 情報一覧: `docs/22 要件定義書/06 機能要件/AG アジルーム/04.システム/01.情報一覧.md`
- データ設計スキル: `.agents/skills/data-design/SKILL.md`
