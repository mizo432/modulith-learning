# Story Map Persona 情報属性一覧

Story MapとPersonaの関連を定義します。

## 情報概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-BL-19 |
| 情報名 | Story Map Persona |
| 情報種類 | 対応表 |
| 説明 | Story MapとPersonaの関連 |
| 履歴管理要 | N |
| 証跡要 | N |
| 実現方法 | DB |

## 個体指定子と属性

| 属性名 | 型 | 必須 | 説明 |
|--------|----|------|------|
| * id | Long | はい | 関連の一意な識別子 |
| storyMapId | Long | はい | 属するStory Mapの識別子 |
| personaId | Long | はい | 関連するPersonaの識別子 |
| createdAt | DateTime | はい | 設定日時 |

## 関連情報

| 情報ID | 情報名 | 関係 | 説明 |
|--------|--------|------|------|
| INF-BL-13 | Story Map | N:1 | 属するStory Map |

## エンティティの3大要素

1. **単一性 (Oneness)**: 1つのStory Mapと1つのPersonaの関連を1つの単位として管理する
2. **同一性 (Sameness)**: idで一意に識別される
3. **カテゴリ (Category)**: Story Map Personaは、マップの対象ユーザーを複数設定するための関連モデル
