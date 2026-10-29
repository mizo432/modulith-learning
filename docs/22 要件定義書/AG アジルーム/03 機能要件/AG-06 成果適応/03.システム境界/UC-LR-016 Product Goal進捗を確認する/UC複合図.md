# UC複合図

成果適応業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-06 成果適応業務領域のシステム境界レイヤーで扱うユースケース「Product
Goal進捗を確認する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Outcome Adaptation] UC Composite Diagram - ConfirmProductGoalProgress
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

actor "Product Owner\n(ACT-02)" as ACT1
actor "Tracker\n(ACT-05)" as ACT2

rectangle "Frontend" {
  usecase "SCR-025 プロダクトメトリクス" as SCR
}

rectangle "Outcome Adaptation Module" {
  usecase "UC-LR-016 ConfirmProductGoalProgress" as UC
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- Product Goal進捗を確認する
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

actor "プロダクトオーナー\n(ACT-02)" as ACT1
actor "トラッキング担当\n(ACT-05)" as ACT2

rectangle "フロントエンド" {
  usecase "SCR-025 プロダクトメトリクス" as SCR
}

rectangle "成果適応モジュール" {
  usecase "UC-LR-016 Product Goal進捗を確認する" as UC
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名             | 英語名                     | 説明                                             | 関連アクター   |
|----------------|----------------------------|----------------------------|--------------------------------------------------|----------------|
| UC-LR-016      | Product Goal進捗を確認する | ConfirmProductGoalProgress | 成果指標とバックログ・リリースの関係を可視化する | ACT-02, ACT-05 |

## 5. 画面一覧

| 画面ID  | 画面名               | 英語名               | 説明       | 関連ユースケース |
|---------|----------------------|----------------------|------------|------------------|
| SCR-025 | プロダクトメトリクス | プロダクトメトリクス | 画面の説明 | UC-LR-016        |

## 6. イベント一覧

イベントなし

## 7. 情報一覧

| 情報ID     | 情報名       | 説明                                   | 操作種類 |
|------------|--------------|----------------------------------------|----------|
| INF-LR-05 | プロジェクト状況 | 進捗、マイルストーン、成果、リスク | 参照     |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
