# AS-01 パーティー UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "システム管理者" as システム管理者

usecase "UC-AS-001" as UC_AS_001
usecase "UC-AS-002" as UC_AS_002
usecase "UC-AS-004" as UC_AS_004
usecase "UC-AS-003" as UC_AS_003

boundary "Party" as PARTY

システム管理者 --> UC_AS_001
システム管理者 --> UC_AS_002
システム管理者 --> UC_AS_004

UC_AS_001 --> PARTY
UC_AS_002 --> PARTY
UC_AS_004 --> PARTY
UC_AS_003 --> PARTY
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-AS-001 | UC-AS-001 | システム管理者 | Party |
| UC-AS-002 | UC-AS-002 | システム管理者 | Party |
| UC-AS-004 | UC-AS-004 | システム管理者 | Party |
| UC-AS-003 | UC-AS-003 | システム管理者 | Party |
