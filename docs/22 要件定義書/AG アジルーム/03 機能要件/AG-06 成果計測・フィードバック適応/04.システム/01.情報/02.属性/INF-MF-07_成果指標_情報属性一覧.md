# 成果指標 情報属性一覧

## 概要

成果指標（INF-MF-07）の属性を定義します。

## 情報概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-MF-07 |
| 情報名 | 成果指標 |
| 情報種類 | リソース |
| 説明 | 指標の定義、基準値、目標値、測定値 |
| 履歴管理要 | N |
| 証跡要 | N |
| 実現方法 | DB |

## 属性一覧

| 項目名 | 型 | 必須/任意 | 初期値 | 制約 |
|--------|-----|-----------|--------|------|
| id | Long | 必須 | - | 主キー |
| name | String | 必須 | - | 最大100文字 |
| definition | String | 必須 | - | 指標の定義 |
| baselineValue | Double | 任意 | - | 基準値 |
| targetValue | Double | 任意 | - | 目標値 |
| unit | String | 任意 | - | 単位 |
| dataSource | String | 任意 | - | データ元 |
| updateFrequency | Enum | 任意 | - | 更新頻度 |
| createdAt | DateTime | 必須 | - | 作成日時 |
| createdBy | String | 必須 | - | 作成者 |
| updatedAt | DateTime | 必須 | - | 更新日時 |
| updatedBy | String | 必須 | - | 更新者 |

## 関連情報

| 情報ID | 情報名 | 関係 | 多重度 |
|--------|--------|------|--------|
| INF-MF-08 | 指標測定値 | 関連 | 1:N |
