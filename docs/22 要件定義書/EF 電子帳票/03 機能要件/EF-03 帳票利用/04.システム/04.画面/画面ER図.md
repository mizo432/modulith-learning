# 画面ER図

## 概要

本ファイルは、EF-03 帳票利用の画面エンティティ間の関係をER図で定義します。

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

entity "Dashboard\nSCR-EF-015" as DB {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "FormList\nSCR-EF-016" as FL {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "FormDetailPreview\nSCR-EF-017" as FDP {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "FormSearch\nSCR-EF-018" as FS {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "ApprovalPendingList\nSCR-EF-019" as APL {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

entity "ApprovalDetail\nSCR-EF-020" as AD {
  * screen_id : String
  * related_info : String
  --
  actions : String
}

Screen <|-- DB
Screen <|-- FL
Screen <|-- FDP
Screen <|-- FS
Screen <|-- APL
Screen <|-- AD

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

entity "ダッシュボード\nSCR-EF-015" as DB {
  * 画面ID : String
  * 関連情報 : String
  --
  操作 : String
}

entity "帳票一覧\nSCR-EF-016" as FL {
  * 画面ID : String
  * 関連情報 : String
  --
  操作 : String
}

entity "承認待ち一覧\nSCR-EF-019" as APL {
  * 画面ID : String
  * 関連情報 : String
  --
  操作 : String
}

Screen <|-- DB
Screen <|-- FL
Screen <|-- APL

note right of Screen
  他の画面も同様に継承
end note

@enduml
```

## 画面エンティティ一覧

| 画面ID | 画面名 | 関連情報 | 主な操作 |
|--------|--------|----------|----------|
| SCR-EF-015 | ダッシュボード | 帳票配信, 帳票分析 | 概要確認・統計表示 |
| SCR-EF-016 | 帳票一覧 | 帳票情報 | 一覧・検索・プレビュー |
| SCR-EF-017 | 帳票詳細・プレビュー | 帳票情報, 帳票プレビュー | 詳細表示・プレビュー |
| SCR-EF-018 | 帳票検索 | 帳票検索 | 検索条件入力・結果表示 |
| SCR-EF-019 | 承認待ち一覧 | 承認・確認 | 一覧・承認・却下 |
| SCR-EF-020 | 承認詳細 | 承認・確認, 帳票情報 | 詳細表示・承認・却下 |

## 関係一覧

| 源エンティティ | 関係 | 対象エンティティ | 説明 |
|---------------|------|-----------------|------|
| Screen | 継承 | ダッシュボード | ベース画面から継承 |
| Screen | 継承 | 帳票一覧 | ベース画面から継承 |
| Screen | 継承 | 承認待ち一覧 | ベース画面から継承 |
