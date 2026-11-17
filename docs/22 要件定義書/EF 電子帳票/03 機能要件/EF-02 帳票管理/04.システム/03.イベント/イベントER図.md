# イベントER図

## 概要

本ファイルは、EF-02 帳票管理のイベントエンティティ間の関係をER図で定義します。

## ER図（英語版）

```plantuml
@startuml
skinparam linetype ortho

entity "DomainEvent\nBase" as DomainEvent {
  * event_id : String
  * event_type : String
  * occurred_at : LocalDateTime
  * aggregate_id : String
  * aggregate_type : String
  --
  payload : String
}

entity "FormDefinitionRegistered\nEVT-EF-16" as FDR {
  * event_id : String
  * form_def_id : String
  * form_type : String
  * form_name : String
  --
  occurred_at : LocalDateTime
}

entity "FormDefinitionPublished\nEVT-EF-18" as FDP {
  * event_id : String
  * form_def_id : String
  --
  occurred_at : LocalDateTime
}

entity "FormTemplateUploaded\nEVT-EF-19" as FTU {
  * event_id : String
  * template_id : String
  * form_def_id : String
  * file_format : String
  --
  occurred_at : LocalDateTime
}

entity "FormGenerated\nEVT-EF-21" as FG {
  * event_id : String
  * form_id : String
  * form_def_id : String
  * system_id : String
  --
  occurred_at : LocalDateTime
}

entity "FormVersionCreated\nEVT-EF-23" as FVC {
  * event_id : String
  * version_id : String
  * form_id : String
  * version_number : Integer
  --
  occurred_at : LocalDateTime
}

DomainEvent <|-- FDR
DomainEvent <|-- FDP
DomainEvent <|-- FTU
DomainEvent <|-- FG
DomainEvent <|-- FVC

@enduml
```

## ER図（日本語版）

```plantuml
@startuml
skinparam linetype ortho

entity "ドメインイベント\nベース" as DomainEvent {
  * イベントID : String
  * イベント種類 : String
  * 発生日時 : LocalDateTime
  * 集約ID : String
  * 集約種類 : String
  --
  ペイロード : String
}

entity "帳票定義が登録された\nEVT-EF-16" as FDR {
  * イベントID : String
  * 帳票定義ID : String
  * 帳票種類 : String
  * 帳票名 : String
  --
  発生日時 : LocalDateTime
}

entity "帳票が生成された\nEVT-EF-21" as FG {
  * イベントID : String
  * 帳票ID : String
  * 帳票定義ID : String
  * システムID : String
  --
  発生日時 : LocalDateTime
}

entity "帳票バージョンが作成された\nEVT-EF-23" as FVC {
  * イベントID : String
  * バージョンID : String
  * 帳票ID : String
  * バージョン番号 : Integer
  --
  発生日時 : LocalDateTime
}

DomainEvent <|-- FDR
DomainEvent <|-- FG
DomainEvent <|-- FVC

note right of DomainEvent
  他のイベントも同様に継承
end note

@enduml
```

## イベントエンティティ一覧

| イベントID | イベント名 | 集約 | 主要属性 |
|------------|------------|------|----------|
| EVT-EF-16 | 帳票定義が登録された | FormDefinition | form_def_id, form_type, form_name |
| EVT-EF-17 | 帳票定義が更新された | FormDefinition | form_def_id, changed_fields |
| EVT-EF-18 | 帳票定義が公開された | FormDefinition | form_def_id |
| EVT-EF-19 | 帳票テンプレートがアップロードされた | FormTemplate | template_id, form_def_id, file_format |
| EVT-EF-20 | 帳票生成設定が変更された | FormGenerationSetting | setting_id, form_def_id |
| EVT-EF-21 | 帳票が生成された | FormInfo | form_id, form_def_id, system_id |
| EVT-EF-22 | 帳票が登録された | FormInfo | form_id, form_def_id |
| EVT-EF-23 | 帳票バージョンが作成された | FormVersion | version_id, form_id, version_number |

## 関係一覧

| 源エンティティ | 関係 | 対象エンティティ | 説明 |
|---------------|------|-----------------|------|
| DomainEvent | 継承 | 帳票定義が登録された | ベースイベントから継承 |
| DomainEvent | 継承 | 帳票が生成された | ベースイベントから継承 |
| DomainEvent | 継承 | 帳票バージョンが作成された | ベースイベントから継承 |
