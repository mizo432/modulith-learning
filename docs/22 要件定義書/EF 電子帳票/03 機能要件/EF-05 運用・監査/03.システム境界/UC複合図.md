# EF-05 運用・監査 UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "システム管理者" as システム管理者

usecase "UC-EF-020" as UC_EF_020
usecase "UC-EF-019" as UC_EF_019
usecase "UC-EF-018" as UC_EF_018
usecase "UC-EF-017" as UC_EF_017

boundary "監査ログ" as AUDIT_LOG

システム管理者 --> UC_EF_020
システム管理者 --> UC_EF_019
システム管理者 --> UC_EF_018

UC_EF_020 --> AUDIT_LOG
UC_EF_019 --> AUDIT_LOG
UC_EF_018 --> AUDIT_LOG
UC_EF_017 --> AUDIT_LOG
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-EF-020 | UC-EF-020 | システム管理者 | 監査ログ |
| UC-EF-019 | UC-EF-019 | システム管理者 | 監査ログ |
| UC-EF-018 | UC-EF-018 | システム管理者 | 監査ログ |
| UC-EF-017 | UC-EF-017 | システム管理者 | 監査ログ |
