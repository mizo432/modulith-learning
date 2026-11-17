# UC複合図

バックログ管理のユースケースと関連要素をPlantUMLで視覚化します。

## ユースケース図

```plantuml
@startuml
left to right direction
skinparam packageStyle rectangle

actor "プロダクトオーナー" as PO
actor "開発者" as Dev
actor "従業員" as Emp
actor "システム管理者" as Admin

package "BUC-BL-01 課題・要望の受付・記録" {
  usecase "課題を登録する" as UC001
  usecase "課題を更新する" as UC002
  usecase "課題を参照する" as UC003
  usecase "コメントを投稿する" as UC016
  usecase "メンション通知を送信する" as UC017
}

package "BUC-BL-02 バックログ項目の作成・明確化" {
  usecase "バックログ項目を作成する" as UC007
  usecase "バックログ項目を更新する" as UC008
  usecase "バックログ項目を参照する" as UC009
  usecase "バックログ階層を設定する" as UC011
  usecase "業務ルールを登録する" as UC014
  usecase "受入条件を定義する" as UC015
  usecase "意思決定を記録する" as UC022
  usecase "Story Mappingを行う" as UC023
}

package "BUC-BL-03 バックログリファインメント" {
  usecase "バックログ項目を分割する" as UC010
  usecase "バックログリファインメントを行う" as UC018
  usecase "依存関係を定義する" as UC019
  usecase "見積もりを記録する" as UC024
}

package "BUC-BL-04 バックログの優先順位管理" {
  usecase "優先順位を設定する" as UC012
  usecase "優先順位を更新する" as UC013
}

package "BUC-BL-05 フィードバックの対応・追跡" {
  usecase "フィードバックを登録する" as UC004
  usecase "フィードバックを更新する" as UC005
  usecase "フィードバックを参照する" as UC006
}

package "BUC-BL-06 要求の関連付け・トレーサビリティ" {
  usecase "変更履歴を参照する" as UC020
  usecase "要求の関連を設定する" as UC021
  usecase "ソース管理情報を関連付ける" as UC025
}

PO --> UC001
PO --> UC002
PO --> UC007
PO --> UC008
PO --> UC012
PO --> UC013
PO --> UC004
PO --> UC005

Dev --> UC007
Dev --> UC008
Dev --> UC009
Dev --> UC010
Dev --> UC018
Dev --> UC019
Dev --> UC024
Dev --> UC020
Dev --> UC021
Dev --> UC025

Emp --> UC001
Emp --> UC003
Emp --> UC004
Emp --> UC006
Emp --> UC009

Admin --> UC003
Admin --> UC006
Admin --> UC020

note right of UC007
  関連情報:
  - バックログ項目
  - ストーリーポイント
  - ラベル
end note

note right of UC012
  関連情報:
  - バックログ項目
end note

note right of UC024
  関連情報:
  - ストーリーポイント
end note
@enduml
```

## 関連要素一覧

### 情報

| 情報ID | 情報名 | 関連UC |
|--------|--------|--------|
| INF-BL-01 | バックログ項目 | UC-BL-001〜UC-BL-025 |
| INF-BL-02 | ストーリーポイント | UC-BL-024 |
| INF-BL-03 | ラベル | UC-BL-008 |

### 条件

| 条件ID | 条件名 | 関連UC |
|--------|--------|--------|
| COND-BL-01 | バックログ項目の必須属性 | UC-BL-007 |
| COND-BL-02 | 優先順位の一意性 | UC-BL-012, UC-BL-013 |
| COND-BL-03 | ストーリーポイントの範囲 | UC-BL-024 |
| COND-BL-04 | ラベルの重複禁止 | UC-BL-008 |
| COND-BL-05 | 状態遷移の制約 | UC-BL-007, UC-BL-008, UC-BL-010, UC-BL-018 |

### 画面

| 画面ID | 画面名 | 関連UC |
|--------|--------|--------|
| SCR-BL-001 | バックログ一覧画面 | UC-BL-003, UC-BL-009, UC-BL-006, UC-BL-018 |
| SCR-BL-002 | バックログ詳細画面 | UC-BL-001, UC-BL-002, UC-BL-007, UC-BL-008, UC-BL-010, UC-BL-012, UC-BL-013, UC-BL-019, UC-BL-020, UC-BL-021, UC-BL-024, UC-BL-025 |
| SCR-BL-003 | バックログ編集画面 | UC-BL-007 |
