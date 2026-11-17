# Story Map Task 情報属性一覧

Activityを構成する具体的なユーザー行動を定義します。

## 情報概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-BL-15 |
| 情報名 | Story Map Task |
| 情報種類 | リソース |
| 説明 | Activityを構成する具体的なユーザー行動 |
| 履歴管理要 | Y |
| 証跡要 | N |
| 実現方法 | DB |

## 個体指定子と属性

| 属性名 | 型 | 必須 | 説明 |
|--------|----|------|------|
| * id | Long | はい | Taskの一意な識別子 |
| activityId | Long | はい | 属するActivityの識別子 |
| name | String | はい | Task名（最大500文字） |
| description | String | いいえ | Taskの詳細説明（最大1000文字） |
| expectedOutcome | String | いいえ | ユーザーの期待する結果（最大1000文字） |
| painPoint | String | いいえ | 関連するPain Point（最大1000文字） |
| opportunity | String | いいえ | 関連するOpportunity（最大1000文字） |
| displayOrder | Integer | はい | 表示順序 |
| createdAt | DateTime | はい | 作成日時 |
| updatedAt | DateTime | はい | 最終更新日時 |

## 関連情報

| 情報ID | 情報名 | 関係 | 説明 |
|--------|--------|------|------|
| INF-BL-14 | Story Map Activity | N:1 | 属するActivity |
| INF-BL-16 | Story Map Story | 1:N | Taskに配置されたStory一覧 |

## エンティティの3大要素

1. **単一性 (Oneness)**: 1つの具体的なユーザー行動を1つの単位として管理する
2. **同一性 (Sameness)**: idで一意に識別される
3. **カテゴリ (Category)**: Taskは、Activityを構成する具体的なユーザー行動を表す要素。Sprint Planningで利用する開発Taskとは別モデル
