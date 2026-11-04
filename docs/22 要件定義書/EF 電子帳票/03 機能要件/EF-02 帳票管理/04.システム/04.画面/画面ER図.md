# 画面ER図

## 概要

本ファイルは、EF-02 帳票管理の画面エンティティ間の関係をER図で定義します。

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

entity "FormDefinitionList\nSCR-EF-009" as FDL {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "FormDefinitionEdit\nSCR-EF-010" as FDE {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "FormTemplateManagement\nSCR-EF-011" as FTM {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "FormGenerationSetting\nSCR-EF-012" as FGS {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "FormInfoList\nSCR-EF-013" as FIL {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "FormVersionManagement\nSCR-EF-014" as FVM {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

Screen <|-- FDL
Screen <|-- FDE
Screen <|-- FTM
Screen <|-- FGS
Screen <|-- FIL
Screen <|-- FVM

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

entity "帳票定義一覧\nSCR-EF-009" as FDL {
  * 画面ID : String
  * 関連情報 : String
  --
  操作 : String
}

entity "帳票定義登録・編集\nSCR-EF-010" as FDE {
  * 画面ID : String
  * 関連情報 : String
  --
  操作 : String
}

entity "帳票情報一覧\nSCR-EF-013" as FIL {
  * 画面ID : String
  * 関連情報 : String
  --
  操作 : String
}

Screen <|-- FDL
Screen <|-- FDE
Screen <|-- FIL

note right of Screen
  他の画面も同様に継承
end note

@enduml
```

## 画面エンティティ一覧

| 画面ID | 画面名 | 関連情報 | 主な操作 |
|--------|--------|----------|----------|
| SCR-EF-009 | 帳票定義一覧 | 帳票定義 | 一覧・検索・登録・編集 |
| SCR-EF-010 | 帳票定義登録・編集 | 帳票定義 | 登録・編集・公開 |
| SCR-EF-011 | 帳票テンプレート管理 | 帳票テンプレート | 一覧・アップロード・無効化 |
| SCR-EF-012 | 帳票生成設定 | 帳票生成設定 | 設定・編集 |
| SCR-EF-013 | 帳票情報一覧 | 帳票情報 | 一覧・検索・プレビュー |
| SCR-EF-014 | 帳票バージョン管理 | 帳票バージョン | 一覧・差分確認 |

## 関係一覧

| 源エンティティ | 関係 | 対象エンティティ | 説明 |
|---------------|------|-----------------|------|
| Screen | 継承 | 帳票定義一覧 | ベース画面から継承 |
| Screen | 継承 | 帳票定義登録・編集 | ベース画面から継承 |
| Screen | 継承 | 帳票情報一覧 | ベース画面から継承 |
