# EF-03 帳票利用 UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "帳票利用者" as 帳票利用者

usecase "UC-EF-012" as UC_EF_012
usecase "UC-EF-011" as UC_EF_011
usecase "UC-EF-009" as UC_EF_009
usecase "UC-EF-010" as UC_EF_010

boundary "帳票配信" as FORM_DELIVERY

帳票利用者 --> UC_EF_012
帳票利用者 --> UC_EF_011
帳票利用者 --> UC_EF_009

UC_EF_012 --> FORM_DELIVERY
UC_EF_011 --> FORM_DELIVERY
UC_EF_009 --> FORM_DELIVERY
UC_EF_010 --> FORM_DELIVERY
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-EF-012 | UC-EF-012 | 帳票利用者 | 帳票配信 |
| UC-EF-011 | UC-EF-011 | 帳票利用者 | 帳票配信 |
| UC-EF-009 | UC-EF-009 | 帳票利用者 | 帳票配信 |
| UC-EF-010 | UC-EF-010 | 帳票利用者 | 帳票配信 |
