# AS-02 パーティーロール UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "システム管理者" as システム管理者

usecase "UC-AS-007" as UC_AS_007
usecase "UC-AS-006" as UC_AS_006
usecase "UC-AS-005" as UC_AS_005
usecase "UC-AS-008" as UC_AS_008

boundary "PartyRole" as PARTY_ROLE

システム管理者 --> UC_AS_007
システム管理者 --> UC_AS_006
システム管理者 --> UC_AS_005

UC_AS_007 --> PARTY_ROLE
UC_AS_006 --> PARTY_ROLE
UC_AS_005 --> PARTY_ROLE
UC_AS_008 --> PARTY_ROLE
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-AS-007 | UC-AS-007 | システム管理者 | PartyRole |
| UC-AS-006 | UC-AS-006 | システム管理者 | PartyRole |
| UC-AS-005 | UC-AS-005 | システム管理者 | PartyRole |
| UC-AS-008 | UC-AS-008 | システム管理者 | PartyRole |
