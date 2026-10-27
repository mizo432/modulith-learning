# UC複合図

[業務領域の説明]業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、[事業領域コード]-[業務領域コード] [業務領域名]
業務領域のシステム境界レイヤーで扱うユースケース、画面、イベント、情報、条件を関連づけたUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
@startuml
title [BusinessDomain] UC Composite Diagram
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

actor "ActorName" as Actor

rectangle "Frontend" {
  usecase "SCR-01 ScreenName" as SCR01
}

rectangle "[Module Name]" {
  usecase "UC-01 UseCaseName" as UC01
}

note "ES-01 ExternalSystem" as ES01

rectangle "System Layer" {
  card "INF-01 InformationName" as INF01
  card "RL-01 RuleName" as RL01
}

Actor -- SCR01
SCR01 ..> UC01 : Call
UC01 ..> ES01 : Data Exchange
UC01 -- INF01 : Use
UC01 -- RL01 : Apply
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
@startuml
title [業務領域名] UC複合図（日本語版）
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

actor "アクター名" as Actor

rectangle "フロントエンド" {
  usecase "SCR-01 画面名" as SCR01
}

rectangle "[モジュール名]" {
  usecase "UC-01 ユースケース名" as UC01
}

note "ES-01 外部システム名" as ES01

rectangle "システムレイヤー" {
  card "INF-01 情報名" as INF01
  card "RL-01 ルール名" as RL01
}

Actor -- SCR01
SCR01 ..> UC01 : 呼び出し
UC01 ..> ES01 : データ交換
UC01 -- INF01 : 使用
UC01 -- RL01 : 適用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名 | 英語名      | 説明 | 関連アクター |
|----------------|----------------|-------------|------|--------------|
| UC-XX-01       | ユースケース名 | UseCaseName | 説明 | アクター名   |

## 5. 画面一覧

| 画面ID    | 画面名 | 英語名     | 説明 | 関連ユースケース |
|-----------|--------|------------|------|------------------|
| SCR-XX-01 | 画面名 | ScreenName | 説明 | UC-XX-01         |

## 6. イベント一覧

| イベントID | イベント名 | 英語名    | 説明 | 関連ユースケース |
|------------|------------|-----------|------|------------------|
| EVT-XX-01  | イベント名 | EventName | 説明 | UC-XX-01         |

## 7. 情報一覧

| 情報ID    | 情報名 | 英語名          | 説明 | 関連ユースケース |
|-----------|--------|-----------------|------|------------------|
| INF-XX-01 | 情報名 | InformationName | 説明 | UC-XX-01         |

## 8. 条件一覧

| 条件ID   | 条件名   | 英語名   | 説明 | 関連ユースケース |
|----------|----------|----------|------|------------------|
| RL-XX-01 | ルール名 | RuleName | 説明 | UC-XX-01         |

## 9. 参照情報

- ユースケース一覧: `03.システム境界/01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
