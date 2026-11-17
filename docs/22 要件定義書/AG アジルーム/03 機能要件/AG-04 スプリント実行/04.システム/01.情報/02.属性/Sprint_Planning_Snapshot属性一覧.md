# INF-IDB-01 Sprint Planning Snapshot 情報属性一覧

## 情報概要

| 項目 | 値 |
|------|-----|
| 情報ID | INF-IDB-01 |
| 情報名 | Sprint Planning Snapshot |
| 情報種類 | リソース |
| 説明 | Sprint Planning完了時の計画情報をSnapshotとして保存する |
| 履歴管理要 | Y |
| 証跡要 | N |
| 実現方法 | DB |

## 個体指定子（主キー）

| 属性名 | 型 | 必須/任意 | 説明 |
|--------|----|-----------|------|
| id | Long | 必須 | Snapshotの一意識別子 |

## 属性

| 属性名 | 型 | 必須/任意 | 説明 |
|--------|----|-----------|------|
| sprintId | Long | 必須 | 関連するSprintのID |
| createdAt | DateTime | 必須 | Snapshot作成日時 |
| createdBy | String | 必須 | 作成者 |
| totalStoryPoints | Integer | 必須 | 計画時点のStory Points合計 |
| totalTasks | Integer | 必須 | 計画時点のTask数 |
| totalEstimatedHours | Double | 任意 | 計画時点の見積時間合計 |
| storyCount | Integer | 必須 | 計画時点のStory数 |
| goalText | String | 任意 | 計画時点のSprint Goal |
| sprintStartDate | Date | 必須 | 計画時点の開始日 |
| sprintEndDate | Date | 必須 | 計画時点の終了日 |

## 関連情報

| 情報ID | 情報名 | 関係 | 説明 |
|--------|--------|------|------|
| INF-SP-01 | スプリント | 1:1 | 1つのSprintに1つのSnapshot |
| INF-SP-02 | Sprint Goal | 1:1 | Goalテキストを保持 |

## エンティティの3大要素

1. **単一性 (Oneness)**: 1つのSprint Planning完了時点の計画情報のスナップショット
2. **同一性 (Sameness)**: id で一意に識別される
3. **カテゴリ (Category)**: Sprint Planningの計画情報
