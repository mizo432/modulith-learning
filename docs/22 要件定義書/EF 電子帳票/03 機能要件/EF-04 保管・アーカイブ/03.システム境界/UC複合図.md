# EF-04 保管・アーカイブ UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "システム管理者" as システム管理者

usecase "UC-EF-013" as UC_EF_013
usecase "UC-EF-014" as UC_EF_014
usecase "UC-EF-016" as UC_EF_016
usecase "UC-EF-015" as UC_EF_015

boundary "帳票保管" as FORM_STORAGE

システム管理者 --> UC_EF_013
システム管理者 --> UC_EF_014
システム管理者 --> UC_EF_016

UC_EF_013 --> FORM_STORAGE
UC_EF_014 --> FORM_STORAGE
UC_EF_016 --> FORM_STORAGE
UC_EF_015 --> FORM_STORAGE
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-EF-013 | UC-EF-013 | システム管理者 | 帳票保管 |
| UC-EF-014 | UC-EF-014 | システム管理者 | 帳票保管 |
| UC-EF-016 | UC-EF-016 | システム管理者 | 帳票保管 |
| UC-EF-015 | UC-EF-015 | システム管理者 | 帳票保管 |
