# UC複合図

運用・監査業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、EF-05 運用・監査業務領域のシステム境界レイヤーで扱うユースケース「通知設定を参照する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Operations & Audit] UC Composite Diagram - ViewNotificationSettings
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

actor "Operations Admin\n(ACT-EF-04)" as ACT04

rectangle "Frontend" {
  usecase "SCR-015 Audit Log List" as SCR
}

rectangle "Operations & Audit Module" {
  usecase "UC-EF-068 ViewNotificationSettings" as UC
}

rectangle "System Layer" {
  card "INF-EF-22\n通知設定" as INF
}

ACT04 -- SCR
SCR ..> UC : Call
UC -- INF : Use
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- 通知設定を参照する
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

actor "運用管理者\n(ACT-EF-04)" as ACT04

rectangle "フロントエンド" {
  usecase "SCR-015 監査ログ一覧" as SCR
}

rectangle "運用・監査モジュール" {
  usecase "UC-EF-068 通知設定を参照する" as UC
}

rectangle "システムレイヤー" {
  card "INF-EF-22\n通知設定" as INF
}

ACT04 -- SCR
SCR ..> UC : 呼び出し
UC -- INF : 使用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名 | 英語名 | 説明 | 関連アクター |
|----------------|----------------|--------|------|--------------|
| UC-EF-068 | 通知設定を参照する | ViewNotificationSettings | 運用管理者が通知設定を通知設定を参照する | ACT-EF-04 |

## 5. 画面一覧

| 画面ID | 画面名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|
| SCR-015 | 監査ログ一覧 | Audit Log List | 画面の説明 | UC-EF-068 |

## 6. イベント一覧

| イベントID | イベント名 | 英語名 | 説明 | 関連ユースケース |
|------------|------------|--------|------|------------------|
| EVT-EF-068 | 通知設定が参照された | Notification Settings Viewed | 通知設定が通知設定を参照する | UC-EF-068 |

## 7. 情報一覧

| 情報ID | 情報名 | 英語名 | 情報種類 | 説明 | 関連ユースケース |
|--------|--------|--------|----------|------|------------------|
| INF-EF-22 | 通知設定 | 通知設定 | リソース | 通知設定の情報 | UC-EF-068 |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
