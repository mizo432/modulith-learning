# AD-01 住所管理 ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "ADDRESS" as ADDRESS {
  * address_id : INTEGER <<not null>>
  --
  国 : VARCHAR
  都道府県 : VARCHAR
  市区町村 : VARCHAR
  番地 : VARCHAR
  建物 : VARCHAR
  郵便番号 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "住所" as ADDRESS {
  * address_id : INTEGER <<not null>>
  --
  国 : VARCHAR
  都道府県 : VARCHAR
  市区町村 : VARCHAR
}

@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| ADDRESS | 住所 | R |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
