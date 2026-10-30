# Sprint Review 情報属性一覧

## 概要

Sprint Review（INF-MF-01）の属性を定義します。

## 情報概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-MF-01 |
| 情報名 | Sprint Review |
| 情報種類 | リソース |
| 説明 | Sprint Reviewの記録（成果、フィードバック、決定事項） |
| 履歴管理要 | Y |
| 証跡要 | Y |
| 実現方法 | DB |

## 属性一覧

| 項目名 | 型 | 必須/任意 | 初期値 | 制約 |
|--------|-----|-----------|--------|------|
| id | Long | 必須 | - | 主キー |
| sprintId | Long | 必須 | - | Sprintの外部キー |
| title | String | 必須 | - | 最大200文字 |
| goal | String | 必須 | - | Sprint Goal |
| goalStatus | Enum | 必須 | - | 達成/未達成/一部達成 |
| completedStories | Integer | 任意 | 0 | 完了Story数 |
| plannedStories | Integer | 任意 | 0 | 計画Story数 |
| status | Enum | 必須 | Preparing | Preparing/InProgress/Completed |
| participants | String | 任意 | - | 参加者リスト |
| agenda | String | 任意 | - | 議題 |
| decisions | String | 任意 | - | 決定事項 |
| unresolvedItems | String | 任意 | - | 未解決事項 |
| createdAt | DateTime | 必須 | - | 作成日時 |
| createdBy | String | 必須 | - | 作成者 |
| updatedAt | DateTime | 必須 | - | 更新日時 |
| updatedBy | String | 必須 | - | 更新者 |

## 関連情報

| 情報ID | 情報名 | 関係 | 多重度 |
|--------|--------|------|--------|
| - | Sprint | 所属 | 1:1 |
| INF-MF-02 | フィードバック | 関連 | 1:N |
