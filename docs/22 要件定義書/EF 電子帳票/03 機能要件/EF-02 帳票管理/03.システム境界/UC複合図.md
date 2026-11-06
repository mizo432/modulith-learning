# EF-02 帳票管理 UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "帳票利用者" as 帳票利用者

usecase "UC-EF-008" as UC_EF_008
usecase "UC-EF-007" as UC_EF_007
usecase "UC-EF-005" as UC_EF_005
usecase "UC-EF-006" as UC_EF_006

boundary "帳票" as FORM

帳票利用者 --> UC_EF_008
帳票利用者 --> UC_EF_007
帳票利用者 --> UC_EF_005

UC_EF_008 --> FORM
UC_EF_007 --> FORM
UC_EF_005 --> FORM
UC_EF_006 --> FORM
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-EF-008 | UC-EF-008 | 帳票利用者 | 帳票 |
| UC-EF-007 | UC-EF-007 | 帳票利用者 | 帳票 |
| UC-EF-005 | UC-EF-005 | 帳票利用者 | 帳票 |
| UC-EF-006 | UC-EF-006 | 帳票利用者 | 帳票 |
