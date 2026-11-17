# JB-01 ジョブ実行管理 UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "運用担当者" as 運用担当者
actor "システム管理者" as システム管理者

usecase "UC-JB-005" as UC_JB_005
usecase "UC-JB-004" as UC_JB_004
usecase "UC-JB-009" as UC_JB_009
usecase "UC-JB-006" as UC_JB_006
usecase "UC-JB-001" as UC_JB_001
usecase "UC-JB-007" as UC_JB_007
usecase "UC-JB-008" as UC_JB_008
usecase "UC-JB-002" as UC_JB_002
usecase "UC-JB-003" as UC_JB_003

boundary "ジョブ定義" as JOB_DEFINITION
boundary "ジョブ実行" as JOB_EXECUTION

運用担当者 --> UC_JB_005
運用担当者 --> UC_JB_004
運用担当者 --> UC_JB_009
システム管理者 --> UC_JB_005
システム管理者 --> UC_JB_004
システム管理者 --> UC_JB_009

UC_JB_005 --> JOB_DEFINITION
UC_JB_005 --> JOB_EXECUTION
UC_JB_004 --> JOB_DEFINITION
UC_JB_004 --> JOB_EXECUTION
UC_JB_009 --> JOB_DEFINITION
UC_JB_009 --> JOB_EXECUTION
UC_JB_006 --> JOB_DEFINITION
UC_JB_006 --> JOB_EXECUTION
UC_JB_001 --> JOB_DEFINITION
UC_JB_001 --> JOB_EXECUTION
UC_JB_007 --> JOB_DEFINITION
UC_JB_007 --> JOB_EXECUTION
UC_JB_008 --> JOB_DEFINITION
UC_JB_008 --> JOB_EXECUTION
UC_JB_002 --> JOB_DEFINITION
UC_JB_002 --> JOB_EXECUTION
UC_JB_003 --> JOB_DEFINITION
UC_JB_003 --> JOB_EXECUTION
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-JB-005 | UC-JB-005 | 運用担当者;システム管理者 | ジョブ定義;ジョブ実行 |
| UC-JB-004 | UC-JB-004 | 運用担当者;システム管理者 | ジョブ定義;ジョブ実行 |
| UC-JB-009 | UC-JB-009 | 運用担当者;システム管理者 | ジョブ定義;ジョブ実行 |
| UC-JB-006 | UC-JB-006 | 運用担当者;システム管理者 | ジョブ定義;ジョブ実行 |
| UC-JB-001 | UC-JB-001 | 運用担当者;システム管理者 | ジョブ定義;ジョブ実行 |
| UC-JB-007 | UC-JB-007 | 運用担当者;システム管理者 | ジョブ定義;ジョブ実行 |
| UC-JB-008 | UC-JB-008 | 運用担当者;システム管理者 | ジョブ定義;ジョブ実行 |
| UC-JB-002 | UC-JB-002 | 運用担当者;システム管理者 | ジョブ定義;ジョブ実行 |
| UC-JB-003 | UC-JB-003 | 運用担当者;システム管理者 | ジョブ定義;ジョブ実行 |
