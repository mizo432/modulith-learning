# EF-01 帳票基盤管理 UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "システム管理者" as システム管理者

usecase "UC-EF-001" as UC_EF_001
usecase "UC-EF-004" as UC_EF_004
usecase "UC-EF-003" as UC_EF_003
usecase "UC-EF-002" as UC_EF_002

boundary "帳票定義" as FORM_DEFINITION

システム管理者 --> UC_EF_001
システム管理者 --> UC_EF_004
システム管理者 --> UC_EF_003

UC_EF_001 --> FORM_DEFINITION
UC_EF_004 --> FORM_DEFINITION
UC_EF_003 --> FORM_DEFINITION
UC_EF_002 --> FORM_DEFINITION
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-EF-001 | UC-EF-001 | システム管理者 | 帳票定義 |
| UC-EF-004 | UC-EF-004 | システム管理者 | 帳票定義 |
| UC-EF-003 | UC-EF-003 | システム管理者 | 帳票定義 |
| UC-EF-002 | UC-EF-002 | システム管理者 | 帳票定義 |
