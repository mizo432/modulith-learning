# UC複合図

帳票管理業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、EF-02 帳票管理業務領域のシステム境界レイヤーで扱うユースケース「帳票バージョンを管理する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Document Management] UC Composite Diagram - ManageDocumentVersion
left to right direction

skinparam usecase {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam actor {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam rectangle {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam arrowColor #475569
skinparam shadowing false

actor "Document Admin\n(ACT-EF-05)" as ACT05

rectangle "Frontend" {
  usecase "SCR-008 Document Definition Edit" as SCR
}

rectangle "Document Management Module" {
  usecase "UC-EF-034 ManageDocumentVersion" as UC
}

rectangle "System Layer" {
  card "INF-EF-13\n帳票バージョン" as INF
}

ACT05 -- SCR
SCR ..> UC : Call
UC -- INF : Use
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- 帳票バージョンを管理する
left to right direction

skinparam usecase {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam actor {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam rectangle {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam arrowColor #475569
skinparam shadowing false

actor "帳票管理者\n(ACT-EF-05)" as ACT05

rectangle "フロントエンド" {
  usecase "SCR-008 帳票定義登録・編集" as SCR
}

rectangle "帳票管理モジュール" {
  usecase "UC-EF-034 帳票バージョンを管理する" as UC
}

rectangle "システムレイヤー" {
  card "INF-EF-13\n帳票バージョン" as INF
}

ACT05 -- SCR
SCR ..> UC : 呼び出し
UC -- INF : 使用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名 | 英語名 | 説明 | 関連アクター |
|----------------|----------------|--------|------|--------------|
| UC-EF-034 | 帳票バージョンを管理する | ManageDocumentVersion | 帳票管理者が帳票バージョンを帳票バージョンを管理する | ACT-EF-05 |

## 5. 画面一覧

| 画面ID | 画面名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|
| SCR-008 | 帳票定義登録・編集 | Document Definition Edit | 画面の説明 | UC-EF-034 |

## 6. イベント一覧

| イベントID | イベント名 | 英語名 | 説明 | 関連ユースケース |
|------------|------------|--------|------|------------------|
| EVT-EF-034 | 帳票バージョンが管理された | Document Version Managed | 帳票バージョンが帳票バージョンを管理する | UC-EF-034 |

## 7. 情報一覧

| 情報ID | 情報名 | 英語名 | 情報種類 | 説明 | 関連ユースケース |
|--------|--------|--------|----------|------|------------------|
| INF-EF-13 | 帳票バージョン | 帳票バージョン | リソース | 帳票バージョンの情報 | UC-EF-034 |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
