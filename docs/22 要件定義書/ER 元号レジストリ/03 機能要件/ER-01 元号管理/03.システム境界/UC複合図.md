# ER-01 元号管理 UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "システム管理者" as システム管理者

usecase "UC-ER-002" as UC_ER_002
usecase "UC-ER-001" as UC_ER_001
usecase "UC-ER-003" as UC_ER_003
usecase "UC-ER-004" as UC_ER_004

boundary "元号" as ERA

システム管理者 --> UC_ER_002
システム管理者 --> UC_ER_001
システム管理者 --> UC_ER_003

UC_ER_002 --> ERA
UC_ER_001 --> ERA
UC_ER_003 --> ERA
UC_ER_004 --> ERA
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-ER-002 | UC-ER-002 | システム管理者 | 元号 |
| UC-ER-001 | UC-ER-001 | システム管理者 | 元号 |
| UC-ER-003 | UC-ER-003 | システム管理者 | 元号 |
| UC-ER-004 | UC-ER-004 | システム管理者 | 元号 |
