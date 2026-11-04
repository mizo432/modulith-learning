# UC複合図

帳票管理業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、EF-02 帳票管理業務領域のシステム境界レイヤーで扱うユースケース「帳票を生成する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Document Management] UC Composite Diagram - GenerateDocument
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
  usecase "UC-EF-027 GenerateDocument" as UC
}

rectangle "System Layer" {
  card "INF-EF-11\n帳票ファイル" as INF
}

ACT05 -- SCR
SCR ..> UC : Call
UC -- INF : Use
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- 帳票を生成する
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
  usecase "UC-EF-027 帳票を生成する" as UC
}

rectangle "システムレイヤー" {
  card "INF-EF-11\n帳票ファイル" as INF
}

ACT05 -- SCR
SCR ..> UC : 呼び出し
UC -- INF : 使用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名 | 英語名 | 説明 | 関連アクター |
|----------------|----------------|--------|------|--------------|
| UC-EF-027 | 帳票を生成する | GenerateDocument | 帳票管理者が帳票ファイルを帳票を生成する | ACT-EF-03 |

## 5. 画面一覧

| 画面ID | 画面名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|
| SCR-008 | 帳票定義登録・編集 | Document Definition Edit | 画面の説明 | UC-EF-027 |

## 6. イベント一覧

| イベントID | イベント名 | 英語名 | 説明 | 関連ユースケース |
|------------|------------|--------|------|------------------|
| EVT-EF-027 | 帳票が生成された | Document Generated | 帳票ファイルが帳票を生成する | UC-EF-027 |

## 7. 情報一覧

| 情報ID | 情報名 | 英語名 | 情報種類 | 説明 | 関連ユースケース |
|--------|--------|--------|----------|------|------------------|
| INF-EF-11 | 帳票ファイル | 帳票ファイル | リソース | 帳票ファイルの情報 | UC-EF-027 |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
