# UC複合図

帳票利用業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、EF-03 帳票利用業務領域のシステム境界レイヤーで扱うユースケース「帳票をプレビューする」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Document Usage] UC Composite Diagram - PreviewDocument
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

actor "Document User\n(ACT-EF-06)" as ACT06

rectangle "Frontend" {
  usecase "SCR-003 Document Detail & Preview" as SCR
}

rectangle "Document Usage Module" {
  usecase "UC-EF-038 PreviewDocument" as UC
}

rectangle "System Layer" {
  card "INF-EF-11\n帳票ファイル" as INF
}

ACT06 -- SCR
SCR ..> UC : Call
UC -- INF : Use
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- 帳票をプレビューする
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

actor "帳票利用者\n(ACT-EF-06)" as ACT06

rectangle "フロントエンド" {
  usecase "SCR-003 帳票詳細・プレビュー" as SCR
}

rectangle "帳票利用モジュール" {
  usecase "UC-EF-038 帳票をプレビューする" as UC
}

rectangle "システムレイヤー" {
  card "INF-EF-11\n帳票ファイル" as INF
}

ACT06 -- SCR
SCR ..> UC : 呼び出し
UC -- INF : 使用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名 | 英語名 | 説明 | 関連アクター |
|----------------|----------------|--------|------|--------------|
| UC-EF-038 | 帳票をプレビューする | PreviewDocument | 帳票利用者が帳票ファイルを帳票をプレビューする | ACT-EF-06 |

## 5. 画面一覧

| 画面ID | 画面名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|
| SCR-003 | 帳票詳細・プレビュー | Document Detail & Preview | 画面の説明 | UC-EF-038 |

## 6. イベント一覧

| イベントID | イベント名 | 英語名 | 説明 | 関連ユースケース |
|------------|------------|--------|------|------------------|
| EVT-EF-038 | 帳票がプレビューされた | Document Previewed | 帳票ファイルが帳票をプレビューする | UC-EF-038 |

## 7. 情報一覧

| 情報ID | 情報名 | 英語名 | 情報種類 | 説明 | 関連ユースケース |
|--------|--------|--------|----------|------|------------------|
| INF-EF-11 | 帳票ファイル | 帳票ファイル | リソース | 帳票ファイルの情報 | UC-EF-038 |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
