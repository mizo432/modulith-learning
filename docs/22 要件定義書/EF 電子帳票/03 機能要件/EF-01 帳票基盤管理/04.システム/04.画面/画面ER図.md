# 画面ER図

## 概要

本ファイルは、EF-01 帳票基盤管理の画面エンティティ間の関係をER図で定義します。

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

entity "SystemEnvironmentList\nSCR-EF-001" as SEL {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "BusinessSystemList\nSCR-EF-002" as BSL {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "StorageConnectionSetting\nSCR-EF-003" as SCS {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "AccessControlPolicyList\nSCR-EF-004" as ACPL {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "OrganizationUserManagement\nSCR-EF-005" as OUM {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "RoleManagement\nSCR-EF-006" as RM {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "ExternalApiKeyManagement\nSCR-EF-007" as EAKM {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "SystemSetting\nSCR-EF-008" as SS {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

Screen <|-- SEL
Screen <|-- BSL
Screen <|-- SCS
Screen <|-- ACPL
Screen <|-- OUM
Screen <|-- RM
Screen <|-- EAKM
Screen <|-- SS

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

entity "システム環境一覧\nSCR-EF-001" as SEL {
  * 画面ID : String
  * 関連情報 : String
  --
  操作 : String
}

entity "組織・利用者管理\nSCR-EF-005" as OUM {
  * 画面ID : String
  * 関連情報 : String
  --
  操作 : String
}

entity "システム設定\nSCR-EF-008" as SS {
  * 画面ID : String
  * 関連情報 : String
  --
  操作 : String
}

Screen <|-- SEL
Screen <|-- OUM
Screen <|-- SS

note right of Screen
  他の画面も同様に継承
end note

@enduml
```

## 画面エンティティ一覧

| 画面ID | 画面名 | 関連情報 | 主な操作 |
|--------|--------|----------|----------|
| SCR-EF-001 | システム環境一覧 | システム利用環境 | 一覧・検索・編集 |
| SCR-EF-002 | 業務システム一覧 | 業務システム | 一覧・検索・登録・編集 |
| SCR-EF-003 | ストレージ接続設定 | ストレージ接続 | 一覧・設定・テスト |
| SCR-EF-004 | アクセス制御ポリシー一覧 | アクセス制御ポリシー | 一覧・検索・登録・編集 |
| SCR-EF-005 | 組織・利用者管理 | 組織, 利用者 | 一覧・検索・登録・編集 |
| SCR-EF-006 | ロール管理 | ロール | 一覧・検索・登録・編集 |
| SCR-EF-007 | 外部APIキー管理 | 外部APIキー | 一覧・検索・発行・無効化 |
| SCR-EF-008 | システム設定 | システム設定 | 一覧・編集 |

## 関係一覧

| 源エンティティ | 関係 | 対象エンティティ | 説明 |
|---------------|------|-----------------|------|
| Screen | 継承 | システム環境一覧 | ベース画面から継承 |
| Screen | 継承 | 組織・利用者管理 | ベース画面から継承 |
| Screen | 継承 | システム設定 | ベース画面から継承 |
