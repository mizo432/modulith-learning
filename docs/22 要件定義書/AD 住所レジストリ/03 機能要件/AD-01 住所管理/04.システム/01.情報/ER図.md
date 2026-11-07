# AD-01 住所管理 ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "PREFECTURE" as PREFECTURE {
  * prefecture_id : INTEGER <<not null>>
  --
  都道府県名 : VARCHAR
  都道府県コード : VARCHAR
  国 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

entity "CITY" as CITY {
  * city_id : INTEGER <<not null>>
  --
  市名 : VARCHAR
  市コード : VARCHAR
  都道府県ID : INTEGER
  作成日 : VARCHAR
  更新日 : VARCHAR
}

entity "WORD" as WORD {
  * word_id : INTEGER <<not null>>
  --
  字名 : VARCHAR
  字コード : VARCHAR
  市ID : INTEGER
  作成日 : VARCHAR
  更新日 : VARCHAR
}

entity "ADDRESS" as ADDRESS {
  * address_id : INTEGER <<not null>>
  --
  国 : VARCHAR
  都道府県ID : INTEGER
  市ID : INTEGER
  字ID : INTEGER
  郵便番号 : VARCHAR
  丁目 : VARCHAR
  番地 : VARCHAR
  号 : VARCHAR
  建物名 : VARCHAR
  部屋番号 : VARCHAR
  カスタマバーコード値 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

PREFECTURE "1" --> "N" CITY
CITY "1" --> "N" WORD
PREFECTURE "1" --> "N" ADDRESS
CITY "1" --> "N" ADDRESS
WORD "1" --> "N" ADDRESS

@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "都道府県" as PREFECTURE {
  * prefecture_id : INTEGER <<not null>>
  --
  都道府県名 : VARCHAR
  都道府県コード : VARCHAR
  国 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

entity "市" as CITY {
  * city_id : INTEGER <<not null>>
  --
  市名 : VARCHAR
  市コード : VARCHAR
  都道府県ID : INTEGER
  作成日 : VARCHAR
  更新日 : VARCHAR
}

entity "字" as WORD {
  * word_id : INTEGER <<not null>>
  --
  字名 : VARCHAR
  字コード : VARCHAR
  市ID : INTEGER
  作成日 : VARCHAR
  更新日 : VARCHAR
}

entity "住所" as ADDRESS {
  * address_id : INTEGER <<not null>>
  --
  国 : VARCHAR
  都道府県ID : INTEGER
  市ID : INTEGER
  字ID : INTEGER
  郵便番号 : VARCHAR
  丁目 : VARCHAR
  番地 : VARCHAR
  号 : VARCHAR
  建物名 : VARCHAR
  部屋番号 : VARCHAR
  カスタマバーコード値 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

PREFECTURE "1" --> "N" CITY
CITY "1" --> "N" WORD
PREFECTURE "1" --> "N" ADDRESS
CITY "1" --> "N" ADDRESS
WORD "1" --> "N" ADDRESS

@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| PREFECTURE | 都道府県 | R |
| CITY | 市 | R |
| WORD | 字 | R |
| ADDRESS | 住所 | R |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
| PREFECTURE | CITY | 1対多 |
| CITY | WORD | 1対多 |
| PREFECTURE | ADDRESS | 1対多 |
| CITY | ADDRESS | 1対多 |
| WORD | ADDRESS | 1対多 |
