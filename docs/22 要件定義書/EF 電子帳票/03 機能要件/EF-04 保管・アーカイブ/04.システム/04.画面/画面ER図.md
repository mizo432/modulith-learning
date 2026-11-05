# 画面ER図

## 概要

本ファイルは、EF-04 保管・アーカイブの画面エンティティ間の関係をER図で定義します。

## ER図（英語版）

```plantuml
@startuml
skinparam linetype ortho

entity "Screen\nBase" as Screen {
  * screen_id : String
  * screen_name : String
  * primary_user : String
  * purpose : String
  --
  module : String
}

entity "RetentionPeriodManagement\nSCR-EF-021" as RPM {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "ArchivePolicyManagement\nSCR-EF-022" as APM {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "ArchiveTargetList\nSCR-EF-023" as ATL {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "ArchiveProcessStatus\nSCR-EF-024" as APS {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "RestoreRequestList\nSCR-EF-025" as RRL {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

Screen <|-- RPM
Screen <|-- APM
Screen <|-- ATL
Screen <|-- APS
Screen <|-- RRL

@enduml
```

## ER図（日本語版）

```plantuml
@startuml
skinparam linetype ortho

entity "画面\nベース" as Screen {
  * 画面ID : String
  * 画面名 : String
  * 主な利用者 : String
  * 画面の目的 : String
  --
  モジュール : String
}

entity "保存期間管理\nSCR-EF-021" as RPM {
  * 画面ID : String
  * 関連情報 : String
  --
  操作 : String
}

entity "アーカイブポリシー管理\nSCR-EF-022" as APM {
  * 画面ID : String
  * 関連情報 : String
  --
  操作 : String
}

entity "復元要求一覧\nSCR-EF-025" as RRL {
  * 画面ID : String
  * 関連情報 : String
  --
  操作 : String
}

Screen <|-- RPM
Screen <|-- APM
Screen <|-- RRL

note right of Screen
  他の画面も同様に継承
end note

@enduml
```

## 画面エンティティ一覧

| 画面ID | 画面名 | 関連情報 | 主な操作 |
|--------|--------|----------|----------|
| SCR-EF-021 | 保存期間管理 | 保存期間 | 一覧・設定・編集 |
| SCR-EF-022 | アーカイブポリシー管理 | アーカイブポリシー | 一覧・設定・編集 |
| SCR-EF-023 | アーカイブ対象一覧 | アーカイブ対象 | 一覧・検索 |
| SCR-EF-024 | アーカイブ処理状況 | アーカイブ処理 | 状況確認・再試行 |
| SCR-EF-025 | 復元要求一覧 | 帳票自動復元 | 一覧・検索・復元要求 |

## 関係一覧

| 源エンティティ | 関係 | 対象エンティティ | 説明 |
|---------------|------|-----------------|------|
| Screen | 継承 | 保存期間管理 | ベース画面から継承 |
| Screen | 継承 | アーカイブポリシー管理 | ベース画面から継承 |
| Screen | 継承 | 復元要求一覧 | ベース画面から継承 |
