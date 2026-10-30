# Story Map Comment 情報属性一覧

Story Map上のコメント・議論を定義します。

## 情報概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-BL-20 |
| 情報名 | Story Map Comment |
| 情報種類 | リソース |
| 説明 | Story Map上のコメント・議論 |
| 履歴管理要 | Y |
| 証跡要 | N |
| 実現方法 | DB |

## 個体指定子と属性

| 属性名 | 型 | 必須 | 説明 |
|--------|----|------|------|
| * id | Long | はい | コメントの一意な識別子 |
| storyMapId | Long | はい | 属するStory Mapの識別子 |
| content | String | はい | コメント内容（最大2000文字） |
| authorId | Long | はい | 投稿者の識別子 |
| createdAt | DateTime | はい | 投稿日時 |
| updatedAt | DateTime | いいえ | 更新日時 |

## 関連情報

| 情報ID | 情報名 | 関係 | 説明 |
|--------|--------|------|------|
| INF-BL-13 | Story Map | N:1 | 属するStory Map |

## エンティティの3大要素

1. **単一性 (Oneness)**: 1つのコメントを1つの単位として管理する
2. **同一性 (Sameness)**: idで一意に識別される
3. **カテゴリ (Category)**: Story Map Commentは、マップ上でメンバーが議論・意見を記録するためのモデル
