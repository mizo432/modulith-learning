# UC複合図

スプリント実行のユースケースと関連要素をPlantUMLで視覚化します。

## ユースケース図

```plantuml
@startuml
left to right direction
skinparam packageStyle rectangle

actor "プロダクトオーナー" as PO
actor "スクラムマスター" as SM
actor "開発者" as Dev
actor "システム管理者" as Admin

package "BUC-SP-01 スプリント計画" {
  usecase "スプリントを作成する" as UC001
  usecase "スプリントを更新する" as UC002
  usecase "スプリントを参照する" as UC003
  usecase "Sprint Goalを設定する" as UC004
  usecase "Sprint Goalを更新する" as UC005
  usecase "Sprint Backlogを作成する" as UC006
}

package "BUC-SP-02 スプリント実行" {
  usecase "Sprint Backlogを更新する" as UC007
  usecase "ボードを更新する" as UC008
  usecase "依存関係を定義する" as UC012
  usecase "デイリースクラムを記録する" as UC016
  usecase "ソース管理情報を関連付ける" as UC018
  usecase "ビルド・デプロイ状況を確認する" as UC019
}

package "BUC-SP-03 障害解消" {
  usecase "障害を登録する" as UC009
  usecase "障害を更新する" as UC010
  usecase "障害を参照する" as UC011
}

package "BUC-SP-04 進捗共有" {
  usecase "通知を送信する" as UC013
  usecase "スプリント進捗を確認する" as UC014
  usecase "作業負荷を確認する" as UC017
}

package "BUC-SP-05 完成確認" {
  usecase "インクリメントを完成確認する" as UC015
}

PO --> UC001
PO --> UC002
PO --> UC003
PO --> UC004
PO --> UC005
PO --> UC014

SM --> UC001
SM --> UC002
SM --> UC003
SM --> UC004
SM --> UC005
SM --> UC006
SM --> UC009
SM --> UC010
SM --> UC013
SM --> UC014
SM --> UC017

Dev --> UC001
Dev --> UC002
Dev --> UC003
Dev --> UC004
Dev --> UC005
Dev --> UC006
Dev --> UC007
Dev --> UC008
Dev --> UC009
Dev --> UC010
Dev --> UC011
Dev --> UC012
Dev --> UC013
Dev --> UC014
Dev --> UC015
Dev --> UC016
Dev --> UC018
Dev --> UC019

Admin --> UC003
Admin --> UC011
Admin --> UC014
@enduml
```

## 関連要素一覧

### 情報

| 情報ID | 情報名 | 関連UC |
|--------|--------|--------|
| INF-SP-01 | スプリント | UC-SP-001, UC-SP-002, UC-SP-003, UC-SP-004, UC-SP-005 |
| INF-SP-02 | スプリントバックログ | UC-SP-006, UC-SP-007, UC-SP-008, UC-SP-012, UC-SP-015 |
| INF-SP-03 | 進捗 | UC-SP-014, UC-SP-017 |
| INF-SP-04 | バーンダウンチャート | UC-SP-014 |

### 条件

| 条件ID | 条件名 | 関連UC |
|--------|--------|--------|
| COND-SP-01 | スプリント期間の制約 | UC-SP-001 |
| COND-SP-02 | タスクの担当者必須 | UC-SP-007 |
| COND-SP-03 | 依存関係の循環禁止 | UC-SP-012 |
| COND-SP-04 | 状態遷移の制約 | UC-SP-001, UC-SP-002, UC-SP-007, UC-SP-008 |
| COND-SP-05 | インクリメントの完成条件 | UC-SP-015 |

### 画面

| 画面ID | 画面名 | 関連UC |
|--------|--------|--------|
| SCR-SP-001 | スプリント一覧画面 | UC-SP-003, UC-SP-014 |
| SCR-SP-002 | スプリント詳細画面 | UC-SP-001, UC-SP-002, UC-SP-004, UC-SP-005, UC-SP-006 |
| SCR-SP-003 | カンバンボード画面 | UC-SP-007, UC-SP-008, UC-SP-009, UC-SP-010, UC-SP-011, UC-SP-012, UC-SP-013, UC-SP-015, UC-SP-017, UC-SP-018, UC-SP-019 |
