# Sprint Reviewが開始された

イベントの詳細を定義します。

## 概要

| 項目 | 内容 |
|------|------|
| イベントID | EVT-SR-001 |
| イベント名 | Sprint Reviewが開始された |
| 説明 | 対象SprintのSprint Reviewが開始された |
| 関連ユースケース | UC-SR-001 |
| 関連情報 | INF-SR-01 |
| 集約 | Sprint Review |

## 属性

| 項目名 | 型 | 必須 | 説明 |
|--------|----|------|------|
| ID | 文字列 | はい | イベントの一意な識別子 |
| 発生日時 | 日付 | はい | イベントが発生した日時 |
| SprintReviewID | 文字列 | はい | 関連するSprint ReviewのID |
| SprintID | 文字列 | はい | 関連するSprintのID |
| 開始者 | 文字列 | はい | Sprint Reviewを開始したユーザー |
