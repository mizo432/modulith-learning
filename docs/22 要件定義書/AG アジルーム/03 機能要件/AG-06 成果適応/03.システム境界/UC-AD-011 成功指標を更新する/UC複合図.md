# UC複合図

成果適応業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-06 成果適応業務領域のシステム境界レイヤーで扱うユースケース「成功指標を更新する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Outcome Adaptation] UC Composite Diagram - UpdateSuccessMetric
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

rectangle "Frontend" {
  usecase "SCR-025 ProductMetrics" as SCR
}

rectangle "Outcome Adaptation Module" {
  usecase "UC-AD-011 UpdateSuccessMetric" as UC
}

ACT1 -- SCR
SCR ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- 成功指標を更新する
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

rectangle "フロントエンド" {
  usecase "SCR-025 プロダクトメトリクス" as SCR
}

rectangle "成果適応モジュール" {
  usecase "UC-AD-011 成功指標を更新する" as UC
}

ACT1 -- SCR
SCR ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名                 | 英語名                    | 説明                               | 関連アクター   |
|----------------|--------------------------------|---------------------------|------------------------------------|----------------|
| UC-AD-011      | 成功指標を更新する             | UpdateSuccessMetric       | 成功指標の目標値・測定方法を修正する | ACT-02 |

## 5. 画面一覧

| 画面ID  | 画面名               | 英語名              | 説明       | 関連ユースケース |
|---------|----------------------|---------------------|------------|------------------|
| SCR-025 | プロダクトメトリクス | ProductMetrics | 画面の説明 | UC-AD-011        |

## 6. イベント一覧

| イベントID | イベント名                       | 説明                                                         | 関連情報  |
|------------|----------------------------------|--------------------------------------------------------------|-----------|
| EVT-AD-008 | 成功指標が更新された             | 成功指標の目標値・測定方法が修正された                       | INF-AD-05 |

## 7. 情報一覧

| 情報ID     | 情報名            | 説明                                   | 操作種類 |
|------------|-------------------|----------------------------------------|----------|
| INF-AD-05 | 成功指標 | 目標値、測定方法、頻度、現状値           | 更新     |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
