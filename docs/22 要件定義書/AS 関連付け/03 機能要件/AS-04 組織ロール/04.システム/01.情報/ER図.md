# AS-04 組織ロール ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "ORGANIZATION_ROLE" as ORGANIZATION_ROLE {
  * org_role_id : INTEGER <<not null>>
  --
  Party : VARCHAR
  Role : VARCHAR
  有効期間 : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
}

@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "OrganizationRole" as ORGANIZATION_ROLE {
  * org_role_id : INTEGER <<not null>>
  --
  Party : VARCHAR
  Role : VARCHAR
  有効期間 : VARCHAR
}

@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| ORGANIZATION_ROLE | OrganizationRole | R |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
