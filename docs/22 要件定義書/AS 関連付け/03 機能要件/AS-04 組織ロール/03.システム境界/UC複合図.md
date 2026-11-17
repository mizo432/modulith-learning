# AS-04 組織ロール UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "システム管理者" as システム管理者

usecase "UC-AS-013" as UC_AS_013
usecase "UC-AS-015" as UC_AS_015
usecase "UC-AS-016" as UC_AS_016
usecase "UC-AS-014" as UC_AS_014

boundary "OrganizationRole" as ORGANIZATION_ROLE

システム管理者 --> UC_AS_013
システム管理者 --> UC_AS_015
システム管理者 --> UC_AS_016

UC_AS_013 --> ORGANIZATION_ROLE
UC_AS_015 --> ORGANIZATION_ROLE
UC_AS_016 --> ORGANIZATION_ROLE
UC_AS_014 --> ORGANIZATION_ROLE
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-AS-013 | UC-AS-013 | システム管理者 | OrganizationRole |
| UC-AS-015 | UC-AS-015 | システム管理者 | OrganizationRole |
| UC-AS-016 | UC-AS-016 | システム管理者 | OrganizationRole |
| UC-AS-014 | UC-AS-014 | システム管理者 | OrganizationRole |
