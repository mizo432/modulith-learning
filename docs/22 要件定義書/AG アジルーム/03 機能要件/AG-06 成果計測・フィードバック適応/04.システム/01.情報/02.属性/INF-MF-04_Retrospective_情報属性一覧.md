# Retrospective 情報属性一覧

## 概要

Retrospective（INF-MF-04）の属性を定義します。

## 情報概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-MF-04 |
| 情報名 | Retrospective |
| 情報種類 | リソース |
| 説明 | 振り返りの記録（学び、改善候補、決定事項） |
| 履歴管理要 | Y |
| 証跡要 | Y |
| 実現方法 | DB |

## 属性一覧

| 項目名 | 型 | 必須/任意 | 初期値 | 制約 |
|--------|-----|-----------|--------|------|
| id | Long | 必須 | - | 主キー |
| sprintId | Long | 必須 | - | Sprintの外部キー |
| title | String | 必須 | - | 最大200文字 |
| wentWell | String | 任意 | - | 良かった点 |
| toImprove | String | 任意 | - | 改善すべき点 |
| learnings | String | 任意 | - | 学び |
| decisions | String | 任意 | - | 決定事項 |
| createdAt | DateTime | 必須 | - | 作成日時 |
| createdBy | String | 必須 | - | 作成者 |
| updatedAt | DateTime | 必須 | - | 更新日時 |
| updatedBy | String | 必須 | - | 更新者 |

## 関連情報

| 情報ID | 情報名 | 関係 | 多重度 |
|--------|--------|------|--------|
| - | Sprint | 所属 | 1:1 |
| INF-MF-05 | 改善アクション | 関連 | 1:N |
