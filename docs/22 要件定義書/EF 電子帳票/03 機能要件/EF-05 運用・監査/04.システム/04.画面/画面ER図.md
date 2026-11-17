# 画面ER図

## 概要

本ファイルは、EF-05 運用・監査の画面エンティティ間の関係をER図で定義します。

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

entity "NotificationSetting\nSCR-EF-026" as NS {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "AuditLogList\nSCR-EF-027" as ALL {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "OperationErrorList\nSCR-EF-028" as OEL {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "FormAnalyticsDashboard\nSCR-EF-029" as FAD {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "ArchiveOperationMonitor\nSCR-EF-030" as AOM {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

Screen <|-- NS
Screen <|-- ALL
Screen <|-- OEL
Screen <|-- FAD
Screen <|-- AOM

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

entity "通知設定\nSCR-EF-026" as NS {
  * 画面ID : String
  * 関連情報 : String
  --
  操作 : String
}

entity "監査ログ一覧\nSCR-EF-027" as ALL {
  * 画面ID : String
  * 関連情報 : String
  --
  操作 : String
}

entity "帳票分析ダッシュボード\nSCR-EF-029" as FAD {
  * 画面ID : String
  * 関連情報 : String
  --
  操作 : String
}

Screen <|-- NS
Screen <|-- ALL
Screen <|-- FAD

note right of Screen
  他の画面も同様に継承
end note

@enduml
```

## 画面エンティティ一覧

| 画面ID | 画面名 | 関連情報 | 主な操作 |
|--------|--------|----------|----------|
| SCR-EF-026 | 通知設定 | 通知設定 | 一覧・設定・編集 |
| SCR-EF-027 | 監査ログ一覧 | 監査ログ | 一覧・検索・詳細表示 |
| SCR-EF-028 | 運用エラー一覧 | 運用管理 | 一覧・検索・対応 |
| SCR-EF-029 | 帳票分析ダッシュボード | 帳票分析 | 統計表示・グラフ表示 |
| SCR-EF-030 | アーカイブ運用監視 | アーカイブ運用 | 状況確認・アラート表示 |

## 関係一覧

| 源エンティティ | 関係 | 対象エンティティ | 説明 |
|---------------|------|-----------------|------|
| Screen | 継承 | 通知設定 | ベース画面から継承 |
| Screen | 継承 | 監査ログ一覧 | ベース画面から継承 |
| Screen | 継承 | 帳票分析ダッシュボード | ベース画面から継承 |
