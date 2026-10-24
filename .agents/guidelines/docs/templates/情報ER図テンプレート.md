# 情報ER図

[業務領域の説明]業務領域で扱う情報の関係を示すER図です。

## 1. 目的

本書は、AG-[コード] [業務領域名]業務領域のシステムレイヤーで扱う情報エンティティ間の関係を示すER図です。

## 2. 日本語版 ER図

```plantuml
@startuml
title AG-[コード] [業務領域名] 情報ER図（日本語版）
hide circle
skinparam linetype ortho
skinparam shadowing false
skinparam entity {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam packageStyle rectangle

entity "エンティティ名" as EntityName {
  * entity_name_id : SnowflakeID <<PK>>
  --
  related_id : UUID <<FK>>
}

' イベントエンティティは薄ピンク色で表現
entity "イベント名" as EventName #FFB6C1 {
  * event_id : SnowflakeID <<PK>>
  --
  actor_id : UUID <<FK>>
}

' ロールエンティティは薄い黄色で表現
entity "ロール名" as RoleName #FFFFE0 {
  * role_id : SnowflakeID <<PK>>
  --
  employee_id : UUID <<FK>>
}

' コメント: 関係の説明
エンティティA ||--o{ エンティティB : 関係

@enduml
```

## 3. 情報ER図

```plantuml
@startuml
title AG-[コード] [業務領域名] 情報ER図
hide circle
skinparam linetype ortho
skinparam shadowing false
skinparam entity {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam packageStyle rectangle

entity "EntityName" as EntityName {
  * entity_name_id : SnowflakeID <<PK>>
  --
  related_id : UUID <<FK>>
}

' イベントエンティティは薄ピンク色で表現
entity "EventName" as EventName #FFB6C1 {
  * event_id : SnowflakeID <<PK>>
  --
  actor_id : UUID <<FK>>
}

' ロールエンティティは薄い黄色で表現
entity "RoleName" as RoleName #FFFFE0 {
  * role_id : SnowflakeID <<PK>>
  --
  employee_id : UUID <<FK>>
}

' コメント: 関係の説明
EntityA ||--o{ EntityB : relationship

@enduml
```

## 4. エンティティ一覧

| エンティティ | 情報名 (ID)        | 説明 |
|--------------|--------------------|------|
| EntityName   | 情報名 (INF-XX-01) | 説明 |

## 5. 主要な関係

- **EntityA → EntityB**：関係の説明

## 6. 参照エンティティ

以下のエンティティは他の業務領域で定義され、参照として使用されます。

| エンティティ | 所属業務領域  | 説明 |
|--------------|---------------|------|
| EntityName   | 共通 / AG-XXX | 説明 |
