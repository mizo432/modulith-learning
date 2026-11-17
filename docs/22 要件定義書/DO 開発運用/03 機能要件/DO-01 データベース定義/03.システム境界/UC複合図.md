# DO-01 データベース定義 UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "システム管理者" as システム管理者

usecase "UC-DO-001" as UC_DO_001
usecase "UC-DO-002" as UC_DO_002
usecase "UC-DO-004" as UC_DO_004
usecase "UC-DO-003" as UC_DO_003

boundary "データベース" as DATABASE
boundary "スキーマ" as SCHEMA
boundary "テーブル" as TABLE

システム管理者 --> UC_DO_001
システム管理者 --> UC_DO_002
システム管理者 --> UC_DO_004

UC_DO_001 --> DATABASE
UC_DO_001 --> SCHEMA
UC_DO_002 --> DATABASE
UC_DO_002 --> SCHEMA
UC_DO_004 --> DATABASE
UC_DO_004 --> SCHEMA
UC_DO_003 --> DATABASE
UC_DO_003 --> SCHEMA
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-DO-001 | UC-DO-001 | システム管理者 | データベース;スキーマ |
| UC-DO-002 | UC-DO-002 | システム管理者 | データベース;スキーマ |
| UC-DO-004 | UC-DO-004 | システム管理者 | データベース;スキーマ |
| UC-DO-003 | UC-DO-003 | システム管理者 | データベース;スキーマ |
