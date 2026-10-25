# 情報ER図

スプリント実行とインクリメント形成業務領域で扱う情報の関係を示すER図です。

## 1. 目的

本書は、AG-04 スプリント実行業務領域のシステムレイヤーで扱う情報エンティティ間の関係を示すER図です。

## 2. PlantUML ER図

```plantuml
@startuml
title AG-04 スプリント実行 情報ER図
hide circle
skinparam linetype ortho
skinparam shadowing false
skinparam entity {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam packageStyle rectangle

entity "Sprint" as Sprint {
  * sprint_id : SnowflakeID <<PK>>
  --
  team_id : UUID <<FK>>
}

entity "SprintBacklogItem" as SprintBacklogItem {
  * sprint_backlog_item_id : SnowflakeID <<PK>>
  --
  sprint_id : SnowflakeID <<FK>>
  backlog_item_id : SnowflakeID <<FK>>
}

entity "Task" as Task {
  * task_id : SnowflakeID <<PK>>
  --
  sprint_backlog_item_id : SnowflakeID <<FK>>
}

entity "TaskAssignment" as TaskAssignment {
  * task_assignment_id : SnowflakeID <<PK>>
  --
  task_id : SnowflakeID <<FK>>
  employee_id : UUID <<FK Employee>>
}

entity "Notification" as Notification {
  * notification_id : SnowflakeID <<PK>>
  --
  recipient_id : UUID <<FK Employee>>
  project_id : UUID <<FK>>
}

' Sprint plans SprintBacklogItems
Sprint ||--o{ SprintBacklogItem : plans

' SprintBacklogItem decomposes into Tasks
SprintBacklogItem ||--o{ Task : decomposes

' Task is assigned to Employees
Task ||--o{ TaskAssignment : assigned through

' Employee works on TaskAssignment
TaskAssignment }o--|| Employee : works on

' Sprint sends Notifications
Sprint ||--o{ Notification : concerns

@enduml
```

## 3. 日本語版 ER図

```plantuml
@startuml
title AG-04 スプリント実行 情報ER図（日本語版）
hide circle
skinparam linetype ortho
skinparam shadowing false
skinparam entity {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam packageStyle rectangle

entity "スプリント" as Sprint {
  * sprint_id : SnowflakeID <<PK>>
  --
  team_id : UUID <<FK>>
}

entity "スプリントバックログ項目" as SprintBacklogItem {
  * sprint_backlog_item_id : SnowflakeID <<PK>>
  --
  sprint_id : SnowflakeID <<FK>>
  backlog_item_id : SnowflakeID <<FK>>
}

entity "タスク" as Task {
  * task_id : SnowflakeID <<PK>>
  --
  sprint_backlog_item_id : SnowflakeID <<FK>>
}

entity "タスク割り当て" as TaskAssignment {
  * task_assignment_id : SnowflakeID <<PK>>
  --
  task_id : SnowflakeID <<FK>>
  employee_id : UUID <<FK Employee>>
}

entity "通知" as Notification {
  * notification_id : SnowflakeID <<PK>>
  --
  recipient_id : UUID <<FK Employee>>
  project_id : UUID <<FK>>
}

' スプリントからスプリントバックログ項目へ
スプリント ||--o{ スプリントバックログ項目 : 計画

' スプリントバックログ項目からタスクへ
スプリントバックログ項目 ||--o{ タスク : 分解

' タスクからタスク割り当てへ
タスク ||--o{ タスク割り当て : 割り当て

' スプリントから通知へ
スプリント ||--o{ 通知 : 関連

@enduml
```

## 4. エンティティ一覧

| エンティティ      | 情報名 (ID)                    | 説明                                 |
|-------------------|--------------------------------|--------------------------------------|
| Sprint            | スプリント (INF-SP-01)         | スプリント期間、状態、チーム         |
| SprintBacklogItem | Sprint Backlog項目 (INF-SP-03) | 選択項目、作業、状態、担当           |
| Task              | タスク                         | 作業項目、見積もり、状態             |
| TaskAssignment    | 作業担当                       | 作業と担当者の割り当て               |
| Notification      | 通知 (INF-SP-06)               | メンション、担当変更、期限、判断待ち |

## 5. 主要な関係

- **Sprint → SprintBacklogItem**：スプリントで計画されたバックログ項目
- **SprintBacklogItem → Task**：バックログ項目を分解した作業
- **Task → TaskAssignment**：作業の担当者割り当て
- **Sprint → Notification**：スプリントに関連する通知

## 6. 参照エンティティ

以下のエンティティは他の業務領域で定義され、参照として使用されます。

| エンティティ | 所属業務領域 | 説明           |
|--------------|--------------|----------------|
| Team         | 共通         | 開発チーム     |
| BacklogItem  | AG-02        | バックログ項目 |
| Employee     | 共通         | 従業員         |
| Project      | 共通         | プロジェクト   |
