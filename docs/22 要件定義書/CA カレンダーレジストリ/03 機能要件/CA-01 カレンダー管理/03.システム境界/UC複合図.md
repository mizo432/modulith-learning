# CA-01 カレンダー管理 UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "システム管理者" as システム管理者

usecase "UC-CA-001" as UC_CA_001
usecase "UC-CA-003" as UC_CA_003
usecase "UC-CA-004" as UC_CA_004
usecase "UC-CA-002" as UC_CA_002

boundary "カレンダー" as CALENDAR
boundary "祝日" as HOLIDAY

システム管理者 --> UC_CA_001
システム管理者 --> UC_CA_003
システム管理者 --> UC_CA_004

UC_CA_001 --> CALENDAR
UC_CA_001 --> HOLIDAY
UC_CA_003 --> CALENDAR
UC_CA_003 --> HOLIDAY
UC_CA_004 --> CALENDAR
UC_CA_004 --> HOLIDAY
UC_CA_002 --> CALENDAR
UC_CA_002 --> HOLIDAY
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-CA-001 | UC-CA-001 | システム管理者 | カレンダー;祝日 |
| UC-CA-003 | UC-CA-003 | システム管理者 | カレンダー;祝日 |
| UC-CA-004 | UC-CA-004 | システム管理者 | カレンダー;祝日 |
| UC-CA-002 | UC-CA-002 | システム管理者 | カレンダー;祝日 |
