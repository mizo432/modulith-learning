# UC複合図

成果適応業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-06 成果適応業務領域のシステム境界レイヤーで扱うユースケース「レポートを出力する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Outcome Adaptation] UC Composite Diagram - ExportReport
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
  usecase "UC-LR-018 ExportReport" as UC
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- レポートを出力する
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
  usecase "UC-LR-018 レポートを出力する" as UC
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名     | 英語名       | 説明                                   | 関連アクター   |
|----------------|--------------------|--------------|----------------------------------------|----------------|
| UC-LR-018      | レポートを出力する | ExportReport | 権限と監査を考慮してレポートを出力する | ACT-02, ACT-05 |

## 5. 画面一覧

| 画面ID  | 画面名               | 英語名               | 説明       | 関連ユースケース |
|---------|----------------------|----------------------|------------|------------------|
| SCR-025 | プロダクトメトリクス | プロダクトメトリクス | 画面の説明 | UC-LR-018        |

## 6. イベント一覧

イベントなし

## 7. 情報一覧

情報なし

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
