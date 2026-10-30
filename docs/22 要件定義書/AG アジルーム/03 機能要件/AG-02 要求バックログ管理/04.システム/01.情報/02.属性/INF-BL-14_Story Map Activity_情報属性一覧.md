# Story Map Activity 情報属性一覧

Story Map上のユーザーの大きな活動を定義します。

## 情報概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-BL-14 |
| 情報名 | Story Map Activity |
| 情報種類 | リソース |
| 説明 | Story Map上のユーザーの大きな活動 |
| 履歴管理要 | Y |
| 証跡要 | N |
| 実現方法 | DB |

## 個体指定子と属性

| 属性名 | 型 | 必須 | 説明 |
|--------|----|------|------|
| * id | Long | はい | Activityの一意な識別子 |
| storyMapId | Long | はい | 属するStory Mapの識別子 |
| name | String | はい | Activity名（最大255文字） |
| description | String | いいえ | Activityの詳細説明（最大1000文字） |
| personaId | Long | いいえ | 対象Personaの識別子 |
| displayOrder | Integer | はい | 表示順序 |
| createdAt | DateTime | はい | 作成日時 |
| updatedAt | DateTime | はい | 最終更新日時 |

## 関連情報

| 情報ID | 情報名 | 関係 | 説明 |
|--------|--------|------|------|
| INF-BL-13 | Story Map | N:1 | 属するStory Map |
| INF-BL-15 | Story Map Task | 1:N | Activityに属するTask一覧 |

## エンティティの3大要素

1. **単一性 (Oneness)**: 1つのユーザーの大きな活動を1つの単位として管理する
2. **同一性 (Sameness)**: idで一意に識別される
3. **カテゴリ (Category)**: Activityは、ユーザーがプロダクトを利用する一連の体験を横方向に表現する要素
