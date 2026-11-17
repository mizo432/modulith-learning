# UC複合図（イテレーションダッシュボード）

イテレーションダッシュボード機能のユースケース、画面、イベント、情報、条件の関係性をPlantUMLで表現します。

## PlantUML

```plantuml
@startuml
skinparam backgroundColor white
skinparam rectangle {
  BackgroundColor LightGray
  BorderColor Black
}

left to right direction

actor "ACT-02\nプロダクトオーナー" as PO
actor "ACT-03\nスクラムマスター" as SM
actor "ACT-04\n開発者" as Dev
actor "ACT-05\nトラッキング担当" as Track

rectangle "SCR-029\nイテレーションダッシュボード" as Dashboard

usecase "UC-IDB-001\nダッシュボードを表示する" as UC001
usecase "UC-IDB-002\nSprintを切り替える" as UC002
usecase "UC-IDB-003\nSprint Goalを確認する" as UC003
usecase "UC-IDB-004\nSprintサマリーを確認する" as UC004
usecase "UC-IDB-005\nBurndownを確認する" as UC005
usecase "UC-IDB-006\nBurnupを確認する" as UC006
usecase "UC-IDB-007\nUser Story進捗を確認する" as UC007
usecase "UC-IDB-008\nTask進捗を確認する" as UC008
usecase "UC-IDB-009\nスコープ変更を確認する" as UC009
usecase "UC-IDB-010\nブロッカーを確認する" as UC010
usecase "UC-IDB-011\nリスクを確認する" as UC011
usecase "UC-IDB-012\nメトリクスを比較する" as UC012
usecase "UC-IDB-013\nTaskボードへ遷移する" as UC013
usecase "UC-IDB-014\nSprint Reviewへ遷移する" as UC014
usecase "UC-IDB-015\nRetrospectiveへ遷移する" as UC015

rectangle "INF-SP-01\nスプリント" as Sprint
rectangle "INF-SP-02\nSprint Goal" as Goal
rectangle "INF-SP-03\nSprint Backlog項目" as Backlog
rectangle "INF-SP-04\n障害" as Blocker
rectangle "INF-SP-10\nタスク" as Task
rectangle "INF-IDB-01\nSprint Planning Snapshot" as Snapshot
rectangle "INF-IDB-02\nSprint Scope Change" as ScopeChange
rectangle "INF-IDB-03\nTask Work Log" as WorkLog
rectangle "INF-IDB-04\nSprint Metrix" as Metrix

rectangle "COND-001\nSprint Goal中心表示" as Cond1
rectangle "COND-002\nSnapshot保存" as Cond2
rectangle "COND-003\n集計単位分離" as Cond3
rectangle "COND-004\n個人監視禁止" as Cond4

PO --> UC001
SM --> UC001
Dev --> UC001
Track --> UC001

PO --> UC002
SM --> UC002
Dev --> UC002
Track --> UC002

PO --> UC003
SM --> UC003
Dev --> UC003
Track --> UC003

PO --> UC004
SM --> UC004
Dev --> UC004
Track --> UC004

PO --> UC005
SM --> UC005
Dev --> UC005
Track --> UC005

PO --> UC006
SM --> UC006
Dev --> UC006
Track --> UC006

PO --> UC007
SM --> UC007
Dev --> UC007
Track --> UC007

PO --> UC008
SM --> UC008
Dev --> UC008
Track --> UC008

PO --> UC009
SM --> UC009
Dev --> UC009
Track --> UC009

PO --> UC010
SM --> UC010
Dev --> UC010
Track --> UC010

PO --> UC011
SM --> UC011
Dev --> UC011
Track --> UC011

PO --> UC012
SM --> UC012
Track --> UC012

SM --> UC013
Dev --> UC013

PO --> UC014
SM --> UC014
Dev --> UC014

SM --> UC015
Dev --> UC015

Dashboard ..> UC001
Dashboard ..> UC002
Dashboard ..> UC003
Dashboard ..> UC004
Dashboard ..> UC005
Dashboard ..> UC006
Dashboard ..> UC007
Dashboard ..> UC008
Dashboard ..> UC009
Dashboard ..> UC010
Dashboard ..> UC011
Dashboard ..> UC012

UC001 --> Sprint
UC001 --> Goal
UC001 --> Backlog
UC001 --> Blocker
UC001 --> Task
UC001 --> Snapshot
UC001 --> ScopeChange
UC001 --> WorkLog
UC001 --> Metrix

UC003 --> Goal
UC004 --> Backlog
UC004 --> Task
UC005 --> WorkLog
UC005 --> Metrix
UC006 --> Backlog
UC006 --> Metrix
UC007 --> Backlog
UC008 --> Task
UC009 --> ScopeChange
UC010 --> Blocker
UC011 --> Blocker
UC012 --> Metrix

UC001 ..> Cond1 : 適用
UC001 ..> Cond2 : 適用
UC005 ..> Cond3 : 適用
UC001 ..> Cond4 : 適用

@enduml
```

## 要素一覧

### アクター

| ID | 名前 | 説明 |
|----|------|------|
| ACT-02 | プロダクトオーナー | Sprint GoalとProduct Backlogの優先順位を踏まえ、成果を確認 |
| ACT-03 | スクラムマスター | 進捗の停滞、障害、チームの改善機会を発見 |
| ACT-04 | 開発者 | チーム全体の進捗、自分の作業、ブロッカーを確認 |
| ACT-05 | トラッキング担当 | スプリントの進捗や成果を把握 |

### 画面

| ID | 名前 | 説明 |
|----|------|------|
| SCR-029 | イテレーションダッシュボード | スプリントの進捗・成果・リスクを可視化する |

### 情報

| ID | 名前 | 説明 |
|----|------|------|
| INF-SP-01 | スプリント | スプリント基本情報 |
| INF-SP-02 | Sprint Goal | 目標と達成状況 |
| INF-SP-03 | Sprint Backlog項目 | 選択されたStory・Task |
| INF-SP-04 | 障害 | ブロッカー情報 |
| INF-SP-10 | タスク | Taskの状態・残作業 |
| INF-IDB-01 | Sprint Planning Snapshot | 計画時点のSnapshot |
| INF-IDB-02 | Sprint Scope Change | スコープ変更履歴 |
| INF-IDB-03 | Task Work Log | 作業実績 |
| INF-IDB-04 | Sprint Metrix | 集計メトリクス |

### 条件（業務ルール）

| ID | 名前 | 種類 | 説明 |
|----|------|------|------|
| COND-001 | Sprint Goal中心表示 | Policy | ダッシュボードはSprint Goalの達成状況を最優先に表示する |
| COND-002 | Snapshot保存 | Operation | Sprint Planning完了時に計画情報をSnapshotとして保存する |
| COND-003 | 集計単位分離 | Policy | Story PointsとTask残作業時間は別のグラフに表示する |
| COND-004 | 個人監視禁止 | Policy | 個人の生産性を評価するための画面にしない |
