# UC複合図

帳票基盤管理業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、EF-01 帳票基盤管理業務領域のシステム境界レイヤーで扱うユースケース「ストレージ接続を更新する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Foundation Management] UC Composite Diagram - UpdateStorageConnection
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

actor "System Admin\n(ACT-EF-01)" as ACT01

rectangle "Frontend" {
  usecase "SCR-009 Organization & User Mgmt" as SCR
}

rectangle "Foundation Management Module" {
  usecase "UC-EF-008 UpdateStorageConnection" as UC
}

rectangle "System Layer" {
  card "INF-EF-03\nストレージ接続" as INF
}

ACT01 -- SCR
SCR ..> UC : Call
UC -- INF : Use
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- ストレージ接続を更新する
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

actor "システム管理者\n(ACT-EF-01)" as ACT01

rectangle "フロントエンド" {
  usecase "SCR-009 組織・利用者管理" as SCR
}

rectangle "帳票基盤管理モジュール" {
  usecase "UC-EF-008 ストレージ接続を更新する" as UC
}

rectangle "システムレイヤー" {
  card "INF-EF-03\nストレージ接続" as INF
}

ACT01 -- SCR
SCR ..> UC : 呼び出し
UC -- INF : 使用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名 | 英語名 | 説明 | 関連アクター |
|----------------|----------------|--------|------|--------------|
| UC-EF-008 | ストレージ接続を更新する | UpdateStorageConnection | システム管理者がストレージ接続をストレージ接続を更新する | ACT-EF-01 |

## 5. 画面一覧

| 画面ID | 画面名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|
| SCR-009 | 組織・利用者管理 | Organization & User Mgmt | 画面の説明 | UC-EF-008 |

## 6. イベント一覧

| イベントID | イベント名 | 英語名 | 説明 | 関連ユースケース |
|------------|------------|--------|------|------------------|
| EVT-EF-006 | ストレージ接続が更新された | Storage Connection Updated | ストレージ接続がストレージ接続を更新する | UC-EF-008 |

## 7. 情報一覧

| 情報ID | 情報名 | 英語名 | 情報種類 | 説明 | 関連ユースケース |
|--------|--------|--------|----------|------|------------------|
| INF-EF-03 | ストレージ接続 | ストレージ接続 | リソース | ストレージ接続の情報 | UC-EF-008 |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
