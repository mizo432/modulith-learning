# フィードバック 情報属性一覧

## 概要

フィードバック（INF-MF-02）の属性を定義します。

## 情報概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-MF-02 |
| 情報名 | フィードバック |
| 情報種類 | リソース |
| 説明 | 利用者からのフィードバック内容・評価・対応状況 |
| 履歴管理要 | Y |
| 証跡要 | N |
| 実現方法 | DB |

## 属性一覧

| 項目名 | 型 | 必須/任意 | 初期値 | 制約 |
|--------|-----|-----------|--------|------|
| id | Long | 必須 | - | 主キー |
| title | String | 必須 | - | 最大200文字 |
| content | String | 必須 | - | フィードバック内容 |
| type | Enum | 必須 | - | INF-MF-13参照 |
| source | String | 任意 | - | 情報源 |
| status | Enum | 必須 | Pending | Pending/Evaluating/Resolved/Hold |
| decision | String | 任意 | - | 対応方針 |
| reason | String | 任意 | - | 判断理由 |
| createdAt | DateTime | 必須 | - | 作成日時 |
| createdBy | String | 必須 | - | 作成者 |
| updatedAt | DateTime | 必須 | - | 更新日時 |
| updatedBy | String | 必須 | - | 更新者 |

## 関連情報

| 情報ID | 情報名 | 関係 | 多重度 |
|--------|--------|------|--------|
| INF-MF-01 | Sprint Review | 所属 | N:1 |
| INF-MF-03 | フィードバック対応 | 関連 | 1:N |
| INF-MF-13 | フィードバック種別 | 参照 | N:1 |
