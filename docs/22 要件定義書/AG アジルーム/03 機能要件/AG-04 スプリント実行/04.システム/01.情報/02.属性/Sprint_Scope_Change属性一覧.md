# INF-IDB-02 Sprint Scope Change 情報属性一覧

## 情報概要

| 項目 | 値 |
|------|-----|
| 情報ID | INF-IDB-02 |
| 情報名 | Sprint Scope Change |
| 情報種類 | イベント |
| 説明 | Sprint Backlogのスコープ変更履歴を記録する |
| 履歴管理要 | N |
| 証跡要 | N |
| 実現方法 | DB |

## 個体指定子（主キー）

| 属性名 | 型 | 必須/任意 | 説明 |
|--------|----|-----------|------|
| id | Long | 必須 | スコープ変更の一意識別子 |

## 属性

| 属性名 | 型 | 必須/任意 | 説明 |
|--------|----|-----------|------|
| sprintId | Long | 必須 | 関連するSprintのID |
| changeType | Enum | 必須 | 変更種類 (ADD/REMOVE/UPDATE) |
| changedAt | DateTime | 必須 | 変更日時 |
| changedBy | String | 必須 | 変更者 |
| storyId | Long | 任意 | 変更対象StoryのID |
| storyPointsBefore | Integer | 任意 | 変更前のStory Points |
| storyPointsAfter | Integer | 任意 | 変更後のStory Points |
| description | String | 任意 | 変更理由・説明 |

## 関連情報

| 情報ID | 情報名 | 関係 | 説明 |
|--------|--------|------|------|
| INF-SP-01 | スプリント | 1:N | 1つのSprintに複数のスコープ変更 |
| INF-SP-03 | Sprint Backlog項目 | 1:N | 1つのBacklog項目に複数の変更 |

## エンティティの3大要素

1. **単一性 (Oneness)**: 1つのスコープ変更イベント
2. **同一性 (Sameness)**: id で一意に識別される
3. **カテゴリ (Category)**: Sprint Backlogの変更履歴
