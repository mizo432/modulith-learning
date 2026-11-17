# Story Map Release Slice 情報属性一覧

Story Map上のリリース単位のスライスを定義します。

## 情報概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-BL-17 |
| 情報名 | Story Map Release Slice |
| 情報種類 | リソース |
| 説明 | Story Map上のリリース単位のスライス |
| 履歴管理要 | Y |
| 証跡要 | N |
| 実現方法 | DB |

## 個体指定子と属性

| 属性名 | 型 | 必須 | 説明 |
|--------|----|------|------|
| * id | Long | はい | Release Sliceの一意な識別子 |
| storyMapId | Long | はい | 属するStory Mapの識別子 |
| name | String | はい | スライス名（最大200文字） |
| description | String | いいえ | 提供価値の説明（最大1000文字） |
| displayOrder | Integer | はい | 表示順序 |
| createdAt | DateTime | はい | 作成日時 |
| updatedAt | DateTime | はい | 最終更新日時 |

## 関連情報

| 情報ID | 情報名 | 関係 | 説明 |
|--------|--------|------|------|
| INF-BL-13 | Story Map | N:1 | 属するStory Map |
| INF-BL-18 | Release Slice Story | 1:N | スライスに含まれるStory一覧 |

## エンティティの3大要素

1. **単一性 (Oneness)**: 1つのリリース単位のスライスを1つの単位として管理する
2. **同一性 (Sameness)**: idで一意に識別される
3. **カテゴリ (Category)**: Release Sliceは、Story Map上で価値を考えるための仮説・計画。正式なRelease Scopeとは区別する
