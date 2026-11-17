# AS-03 人ロール UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "システム管理者" as システム管理者

usecase "UC-AS-011" as UC_AS_011
usecase "UC-AS-009" as UC_AS_009
usecase "UC-AS-010" as UC_AS_010
usecase "UC-AS-012" as UC_AS_012

boundary "PersonRole" as PERSON_ROLE

システム管理者 --> UC_AS_011
システム管理者 --> UC_AS_009
システム管理者 --> UC_AS_010

UC_AS_011 --> PERSON_ROLE
UC_AS_009 --> PERSON_ROLE
UC_AS_010 --> PERSON_ROLE
UC_AS_012 --> PERSON_ROLE
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-AS-011 | UC-AS-011 | システム管理者 | PersonRole |
| UC-AS-009 | UC-AS-009 | システム管理者 | PersonRole |
| UC-AS-010 | UC-AS-010 | システム管理者 | PersonRole |
| UC-AS-012 | UC-AS-012 | システム管理者 | PersonRole |
