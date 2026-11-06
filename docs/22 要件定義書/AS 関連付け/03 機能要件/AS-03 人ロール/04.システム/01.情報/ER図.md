# AS-03 人ロール ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "PERSON_ROLE" as PERSON_ROLE {
  * person_role_id : INTEGER <<not null>>
  --
  Party : VARCHAR
  Role : VARCHAR
  組織 : VARCHAR
  有効期間 : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
}

@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "PersonRole" as PERSON_ROLE {
  * person_role_id : INTEGER <<not null>>
  --
  Party : VARCHAR
  Role : VARCHAR
  組織 : VARCHAR
}

@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| PERSON_ROLE | PersonRole | R |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
