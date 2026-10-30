# Story Map Story 情報属性一覧

Story MapとUser Storyの関連・配置・順序を定義します。

## 情報概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-BL-16 |
| 情報名 | Story Map Story |
| 情報種類 | 対応表 |
| 説明 | Story MapとUser Storyの関連・配置・順序 |
| 履歴管理要 | N |
| 証跡要 | N |
| 実現方法 | DB |

## 個体指定子と属性

| 属性名 | 型 | 必須 | 説明 |
|--------|----|------|------|
| * id | Long | はい | 関連の一意な識別子 |
| taskId | Long | はい | 属するTaskの識別子 |
| backlogItemId | Long | はい | 関連するバックログ項目（User Story）の識別子 |
| displayOrder | Integer | はい | Task内での表示順序 |
| createdAt | DateTime | はい | 配置日時 |

## 関連情報

| 情報ID | 情報名 | 関係 | 説明 |
|--------|--------|------|------|
| INF-BL-15 | Story Map Task | N:1 | 属するTask |
| INF-BL-01 | バックログ項目 | N:1 | 関連するUser Story（参照） |

## エンティティの3大要素

1. **単一性 (Oneness)**: 1つのTaskと1つのUser Storyの配置関係を1つの単位として管理する
2. **同一性 (Sameness)**: idで一意に識別される
3. **カテゴリ (Category)**: Story Map Storyは、User Storyそのものではなく、Story Map上の配置・順序・関連情報を管理するモデル
