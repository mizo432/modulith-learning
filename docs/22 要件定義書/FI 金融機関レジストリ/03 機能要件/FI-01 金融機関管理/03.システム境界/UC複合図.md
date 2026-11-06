# FI-01 金融機関管理 UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "システム管理者" as システム管理者

usecase "UC-FI-004" as UC_FI_004
usecase "UC-FI-002" as UC_FI_002
usecase "UC-FI-001" as UC_FI_001
usecase "UC-FI-003" as UC_FI_003

boundary "金融機関" as FINANCIAL_INSTITUTION

システム管理者 --> UC_FI_004
システム管理者 --> UC_FI_002
システム管理者 --> UC_FI_001

UC_FI_004 --> FINANCIAL_INSTITUTION
UC_FI_002 --> FINANCIAL_INSTITUTION
UC_FI_001 --> FINANCIAL_INSTITUTION
UC_FI_003 --> FINANCIAL_INSTITUTION
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-FI-004 | UC-FI-004 | システム管理者 | 金融機関 |
| UC-FI-002 | UC-FI-002 | システム管理者 | 金融機関 |
| UC-FI-001 | UC-FI-001 | システム管理者 | 金融機関 |
| UC-FI-003 | UC-FI-003 | システム管理者 | 金融機関 |
