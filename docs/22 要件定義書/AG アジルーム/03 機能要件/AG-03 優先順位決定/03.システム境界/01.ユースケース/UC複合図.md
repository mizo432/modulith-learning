# UC複合図

優先順位決定のユースケースと関連要素をPlantUMLで視覚化します。

## ユースケース図

```plantuml
@startuml
left to right direction
skinparam packageStyle rectangle

actor "プロダクトオーナー" as PO
actor "開発者" as Dev
actor "従業員" as Emp
actor "システム管理者" as Admin

package "BUC-PR-01 評価基準の定義" {
  usecase "評価基準を定義する" as UC001
  usecase "評価基準を更新する" as UC002
  usecase "評価基準を参照する" as UC003
}

package "BUC-PR-02 優先順位評価" {
  usecase "価値を評価する" as UC004
  usecase "Kano分類を行う" as UC005
  usecase "リスクを評価する" as UC006
  usecase "コストを見積もる" as UC007
}

package "BUC-PR-03 優先順位付け" {
  usecase "優先順位を計算する" as UC008
  usecase "優先順位を決定する" as UC009
  usecase "投資判断を記録する" as UC010
}

package "BUC-PR-04 依存関係・リスク管理" {
  usecase "依存関係を定義する" as UC011
  usecase "依存関係を更新する" as UC012
  usecase "リスクを特定する" as UC013
  usecase "対応策を定義する" as UC014
}

package "BUC-PR-05 結果の可視化" {
  usecase "優先順位を参照する" as UC015
  usecase "優先順位を可視化する" as UC016
  usecase "変更履歴を確認する" as UC017
}

PO --> UC001
PO --> UC002
PO --> UC004
PO --> UC005
PO --> UC008
PO --> UC009
PO --> UC010
PO --> UC016

Dev --> UC006
Dev --> UC007
Dev --> UC011
Dev --> UC012
Dev --> UC013
Dev --> UC014

Emp --> UC003
Emp --> UC015
Emp --> UC017

Admin --> UC003
Admin --> UC015
Admin --> UC017
@enduml
```

## 関連要素一覧

### 情報

| 情報ID | 情報名 | 関連UC |
|--------|--------|--------|
| INF-PR-01 | 優先順位モデル | UC-PR-008, UC-PR-009, UC-PR-011, UC-PR-012 |
| INF-PR-02 | 評価基準 | UC-PR-001, UC-PR-002, UC-PR-003 |
| INF-PR-03 | 評価結果 | UC-PR-004, UC-PR-005, UC-PR-006, UC-PR-007, UC-PR-008, UC-PR-015 |

### 条件

| 条件ID | 条件名 | 関連UC |
|--------|--------|--------|
| COND-PR-01 | 評価基準の必須属性 | UC-PR-001 |
| COND-PR-02 | 優先度スコアの範囲 | UC-PR-008 |
| COND-PR-03 | Kanoカテゴリの排他性 | UC-PR-005 |
| COND-PR-04 | 依存関係の循環禁止 | UC-PR-011 |
| COND-PR-05 | 状態遷移の制約 | UC-PR-004, UC-PR-008, UC-PR-009 |

### 画面

| 画面ID | 画面名 | 関連UC |
|--------|--------|--------|
| SCR-PR-001 | 優先順位一覧画面 | UC-PR-015, UC-PR-016, UC-PR-017 |
| SCR-PR-002 | 評価基準編集画面 | UC-PR-001, UC-PR-002 |
| SCR-PR-003 | 評価結果詳細画面 | UC-PR-004, UC-PR-005, UC-PR-006, UC-PR-007, UC-PR-008, UC-PR-009 |
