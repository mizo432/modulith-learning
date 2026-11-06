# AD-01 住所管理 UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "システム管理者" as システム管理者
actor "従業員" as 従業員

usecase "UC-AD-001" as UC_AD_001
usecase "UC-AD-002" as UC_AD_002
usecase "UC-AD-004" as UC_AD_004
usecase "UC-AD-003" as UC_AD_003

boundary "住所" as ADDRESS

システム管理者 --> UC_AD_001
システム管理者 --> UC_AD_002
システム管理者 --> UC_AD_004
従業員 --> UC_AD_001
従業員 --> UC_AD_002
従業員 --> UC_AD_004

UC_AD_001 --> ADDRESS
UC_AD_002 --> ADDRESS
UC_AD_004 --> ADDRESS
UC_AD_003 --> ADDRESS
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-AD-001 | UC-AD-001 | システム管理者;従業員 | 住所 |
| UC-AD-002 | UC-AD-002 | システム管理者;従業員 | 住所 |
| UC-AD-004 | UC-AD-004 | システム管理者;従業員 | 住所 |
| UC-AD-003 | UC-AD-003 | システム管理者;従業員 | 住所 |
