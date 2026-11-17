# 改善アクション 情報属性一覧

## 概要

改善アクション（INF-MF-05）の属性を定義します。

## 情報概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-MF-05 |
| 情報名 | 改善アクション |
| 情報種類 | リソース |
| 説明 | 改善の担当、期限、状態、効果 |
| 履歴管理要 | Y |
| 証跡要 | Y |
| 実現方法 | DB |

## 属性一覧

| 項目名 | 型 | 必須/任意 | 初期値 | 制約 |
|--------|-----|-----------|--------|------|
| id | Long | 必須 | - | 主キー |
| title | String | 必須 | - | 最大200文字 |
| content | String | 必須 | - | 改善内容 |
| category | Enum | 必須 | - | INF-MF-14参照 |
| assignee | String | 必須 | - | 担当者 |
| deadline | Date | 任意 | - | 期限 |
| status | Enum | 必須 | Pending | Pending/InProgress/Completed/Cancelled |
| effect | String | 任意 | - | 改善効果 |
| createdAt | DateTime | 必須 | - | 作成日時 |
| createdBy | String | 必須 | - | 作成者 |
| updatedAt | DateTime | 必須 | - | 更新日時 |
| updatedBy | String | 必須 | - | 更新者 |

## 関連情報

| 情報ID | 情報名 | 関係 | 多重度 |
|--------|--------|------|--------|
| INF-MF-04 | Retrospective | 所属 | N:1 |
| INF-MF-14 | 改善カテゴリ | 参照 | N:1 |
