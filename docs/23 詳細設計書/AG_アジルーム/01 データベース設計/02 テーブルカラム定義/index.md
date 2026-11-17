# テーブルカラム定義

## 概要

このディレクトリには、アジルーム（Agiloom）事業領域のテーブルごとのカラム定義を格納します。
すべてのテーブルは `undecided_ag` スキーマに配置されます。

## 命名規則

- テーブル名はスネークケース、複数形で命名（例: `products`, `user_stories`）
- カラム名はスネークケースで命名
- 外部キーは `<関連名>_id` の形式（例: `product_id`, `sprint_id`）
- 履歴管理が必要なテーブルは `history_` プレフィックスの履歴テーブルを併設

## 標準カラム

全テーブルに以下の標準カラムを適用します：

| カラム名     | 型          | 制約                                 | 説明         |
|:-------------|:------------|:-------------------------------------|:-------------|
| `id`         | `BIGINT`    | `PRIMARY KEY`                        | 主キー       |
| `created_at` | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時     |
| `updated_at` | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時     |
| `deleted_at` | `TIMESTAMP` | `NULL`                               | 論理削除日時 |

---

## 1. products（プロダクト）

Scrumプロジェクトの基本情報を管理します。

| カラム名       | 型             | 制約                                 | 説明                     |
|:---------------|:---------------|:-------------------------------------|:-------------------------|
| `id`           | `BIGINT`       | `PRIMARY KEY`                        | 主キー                   |
| `product_code` | `VARCHAR(20)`  | `NOT NULL UNIQUE`                    | プロダクトコード（一意） |
| `name`         | `VARCHAR(100)` | `NOT NULL`                           | プロダクト名             |
| `description`  | `TEXT`         | `NULL`                               | プロダクトの説明         |
| `status`       | `VARCHAR(20)`  | `NOT NULL DEFAULT 'ACTIVE' CHECK`    | 状態（ACTIVE/ARCHIVED）  |
| `created_at`   | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                 |
| `updated_at`   | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                 |
| `deleted_at`   | `TIMESTAMP`    | `NULL`                               | 論理削除日時             |

**インデックス**: `idx_products_status`

---

## 2. teams（チーム）

プロダクトに属する開発チームを管理します。

| カラム名      | 型             | 制約                                 | 説明                       |
|:--------------|:---------------|:-------------------------------------|:---------------------------|
| `id`          | `BIGINT`       | `PRIMARY KEY`                        | 主キー                     |
| `product_id`  | `BIGINT`       | `NOT NULL`                           | 所属プロダクト（外部キー） |
| `team_code`   | `VARCHAR(20)`  | `NOT NULL`                           | チームコード               |
| `name`        | `VARCHAR(100)` | `NOT NULL`                           | チーム名                   |
| `description` | `TEXT`         | `NULL`                               | チームの説明               |
| `status`      | `VARCHAR(20)`  | `NOT NULL DEFAULT 'ACTIVE' CHECK`    | 状態（ACTIVE/INACTIVE）    |
| `created_at`  | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                   |
| `updated_at`  | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                   |
| `deleted_at`  | `TIMESTAMP`    | `NULL`                               | 論理削除日時               |

**制約**: `UNIQUE(product_id, team_code)`, `FK: product_id -> products.id`
**インデックス**: `idx_teams_product_id`

---

## 3. team_members（チームメンバー）

プロジェクトに参加するメンバー情報を管理します。

| カラム名     | 型             | 制約                                 | 説明                    |
|:-------------|:---------------|:-------------------------------------|:------------------------|
| `id`         | `BIGINT`       | `PRIMARY KEY`                        | 主キー                  |
| `user_id`    | `BIGINT`       | `NOT NULL UNIQUE`                    | ユーザーID（一意）      |
| `name`       | `VARCHAR(100)` | `NOT NULL`                           | 氏名                    |
| `email`      | `VARCHAR(255)` | `NOT NULL UNIQUE`                    | メールアドレス          |
| `role`       | `VARCHAR(30)`  | `NOT NULL CHECK`                     | 役割（DEVELOPER/PO/SM） |
| `created_at` | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                |
| `updated_at` | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                |
| `deleted_at` | `TIMESTAMP`    | `NULL`                               | 論理削除日時            |

**CHECK**: `role IN ('DEVELOPER', 'PRODUCT_OWNER', 'SCRUM_MASTER')`

---

## 4. product_team_members（プロダクト-チームメンバー対応）

プロダクトとチームメンバーの所属関係、役割割り当てを管理します。

| カラム名          | 型            | 制約                                 | 説明                       |
|:------------------|:--------------|:-------------------------------------|:---------------------------|
| `id`              | `BIGINT`      | `PRIMARY KEY`                        | 主キー                     |
| `product_id`      | `BIGINT`      | `NOT NULL`                           | プロダクト（外部キー）     |
| `team_member_id`  | `BIGINT`      | `NOT NULL`                           | チームメンバー（外部キー） |
| `team_id`         | `BIGINT`      | `NULL`                               | 所属チーム（外部キー）     |
| `assignment_role` | `VARCHAR(30)` | `NOT NULL CHECK`                     | 割り当て役割               |
| `assigned_at`     | `TIMESTAMP`   | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 割り当て日時               |
| `created_at`      | `TIMESTAMP`   | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                   |
| `updated_at`      | `TIMESTAMP`   | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                   |
| `deleted_at`      | `TIMESTAMP`   | `NULL`                               | 論理削除日時               |

**制約**: `UNIQUE(product_id, team_member_id)`, `FK: product_id -> products.id`,
`FK: team_member_id -> team_members.id`, `FK: team_id -> teams.id`
**CHECK**: `assignment_role IN ('PRODUCT_OWNER', 'SCRUM_MASTER', 'DEVELOPER', 'STAKEHOLDER')`
**インデックス**: `idx_product_team_members_product_id`

---

## 5. product_visions（プロダクトビジョン）

プロダクトのビジョン・成果指標を定義します。

| カラム名          | 型          | 制約                                 | 説明                       |
|:------------------|:------------|:-------------------------------------|:---------------------------|
| `id`              | `BIGINT`    | `PRIMARY KEY`                        | 主キー                     |
| `product_id`      | `BIGINT`    | `NOT NULL UNIQUE`                    | 関連プロダクト（外部キー） |
| `vision`          | `TEXT`      | `NOT NULL`                           | ビジョン文書               |
| `success_metrics` | `TEXT`      | `NULL`                               | 成果指標                   |
| `target_date`     | `DATE`      | `NULL`                               | 目標達成日                 |
| `created_at`      | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                   |
| `updated_at`      | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                   |
| `deleted_at`      | `TIMESTAMP` | `NULL`                               | 論理削除日時               |

**制約**: `FK: product_id -> products.id`

---

## 6. epics（エピック）

大規模な機能単位のエピックを管理します。

| カラム名           | 型             | 制約                                 | 説明                         |
|:-------------------|:---------------|:-------------------------------------|:-----------------------------|
| `id`               | `BIGINT`       | `PRIMARY KEY`                        | 主キー                       |
| `epic_code`        | `VARCHAR(30)`  | `NOT NULL`                           | エピックコード               |
| `name`             | `VARCHAR(200)` | `NOT NULL`                           | エピック名                   |
| `description`      | `TEXT`         | `NULL`                               | 説明                         |
| `priority`         | `INTEGER`      | `NOT NULL DEFAULT 0`                 | 優先度（数値が小さいほど高） |
| `status`           | `VARCHAR(20)`  | `NOT NULL DEFAULT 'BACKLOG' CHECK`   | 状態                         |
| `estimated_points` | `INTEGER`      | `NULL`                               | 推定ストーリーポイント       |
| `created_at`       | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                     |
| `updated_at`       | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                     |
| `deleted_at`       | `TIMESTAMP`    | `NULL`                               | 論理削除日時                 |

**CHECK**: `status IN ('BACKLOG', 'IN_PROGRESS', 'DONE', 'CANCELLED')`
**インデックス**: `idx_epics_status`, `idx_epics_priority`

---

## 7. product_epics（プロダクト-エピック対応）

プロダクトとエピックの包含関係を管理します。

| カラム名     | 型          | 制約                                 | 説明                   |
|:-------------|:------------|:-------------------------------------|:-----------------------|
| `id`         | `BIGINT`    | `PRIMARY KEY`                        | 主キー                 |
| `product_id` | `BIGINT`    | `NOT NULL`                           | プロダクト（外部キー） |
| `epic_id`    | `BIGINT`    | `NOT NULL`                           | エピック（外部キー）   |
| `created_at` | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時               |
| `updated_at` | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時               |
| `deleted_at` | `TIMESTAMP` | `NULL`                               | 論理削除日時           |

**制約**: `UNIQUE(product_id, epic_id)`, `FK: product_id -> products.id`, `FK: epic_id -> epics.id`
**インデックス**: `idx_product_epics_product_id`

---

## 8. features（フィーチャー）

エピックを分解したフィーチャーを管理します。

| カラム名           | 型             | 制約                                 | 説明                   |
|:-------------------|:---------------|:-------------------------------------|:-----------------------|
| `id`               | `BIGINT`       | `PRIMARY KEY`                        | 主キー                 |
| `feature_code`     | `VARCHAR(30)`  | `NOT NULL`                           | フィーチャーコード     |
| `name`             | `VARCHAR(200)` | `NOT NULL`                           | フィーチャー名         |
| `description`      | `TEXT`         | `NULL`                               | 説明                   |
| `priority`         | `INTEGER`      | `NOT NULL DEFAULT 0`                 | 優先度                 |
| `status`           | `VARCHAR(20)`  | `NOT NULL DEFAULT 'BACKLOG' CHECK`   | 状態                   |
| `estimated_points` | `INTEGER`      | `NULL`                               | 推定ストーリーポイント |
| `created_at`       | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時               |
| `updated_at`       | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時               |
| `deleted_at`       | `TIMESTAMP`    | `NULL`                               | 論理削除日時           |

**CHECK**: `status IN ('BACKLOG', 'IN_PROGRESS', 'DONE', 'CANCELLED')`

---

## 9. epic_features（エピック-フィーチャー対応）

エピックとフィーチャーの階層関係を管理します。

| カラム名     | 型          | 制約                                 | 説明                     |
|:-------------|:------------|:-------------------------------------|:-------------------------|
| `id`         | `BIGINT`    | `PRIMARY KEY`                        | 主キー                   |
| `epic_id`    | `BIGINT`    | `NOT NULL`                           | エピック（外部キー）     |
| `feature_id` | `BIGINT`    | `NOT NULL`                           | フィーチャー（外部キー） |
| `sort_order` | `INTEGER`   | `NOT NULL DEFAULT 0`                 | 表示順序                 |
| `created_at` | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                 |
| `updated_at` | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                 |
| `deleted_at` | `TIMESTAMP` | `NULL`                               | 論理削除日時             |

**制約**: `UNIQUE(epic_id, feature_id)`, `FK: epic_id -> epics.id`, `FK: feature_id -> features.id`
**インデックス**: `idx_epic_features_epic_id`

---

## 10. user_stories（ユーザーストーリー）

ユーザーストーリー情報を管理します。

| カラム名              | 型             | 制約                                 | 説明                     |
|:----------------------|:---------------|:-------------------------------------|:-------------------------|
| `id`                  | `BIGINT`       | `PRIMARY KEY`                        | 主キー                   |
| `story_code`          | `VARCHAR(30)`  | `NOT NULL UNIQUE`                    | ストーリーコード（一意） |
| `title`               | `VARCHAR(200)` | `NOT NULL`                           | タイトル                 |
| `description`         | `TEXT`         | `NULL`                               | 説明                     |
| `acceptance_criteria` | `TEXT`         | `NULL`                               | 受け入れ基準             |
| `priority`            | `INTEGER`      | `NOT NULL DEFAULT 0`                 | 優先度                   |
| `estimated_points`    | `INTEGER`      | `NULL`                               | 推定ストーリーポイント   |
| `actual_points`       | `INTEGER`      | `NULL`                               | 実績ストーリーポイント   |
| `status_id`           | `BIGINT`       | `NULL`                               | 状態（外部キー）         |
| `assignee_id`         | `BIGINT`       | `NULL`                               | 担当者（外部キー）       |
| `created_at`          | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                 |
| `updated_at`          | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                 |
| `deleted_at`          | `TIMESTAMP`    | `NULL`                               | 論理削除日時             |

**制約**: `FK: status_id -> story_statuses.id`, `FK: assignee_id -> team_members.id`
**インデックス**: `idx_user_stories_status_id`, `idx_user_stories_assignee_id`

---

## 11. feature_user_stories（フィーチャー-ユーザーストーリー対応）

フィーチャーとユーザーストーリーの階層関係を管理します。

| カラム名        | 型          | 制約                                 | 説明                           |
|:----------------|:------------|:-------------------------------------|:-------------------------------|
| `id`            | `BIGINT`    | `PRIMARY KEY`                        | 主キー                         |
| `feature_id`    | `BIGINT`    | `NOT NULL`                           | フィーチャー（外部キー）       |
| `user_story_id` | `BIGINT`    | `NOT NULL`                           | ユーザーストーリー（外部キー） |
| `sort_order`    | `INTEGER`   | `NOT NULL DEFAULT 0`                 | 表示順序                       |
| `created_at`    | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                       |
| `updated_at`    | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                       |
| `deleted_at`    | `TIMESTAMP` | `NULL`                               | 論理削除日時                   |

**制約**: `UNIQUE(feature_id, user_story_id)`, `FK: feature_id -> features.id`,
`FK: user_story_id -> user_stories.id`
**インデックス**: `idx_feature_user_stories_feature_id`

---

## 12. product_backlogs（プロダクトバックログ）

優先順位付けされたユーザーストーリーのリストを管理します。

| カラム名        | 型          | 制約                                 | 説明                           |
|:----------------|:------------|:-------------------------------------|:-------------------------------|
| `id`            | `BIGINT`    | `PRIMARY KEY`                        | 主キー                         |
| `product_id`    | `BIGINT`    | `NOT NULL`                           | プロダクト（外部キー）         |
| `user_story_id` | `BIGINT`    | `NOT NULL`                           | ユーザーストーリー（外部キー） |
| `priority`      | `INTEGER`   | `NOT NULL DEFAULT 0`                 | 優先順位（数値が小さいほど高） |
| `version`       | `INTEGER`   | `NOT NULL DEFAULT 0`                 | 楽観的ロック用バージョン       |
| `created_at`    | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                       |
| `updated_at`    | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                       |
| `deleted_at`    | `TIMESTAMP` | `NULL`                               | 論理削除日時                   |

**制約**: `UNIQUE(product_id, user_story_id)`, `FK: product_id -> products.id`,
`FK: user_story_id -> user_stories.id`
**インデックス**: `idx_product_backlogs_product_id`, `idx_product_backlogs_priority`

---

## 13. story_statuses（ストーリーステータス）

ユーザーストーリーの状態定義を管理します。

| カラム名      | 型            | 制約                                 | 説明                  |
|:--------------|:--------------|:-------------------------------------|:----------------------|
| `id`          | `BIGINT`      | `PRIMARY KEY`                        | 主キー                |
| `status_code` | `VARCHAR(20)` | `NOT NULL UNIQUE`                    | 状態コード（一意）    |
| `name`        | `VARCHAR(50)` | `NOT NULL`                           | 状態名                |
| `color`       | `VARCHAR(10)` | `NULL`                               | 表示カラー（#RRGGBB） |
| `sort_order`  | `INTEGER`     | `NOT NULL DEFAULT 0`                 | 表示順序              |
| `is_terminal` | `BOOLEAN`     | `NOT NULL DEFAULT FALSE`             | 終端状態かどうか      |
| `created_at`  | `TIMESTAMP`   | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時              |
| `updated_at`  | `TIMESTAMP`   | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時              |
| `deleted_at`  | `TIMESTAMP`   | `NULL`                               | 論理削除日時          |

---

## 14. user_story_dependencies（ユーザーストーリー間依存）

ユーザーストーリー間の依存関係を管理します。

| カラム名          | 型            | 制約                                 | 説明                         |
|:------------------|:--------------|:-------------------------------------|:-----------------------------|
| `id`              | `BIGINT`      | `PRIMARY KEY`                        | 主キー                       |
| `source_story_id` | `BIGINT`      | `NOT NULL`                           | 依存元ストーリー（外部キー） |
| `target_story_id` | `BIGINT`      | `NOT NULL`                           | 依存先ストーリー（外部キー） |
| `dependency_type` | `VARCHAR(20)` | `NOT NULL CHECK`                     | 依存タイプ                   |
| `created_at`      | `TIMESTAMP`   | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                     |
| `updated_at`      | `TIMESTAMP`   | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                     |
| `deleted_at`      | `TIMESTAMP`   | `NULL`                               | 論理削除日時                 |

**制約**: `UNIQUE(source_story_id, target_story_id)`, `FK: source_story_id -> user_stories.id`,
`FK: target_story_id -> user_stories.id`
**CHECK**: `dependency_type IN ('BLOCKS', 'REQUIRES', 'RELATES_TO')`

---

## 15. sprints（スプリント）

スプリントの期間・目標情報を管理します。

| カラム名        | 型             | 制約                                 | 説明                       |
|:----------------|:---------------|:-------------------------------------|:---------------------------|
| `id`            | `BIGINT`       | `PRIMARY KEY`                        | 主キー                     |
| `product_id`    | `BIGINT`       | `NOT NULL`                           | 関連プロダクト（外部キー） |
| `sprint_number` | `INTEGER`      | `NOT NULL`                           | スプリント番号             |
| `name`          | `VARCHAR(100)` | `NOT NULL`                           | スプリント名               |
| `goal`          | `TEXT`         | `NULL`                               | スプリントゴール           |
| `start_date`    | `DATE`         | `NOT NULL`                           | 開始日                     |
| `end_date`      | `DATE`         | `NOT NULL`                           | 終了日                     |
| `status`        | `VARCHAR(20)`  | `NOT NULL DEFAULT 'PLANNED' CHECK`   | 状態                       |
| `created_at`    | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                   |
| `updated_at`    | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                   |
| `deleted_at`    | `TIMESTAMP`    | `NULL`                               | 論理削除日時               |

**制約**: `UNIQUE(product_id, sprint_number)`, `FK: product_id -> products.id`
**CHECK**: `status IN ('PLANNED', 'ACTIVE', 'COMPLETED', 'CANCELLED')`
**CHECK**: `end_date >= start_date`
**インデックス**: `idx_sprints_product_id`, `idx_sprints_status`

---

## 16. sprint_backlogs（スプリントバックログ）

スプリントで実施するタスクのリストを管理します。

| カラム名           | 型          | 制約                                 | 説明                           |
|:-------------------|:------------|:-------------------------------------|:-------------------------------|
| `id`               | `BIGINT`    | `PRIMARY KEY`                        | 主キー                         |
| `sprint_id`        | `BIGINT`    | `NOT NULL`                           | スプリント（外部キー）         |
| `capacity_points`  | `INTEGER`   | `NULL`                               | スプリントのキャパシティ       |
| `committed_points` | `INTEGER`   | `NOT NULL DEFAULT 0`                 | コミットしたストーリーポイント |
| `created_at`       | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                       |
| `updated_at`       | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                       |
| `deleted_at`       | `TIMESTAMP` | `NULL`                               | 論理削除日時                   |

**制約**: `UNIQUE(sprint_id)`, `FK: sprint_id -> sprints.id`

---

## 17. tasks（タスク）

作業単位のタスク情報を管理します。

| カラム名          | 型             | 制約                                 | 説明                 |
|:------------------|:---------------|:-------------------------------------|:---------------------|
| `id`              | `BIGINT`       | `PRIMARY KEY`                        | 主キー               |
| `task_code`       | `VARCHAR(30)`  | `NOT NULL UNIQUE`                    | タスクコード（一意） |
| `title`           | `VARCHAR(200)` | `NOT NULL`                           | タスク名             |
| `description`     | `TEXT`         | `NULL`                               | 説明                 |
| `status`          | `VARCHAR(20)`  | `NOT NULL DEFAULT 'TODO' CHECK`      | 状態                 |
| `estimated_hours` | `DECIMAL(5,1)` | `NULL`                               | 推定時間（時間）     |
| `actual_hours`    | `DECIMAL(5,1)` | `NULL`                               | 実績時間（時間）     |
| `progress`        | `INTEGER`      | `NOT NULL DEFAULT 0 CHECK`           | 進捗率（0-100）      |
| `assignee_id`     | `BIGINT`       | `NULL`                               | 担当者（外部キー）   |
| `start_date`      | `DATE`         | `NULL`                               | 開始日               |
| `due_date`        | `DATE`         | `NULL`                               | 期限                 |
| `completed_at`    | `TIMESTAMP`    | `NULL`                               | 完了日時             |
| `created_at`      | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時             |
| `updated_at`      | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時             |
| `deleted_at`      | `TIMESTAMP`    | `NULL`                               | 論理削除日時         |

**制約**: `FK: assignee_id -> team_members.id`
**CHECK**: `status IN ('TODO', 'IN_PROGRESS', 'DONE', 'CANCELLED', 'BLOCKED')`
**CHECK**: `progress >= 0 AND progress <= 100`
**インデックス**: `idx_tasks_status`, `idx_tasks_assignee_id`

---

## 18. user_story_tasks（ユーザーストーリー-タスク対応）

ユーザーストーリーとタスクの分解関係を管理します。

| カラム名        | 型          | 制約                                 | 説明                           |
|:----------------|:------------|:-------------------------------------|:-------------------------------|
| `id`            | `BIGINT`    | `PRIMARY KEY`                        | 主キー                         |
| `user_story_id` | `BIGINT`    | `NOT NULL`                           | ユーザーストーリー（外部キー） |
| `task_id`       | `BIGINT`    | `NOT NULL`                           | タスク（外部キー）             |
| `sort_order`    | `INTEGER`   | `NOT NULL DEFAULT 0`                 | 表示順序                       |
| `created_at`    | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                       |
| `updated_at`    | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                       |
| `deleted_at`    | `TIMESTAMP` | `NULL`                               | 論理削除日時                   |

**制約**: `UNIQUE(user_story_id, task_id)`, `FK: user_story_id -> user_stories.id`,
`FK: task_id -> tasks.id`
**インデックス**: `idx_user_story_tasks_user_story_id`

---

## 19. sprint_tasks（スプリント-タスク対応）

スプリントとタスクの割り当て関係を管理します。

| カラム名     | 型          | 制約                                 | 説明                   |
|:-------------|:------------|:-------------------------------------|:-----------------------|
| `id`         | `BIGINT`    | `PRIMARY KEY`                        | 主キー                 |
| `sprint_id`  | `BIGINT`    | `NOT NULL`                           | スプリント（外部キー） |
| `task_id`    | `BIGINT`    | `NOT NULL`                           | タスク（外部キー）     |
| `created_at` | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時               |
| `updated_at` | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時               |
| `deleted_at` | `TIMESTAMP` | `NULL`                               | 論理削除日時           |

**制約**: `UNIQUE(sprint_id, task_id)`, `FK: sprint_id -> sprints.id`, `FK: task_id -> tasks.id`
**インデックス**: `idx_sprint_tasks_sprint_id`

---

## 20. daily_scrums（デイリースクラム）

デイリースクラムの記録を管理します。

| カラム名         | 型          | 制約                                 | 説明                     |
|:-----------------|:------------|:-------------------------------------|:-------------------------|
| `id`             | `BIGINT`    | `PRIMARY KEY`                        | 主キー                   |
| `sprint_id`      | `BIGINT`    | `NOT NULL`                           | スプリント（外部キー）   |
| `team_member_id` | `BIGINT`    | `NOT NULL`                           | 発言メンバー（外部キー） |
| `scrum_date`     | `DATE`      | `NOT NULL`                           | スクラム日付             |
| `yesterday_work` | `TEXT`      | `NULL`                               | 昨日の作業               |
| `today_work`     | `TEXT`      | `NULL`                               | 今日の予定               |
| `blockers`       | `TEXT`      | `NULL`                               | 障害事項                 |
| `created_at`     | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                 |
| `updated_at`     | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                 |
| `deleted_at`     | `TIMESTAMP` | `NULL`                               | 論理削除日時             |

**制約**: `FK: sprint_id -> sprints.id`, `FK: team_member_id -> team_members.id`
**インデックス**: `idx_daily_scrums_sprint_id`, `idx_daily_scrums_scrum_date`

---

## 21. sprint_reviews（スプリントレビュー）

スプリントレビューの記録を管理します。

| カラム名      | 型          | 制約                                 | 説明                   |
|:--------------|:------------|:-------------------------------------|:-----------------------|
| `id`          | `BIGINT`    | `PRIMARY KEY`                        | 主キー                 |
| `sprint_id`   | `BIGINT`    | `NOT NULL UNIQUE`                    | スプリント（外部キー） |
| `review_date` | `DATE`      | `NOT NULL`                           | レビュー日付           |
| `summary`     | `TEXT`      | `NULL`                               | 要約                   |
| `feedback`    | `TEXT`      | `NULL`                               | フィードバック         |
| `attendees`   | `TEXT`      | `NULL`                               | 参加者リスト（JSON）   |
| `created_at`  | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時               |
| `updated_at`  | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時               |
| `deleted_at`  | `TIMESTAMP` | `NULL`                               | 論理削除日時           |

**制約**: `FK: sprint_id -> sprints.id`

---

## 22. retrospectives（レトロスペクティブ）

レトロスペクティブの記録を管理します。

| カラム名       | 型          | 制約                                 | 説明                       |
|:---------------|:------------|:-------------------------------------|:---------------------------|
| `id`           | `BIGINT`    | `PRIMARY KEY`                        | 主キー                     |
| `sprint_id`    | `BIGINT`    | `NOT NULL UNIQUE`                    | スプリント（外部キー）     |
| `retro_date`   | `DATE`      | `NOT NULL`                           | 実施日付                   |
| `went_well`    | `TEXT`      | `NULL`                               | 良かったこと               |
| `to_improve`   | `TEXT`      | `NULL`                               | 改善すべきこと             |
| `action_items` | `TEXT`      | `NULL`                               | アクションアイテム（JSON） |
| `created_at`   | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                   |
| `updated_at`   | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                   |
| `deleted_at`   | `TIMESTAMP` | `NULL`                               | 論理削除日時               |

**制約**: `FK: sprint_id -> sprints.id`

---

## 23. releases（リリース）

リリース情報を管理します。

| カラム名       | 型             | 制約                                 | 説明                   |
|:---------------|:---------------|:-------------------------------------|:-----------------------|
| `id`           | `BIGINT`       | `PRIMARY KEY`                        | 主キー                 |
| `product_id`   | `BIGINT`       | `NOT NULL`                           | プロダクト（外部キー） |
| `release_code` | `VARCHAR(30)`  | `NOT NULL UNIQUE`                    | リリースコード（一意） |
| `name`         | `VARCHAR(100)` | `NOT NULL`                           | リリース名             |
| `description`  | `TEXT`         | `NULL`                               | 説明                   |
| `release_date` | `DATE`         | `NULL`                               | リリース日             |
| `planned_date` | `DATE`         | `NULL`                               | 計画日                 |
| `status`       | `VARCHAR(20)`  | `NOT NULL DEFAULT 'PLANNED' CHECK`   | 状態                   |
| `created_at`   | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時               |
| `updated_at`   | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時               |
| `deleted_at`   | `TIMESTAMP`    | `NULL`                               | 論理削除日時           |

**制約**: `FK: product_id -> products.id`
**CHECK**: `status IN ('PLANNED', 'IN_PROGRESS', 'RELEASED', 'CANCELLED')`
**インデックス**: `idx_releases_product_id`

---

## 24. definition_of_done（完了の定義）

開発チームが合意した完了基準（DoD）を管理します。

| カラム名     | 型          | 制約                                 | 説明                   |
|:-------------|:------------|:-------------------------------------|:-----------------------|
| `id`         | `BIGINT`    | `PRIMARY KEY`                        | 主キー                 |
| `team_id`    | `BIGINT`    | `NOT NULL UNIQUE`                    | 関連チーム（外部キー） |
| `criteria`   | `TEXT`      | `NOT NULL`                           | 完了基準（JSON配列）   |
| `version`    | `INTEGER`   | `NOT NULL DEFAULT 1`                 | バージョン番号         |
| `created_at` | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時               |
| `updated_at` | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時               |
| `deleted_at` | `TIMESTAMP` | `NULL`                               | 論理削除日時           |

**制約**: `FK: team_id -> teams.id`

---

## 25. product_preparations（プロダクト準備）

プロダクトの立ち上げ準備期間と状態を管理します。

| カラム名           | 型            | 制約                                   | 説明                       |
|:-------------------|:--------------|:---------------------------------------|:---------------------------|
| `id`               | `BIGINT`      | `PRIMARY KEY`                          | 主キー                     |
| `product_id`       | `BIGINT`      | `NOT NULL UNIQUE`                      | 関連プロダクト（外部キー） |
| `status`           | `VARCHAR(20)` | `NOT NULL DEFAULT 'IN_PROGRESS' CHECK` | 準備状態                   |
| `start_date`       | `DATE`        | `NOT NULL`                             | 開始日                     |
| `planned_end_date` | `DATE`        | `NULL`                                 | 計画終了日                 |
| `actual_end_date`  | `DATE`        | `NULL`                                 | 実際終了日                 |
| `progress`         | `INTEGER`     | `NOT NULL DEFAULT 0 CHECK`             | 進捗率（0-100）            |
| `notes`            | `TEXT`        | `NULL`                                 | 備考                       |
| `created_at`       | `TIMESTAMP`   | `NOT NULL DEFAULT CURRENT_TIMESTAMP`   | 作成日時                   |
| `updated_at`       | `TIMESTAMP`   | `NOT NULL DEFAULT CURRENT_TIMESTAMP`   | 更新日時                   |
| `deleted_at`       | `TIMESTAMP`   | `NULL`                                 | 論理削除日時               |

**制約**: `FK: product_id -> products.id`
**CHECK**: `status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')`
**CHECK**: `progress >= 0 AND progress <= 100`

---

## 26. preparation_tasks（準備作業項目）

環境構築、規約策定などの準備作業単位を管理します。

| カラム名       | 型             | 制約                                 | 説明               |
|:---------------|:---------------|:-------------------------------------|:-------------------|
| `id`           | `BIGINT`       | `PRIMARY KEY`                        | 主キー             |
| `task_code`    | `VARCHAR(30)`  | `NOT NULL`                           | 作業コード         |
| `title`        | `VARCHAR(200)` | `NOT NULL`                           | タイトル           |
| `description`  | `TEXT`         | `NULL`                               | 説明               |
| `category`     | `VARCHAR(30)`  | `NOT NULL CHECK`                     | カテゴリ           |
| `status`       | `VARCHAR(20)`  | `NOT NULL DEFAULT 'TODO' CHECK`      | 状態               |
| `assignee_id`  | `BIGINT`       | `NULL`                               | 担当者（外部キー） |
| `due_date`     | `DATE`         | `NULL`                               | 期限               |
| `completed_at` | `TIMESTAMP`    | `NULL`                               | 完了日時           |
| `created_at`   | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時           |
| `updated_at`   | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時           |
| `deleted_at`   | `TIMESTAMP`    | `NULL`                               | 論理削除日時       |

**制約**: `FK: assignee_id -> team_members.id`
**CHECK**: `category IN ('ENVIRONMENT', 'CONVENTION', 'DOCUMENT', 'TOOL', 'OTHER')`
**CHECK**: `status IN ('TODO', 'IN_PROGRESS', 'DONE', 'CANCELLED', 'BLOCKED')`

---

## 27. preparation_task_items（準備-準備作業項目対応）

準備計画と作業項目の包含関係を管理します。

| カラム名         | 型          | 制約                                 | 説明                       |
|:-----------------|:------------|:-------------------------------------|:---------------------------|
| `id`             | `BIGINT`    | `PRIMARY KEY`                        | 主キー                     |
| `preparation_id` | `BIGINT`    | `NOT NULL`                           | プロダクト準備（外部キー） |
| `task_id`        | `BIGINT`    | `NOT NULL`                           | 準備作業項目（外部キー）   |
| `sort_order`     | `INTEGER`   | `NOT NULL DEFAULT 0`                 | 表示順序                   |
| `created_at`     | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                   |
| `updated_at`     | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                   |
| `deleted_at`     | `TIMESTAMP` | `NULL`                               | 論理削除日時               |

**制約**: `UNIQUE(preparation_id, task_id)`, `FK: preparation_id -> product_preparations.id`,
`FK: task_id -> preparation_tasks.id`
**インデックス**: `idx_preparation_task_items_preparation_id`

---

## 28. preparation_decisions（準備決定）

技術・業務上の意思決定記録を管理します。

| カラム名        | 型             | 制約                                 | 説明               |
|:----------------|:---------------|:-------------------------------------|:-------------------|
| `id`            | `BIGINT`       | `PRIMARY KEY`                        | 主キー             |
| `title`         | `VARCHAR(200)` | `NOT NULL`                           | 決定タイトル       |
| `description`   | `TEXT`         | `NOT NULL`                           | 決定内容           |
| `decision_type` | `VARCHAR(20)`  | `NOT NULL CHECK`                     | 決定タイプ         |
| `decider_id`    | `BIGINT`       | `NULL`                               | 決定者（外部キー） |
| `decision_date` | `DATE`         | `NOT NULL`                           | 決定日付           |
| `rationale`     | `TEXT`         | `NULL`                               | 決定理由           |
| `created_at`    | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時           |
| `updated_at`    | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時           |
| `deleted_at`    | `TIMESTAMP`    | `NULL`                               | 論理削除日時       |

**制約**: `FK: decider_id -> team_members.id`
**CHECK**: `decision_type IN ('TECHNICAL', 'BUSINESS', 'PROCESS', 'OTHER')`

---

## 29. preparation_task_decisions（準備作業-準備決定対応）

作業項目と意思決定の関係を管理します。

| カラム名      | 型          | 制約                                 | 説明                     |
|:--------------|:------------|:-------------------------------------|:-------------------------|
| `id`          | `BIGINT`    | `PRIMARY KEY`                        | 主キー                   |
| `task_id`     | `BIGINT`    | `NOT NULL`                           | 準備作業項目（外部キー） |
| `decision_id` | `BIGINT`    | `NOT NULL`                           | 準備決定（外部キー）     |
| `created_at`  | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                 |
| `updated_at`  | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                 |
| `deleted_at`  | `TIMESTAMP` | `NULL`                               | 論理削除日時             |

**制約**: `UNIQUE(task_id, decision_id)`, `FK: task_id -> preparation_tasks.id`,
`FK: decision_id -> preparation_decisions.id`

---

## 30. preparation_risks（準備リスク）

技術的・業務的リスク情報を管理します。

| カラム名      | 型             | 制約                                  | 説明                 |
|:--------------|:---------------|:--------------------------------------|:---------------------|
| `id`          | `BIGINT`       | `PRIMARY KEY`                         | 主キー               |
| `risk_code`   | `VARCHAR(30)`  | `NOT NULL UNIQUE`                     | リスクコード（一意） |
| `title`       | `VARCHAR(200)` | `NOT NULL`                            | リスクタイトル       |
| `description` | `TEXT`         | `NOT NULL`                            | リスク内容           |
| `category`    | `VARCHAR(20)`  | `NOT NULL CHECK`                      | リスクカテゴリ       |
| `severity`    | `VARCHAR(10)`  | `NOT NULL CHECK`                      | 重大度               |
| `probability` | `VARCHAR(10)`  | `NOT NULL CHECK`                      | 発生確率             |
| `status`      | `VARCHAR(20)`  | `NOT NULL DEFAULT 'IDENTIFIED' CHECK` | 状態                 |
| `mitigation`  | `TEXT`         | `NULL`                                | 緩和策               |
| `owner_id`    | `BIGINT`       | `NULL`                                | 担当者（外部キー）   |
| `created_at`  | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP`  | 作成日時             |
| `updated_at`  | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP`  | 更新日時             |
| `deleted_at`  | `TIMESTAMP`    | `NULL`                                | 論理削除日時         |

**制約**: `FK: owner_id -> team_members.id`
**CHECK**: `category IN ('TECHNICAL', 'BUSINESS', 'RESOURCE', 'SCHEDULE', 'OTHER')`
**CHECK**: `severity IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')`
**CHECK**: `probability IN ('LOW', 'MEDIUM', 'HIGH')`
**CHECK**: `status IN ('IDENTIFIED', 'MONITORING', 'MITIGATED', 'CLOSED')`

---

## 31. preparation_risks_map（準備-準備リスク対応）

準備計画とリスクの関係を管理します。

| カラム名         | 型          | 制約                                 | 説明                       |
|:-----------------|:------------|:-------------------------------------|:---------------------------|
| `id`             | `BIGINT`    | `PRIMARY KEY`                        | 主キー                     |
| `preparation_id` | `BIGINT`    | `NOT NULL`                           | プロダクト準備（外部キー） |
| `risk_id`        | `BIGINT`    | `NOT NULL`                           | 準備リスク（外部キー）     |
| `created_at`     | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                   |
| `updated_at`     | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                   |
| `deleted_at`     | `TIMESTAMP` | `NULL`                               | 論理削除日時               |

**制約**: `UNIQUE(preparation_id, risk_id)`, `FK: preparation_id -> product_preparations.id`,
`FK: risk_id -> preparation_risks.id`
**インデックス**: `idx_preparation_risks_map_preparation_id`

---

## 32. impediments（障害）

スプリント実行中に発生する障害（インピーディメント）を管理します。

| カラム名      | 型             | 制約                                  | 説明                       |
|:--------------|:---------------|:--------------------------------------|:---------------------------|
| `id`          | `BIGINT`       | `PRIMARY KEY`                         | 主キー                     |
| `sprint_id`   | `BIGINT`       | `NOT NULL`                            | 関連スプリント（外部キー） |
| `title`       | `VARCHAR(200)` | `NOT NULL`                            | 障害タイトル               |
| `description` | `TEXT`         | `NOT NULL`                            | 障害の詳細説明             |
| `impact`      | `TEXT`         | `NULL`                                | 影響範囲                   |
| `assignee_id` | `BIGINT`       | `NULL`                                | 担当者（外部キー）         |
| `status`      | `VARCHAR(20)`  | `NOT NULL DEFAULT 'NEW' CHECK`        | 状態                       |
| `resolved_at` | `TIMESTAMP`    | `NULL`                                | 解消日時                   |
| `created_at`  | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP`  | 作成日時                   |
| `updated_at`  | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP`  | 更新日時                   |
| `deleted_at`  | `TIMESTAMP`    | `NULL`                                | 論理削除日時               |

**制約**: `FK: sprint_id -> sprints.id`, `FK: assignee_id -> team_members.id`
**CHECK**: `status IN ('NEW', 'IN_PROGRESS', 'RESOLVED')`
**インデックス**: `idx_impediments_sprint_id`, `idx_impediments_status`

---

## 33. sprint_dependencies（スプリント依存関係）

スプリント内のバックログ項目間の依存関係を管理します。

| カラム名          | 型             | 制約                                 | 説明                         |
|:------------------|:---------------|:-------------------------------------|:-----------------------------|
| `id`              | `BIGINT`       | `PRIMARY KEY`                        | 主キー                       |
| `source_item_id`  | `BIGINT`       | `NOT NULL`                           | 依存元項目（外部キー）       |
| `target_item_id`  | `BIGINT`       | `NOT NULL`                           | 依存先項目（外部キー）       |
| `dependency_type` | `VARCHAR(20)`  | `NOT NULL CHECK`                     | 依存関係の種類               |
| `description`     | `TEXT`         | `NULL`                               | 依存関係の説明               |
| `created_at`      | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                     |
| `updated_at`      | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                     |
| `deleted_at`      | `TIMESTAMP`    | `NULL`                               | 論理削除日時                 |

**制約**: `UNIQUE(source_item_id, target_item_id)`, `FK: source_item_id -> tasks.id`,
`FK: target_item_id -> tasks.id`
**CHECK**: `dependency_type IN ('BLOCKS', 'REQUIRES', 'RELATES_TO')`
**インデックス**: `idx_sprint_dependencies_source_id`, `idx_sprint_dependencies_target_id`

---

## 34. notifications（通知）

スプリント実行中に発生する通知を管理します。

| カラム名       | 型             | 制約                                 | 説明                         |
|:---------------|:---------------|:-------------------------------------|:-----------------------------|
| `id`           | `BIGINT`       | `PRIMARY KEY`                        | 主キー                       |
| `recipient_id` | `BIGINT`       | `NOT NULL`                           | 受信者（外部キー）           |
| `product_id`   | `BIGINT`       | `NOT NULL`                           | 所属プロダクト（外部キー）   |
| `title`        | `VARCHAR(200)` | `NOT NULL`                           | 通知タイトル                 |
| `content`      | `TEXT`         | `NOT NULL`                           | 通知内容                     |
| `type`         | `VARCHAR(20)`  | `NOT NULL CHECK`                     | 通知種別                     |
| `is_read`      | `BOOLEAN`      | `NOT NULL DEFAULT FALSE`             | 既読フラグ                   |
| `created_at`   | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時                     |
| `updated_at`   | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 更新日時                     |
| `deleted_at`   | `TIMESTAMP`    | `NULL`                               | 論理削除日時                 |

**制約**: `FK: recipient_id -> team_members.id`, `FK: product_id -> products.id`
**CHECK**: `type IN ('MENTION', 'ASSIGNEE_CHANGE', 'DUE_DATE', 'AWAITING_DECISION')`
**インデックス**: `idx_notifications_recipient_id`, `idx_notifications_is_read`

---

## 35. activities（アクティビティ）

スプリント実行中のアクティビティ履歴を管理します。

| カラム名        | 型             | 制約                                 | 説明                         |
|:----------------|:---------------|:-------------------------------------|:-----------------------------|
| `id`            | `BIGINT`       | `PRIMARY KEY`                        | 主キー                       |
| `sprint_id`     | `BIGINT`       | `NOT NULL`                           | 関連スプリント（外部キー）   |
| `actor_id`      | `BIGINT`       | `NOT NULL`                           | 実行者（外部キー）           |
| `activity_type` | `VARCHAR(30)`  | `NOT NULL CHECK`                     | アクティビティの種類         |
| `description`   | `TEXT`         | `NULL`                               | アクティビティの説明         |
| `created_at`    | `TIMESTAMP`    | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | 作成日時（発生日時）         |

**制約**: `FK: sprint_id -> sprints.id`, `FK: actor_id -> team_members.id`
**CHECK**: `activity_type IN ('STATUS_CHANGE', 'COMMENT_ADDED', 'TASK_ASSIGNED', 'TASK_COMPLETED', 'IMPEDIMENT_ADDED', 'IMPEDIMENT_RESOLVED', 'DEPENDENCY_ADDED', 'DEPENDENCY_RESOLVED', 'SPRINT_GOAL_UPDATED', 'BOARD_UPDATED')`
**インデックス**: `idx_activities_sprint_id`, `idx_activities_actor_id`, `idx_activities_created_at`

---

## 参照

- 情報一覧: `docs/22 要件定義書/06 機能要件/AG アジルーム/04.システム/01.情報一覧.md`
- データ設計スキル: `.agents/skills/data-design/SKILL.md`
