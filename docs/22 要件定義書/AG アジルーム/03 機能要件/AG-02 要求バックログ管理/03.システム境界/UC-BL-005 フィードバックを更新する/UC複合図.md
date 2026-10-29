# UC複合図

要求バックログ管理業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-02 要求バックログ管理業務領域のシステム境界レイヤーで扱うユースケース「フィードバックを更新する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Backlog Management] UC Composite Diagram - UpdateFeedback
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

actor "Employee\n(ACT-01)" as ACT1
actor "Product Owner\n(ACT-02)" as ACT2

rectangle "Frontend" {
  usecase "SCR-010 Product Backlog" as SCR
}

rectangle "Backlog Management Module" {
  usecase "UC-BL-005 UpdateFeedback" as UC
}

rectangle "System Layer" {
  card "INF-BL-03\nFeedback" as INF03
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : Call
UC -- INF03 : Use
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- フィードバックを更新する
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

actor "従業員\n(ACT-01)" as ACT1
actor "プロダクトオーナー\n(ACT-02)" as ACT2

rectangle "フロントエンド" {
  usecase "SCR-010 Product Backlog" as SCR
}

rectangle "要求バックログ管理モジュール" {
  usecase "UC-BL-005 フィードバックを更新する" as UC
}

rectangle "システムレイヤー" {
  card "INF-BL-03\nフィードバック" as INF03
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : 呼び出し
UC -- INF03 : 使用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名           | 英語名         | 説明                                         | 関連アクター   |
|----------------|--------------------------|----------------|----------------------------------------------|----------------|
| UC-BL-005      | フィードバックを更新する | UpdateFeedback | フィードバックの対応状況と判断理由を追跡する | ACT-01, ACT-02 |

## 5. 画面一覧

| 画面ID  | 画面名          | 英語名          | 説明       | 関連ユースケース |
|---------|-----------------|-----------------|------------|------------------|
| SCR-010 | Product Backlog | Product Backlog | 画面の説明 | UC-BL-005        |

## 6. イベント一覧

| イベントID | イベント名             | 英語名               | 説明                                     | 関連ユースケース |
|------------|------------------------|----------------------|------------------------------------------|------------------|
| EVT-BL-004 | フィードバックが更新された | Feedback Updated  | フィードバックの対応状況と判断理由が追跡された | UC-BL-005      |

## 7. 情報一覧

| 情報ID  | 情報名     | 英語名     | 情報種類 | 説明                         | 関連ユースケース |
|---------|------------|------------|----------|------------------------------|------------------|
| INF-BL-03 | フィードバック | Feedback | リソース | 利用者からの意見・評価       | UC-BL-005        |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
