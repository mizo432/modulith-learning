# Release Slice Story 情報属性一覧

Release SliceとUser Storyの関連を定義します。

## 情報概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-BL-18 |
| 情報名 | Release Slice Story |
| 情報種類 | 対応表 |
| 説明 | Release SliceとUser Storyの関連 |
| 履歴管理要 | N |
| 証跡要 | N |
| 実現方法 | DB |

## 個体指定子と属性

| 属性名 | 型 | 必須 | 説明 |
|--------|----|------|------|
| * id | Long | はい | 関連の一意な識別子 |
| releaseSliceId | Long | はい | 属するRelease Sliceの識別子 |
| backlogItemId | Long | はい | 関連するバックログ項目（User Story）の識別子 |
| createdAt | DateTime | はい | 割り当て日時 |

## 関連情報

| 情報ID | 情報名 | 関係 | 説明 |
|--------|--------|------|------|
| INF-BL-17 | Story Map Release Slice | N:1 | 属するRelease Slice |
| INF-BL-01 | バックログ項目 | N:1 | 関連するUser Story（参照） |

## エンティティの3大要素

1. **単一性 (Oneness)**: 1つのRelease Sliceと1つのUser Storyの関連を1つの単位として管理する
2. **同一性 (Sameness)**: idで一意に識別される
3. **カテゴリ (Category)**: Release Slice Storyは、Storyをリリース単位に分類するための関連モデル
