# AS-01 パーティー ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "PARTY" as PARTY {
  * party_id : INTEGER <<not null>>
  --
  名前 : VARCHAR
  タイプ : VARCHAR
  識別子 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "Party" as PARTY {
  * party_id : INTEGER <<not null>>
  --
  名前 : VARCHAR
  タイプ : VARCHAR
  識別子 : VARCHAR
}

@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| PARTY | Party | R |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
