# Story Map 変更履歴 情報属性一覧

Story Mapの変更内容・変更者・変更日時を定義します。

## 情報概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-BL-21 |
| 情報名 | Story Map 変更履歴 |
| 情報種類 | イベント |
| 説明 | Story Mapの変更内容・変更者・変更日時 |
| 履歴管理要 | N |
| 証跡要 | N |
| 実現方法 | DB |

## 個体指定子と属性

| 属性名 | 型 | 必須 | 説明 |
|--------|----|------|------|
| * id | Long | はい | 変更履歴の一意な識別子 |
| storyMapId | Long | はい | 属するStory Mapの識別子 |
| changeType | Enum | はい | 変更タイプ（CREATE, UPDATE, DELETE, REORDER, VERSION） |
| targetEntity | String | いいえ | 変更対象のエンティティ種別 |
| targetId | Long | いいえ | 変更対象の識別子 |
| changeContent | String | いいえ | 変更内容の詳細（最大2000文字） |
| changedById | Long | はい | 変更者の識別子 |
| changedAt | DateTime | はい | 変更日時 |

## 関連情報

| 情報ID | 情報名 | 関係 | 説明 |
|--------|--------|------|------|
| INF-BL-13 | Story Map | N:1 | 属するStory Map |

## エンティティの3大要素

1. **単一性 (Oneness)**: 1つの変更イベントを1つの単位として管理する
2. **同一性 (Sameness)**: idで一意に識別される
3. **カテゴリ (Category)**: Story Map 変更履歴は、マップの変更を追跡・監査するためのイベントモデル
