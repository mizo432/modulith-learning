# EF-04 保管・アーカイブ ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "FORM_STORAGE" as FORM_STORAGE {
  * form_storage_id : INTEGER <<not null>>
  --
  保存期間 : VARCHAR
  廃棄候補 : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
}

@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "帳票保管" as FORM_STORAGE {
  * form_storage_id : INTEGER <<not null>>
  --
  保存期間 : VARCHAR
  廃棄候補 : VARCHAR
  状態 : VARCHAR
}

@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| FORM_STORAGE | 帳票保管 | E |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
