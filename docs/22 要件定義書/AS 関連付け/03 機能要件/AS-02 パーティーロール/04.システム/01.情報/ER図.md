# AS-02 パーティーロール ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "PARTY_ROLE" as PARTY_ROLE {
  * party_role_id : INTEGER <<not null>>
  --
  Party : VARCHAR
  Roleタイプ : VARCHAR
  有効期間 : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
}

@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "PartyRole" as PARTY_ROLE {
  * party_role_id : INTEGER <<not null>>
  --
  Party : VARCHAR
  Roleタイプ : VARCHAR
  有効期間 : VARCHAR
}

@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| PARTY_ROLE | PartyRole | R |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
