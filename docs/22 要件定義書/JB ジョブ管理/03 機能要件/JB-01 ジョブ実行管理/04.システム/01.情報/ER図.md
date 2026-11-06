# JB-01 ジョブ実行管理 ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "JOB_DEFINITION" as JOB_DEFINITION {
  * job_def_id : INTEGER <<not null>>
  --
  コード : VARCHAR
  パラメータ : VARCHAR
  実行条件 : VARCHAR
  バージョン : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

entity "JOB_EXECUTION" as JOB_EXECUTION {
  * job_exec_id : INTEGER <<not null>>
  --
  状態 : VARCHAR
  開始時刻 : VARCHAR
  終了時刻 : VARCHAR
  結果 : VARCHAR
  エラー : VARCHAR
  作成日 : VARCHAR
}

JOB_DEFINITION ||--o{ JOB_EXECUTION
@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "ジョブ定義" as JOB_DEFINITION {
  * job_def_id : INTEGER <<not null>>
  --
  コード : VARCHAR
  パラメータ : VARCHAR
  実行条件 : VARCHAR
}

entity "ジョブ実行" as JOB_EXECUTION {
  * job_exec_id : INTEGER <<not null>>
  --
  状態 : VARCHAR
  開始時刻 : VARCHAR
  終了時刻 : VARCHAR
}

JOB_DEFINITION ||--o{ JOB_EXECUTION
@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| JOB_DEFINITION | ジョブ定義 | R |
| JOB_EXECUTION | ジョブ実行 | E |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
| JOB_DEFINITION | JOB_EXECUTION | 1対多 |
