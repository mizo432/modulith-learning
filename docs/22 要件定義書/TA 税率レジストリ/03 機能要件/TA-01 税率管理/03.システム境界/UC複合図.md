# TA-01 税率管理 UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "システム管理者" as システム管理者

usecase "UC-TA-001" as UC_TA_001
usecase "UC-TA-002" as UC_TA_002
usecase "UC-TA-003" as UC_TA_003
usecase "UC-TA-004" as UC_TA_004

boundary "税率" as TAX_RATE

システム管理者 --> UC_TA_001
システム管理者 --> UC_TA_002
システム管理者 --> UC_TA_003

UC_TA_001 --> TAX_RATE
UC_TA_002 --> TAX_RATE
UC_TA_003 --> TAX_RATE
UC_TA_004 --> TAX_RATE
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-TA-001 | UC-TA-001 | システム管理者 | 税率 |
| UC-TA-002 | UC-TA-002 | システム管理者 | 税率 |
| UC-TA-003 | UC-TA-003 | システム管理者 | 税率 |
| UC-TA-004 | UC-TA-004 | システム管理者 | 税率 |
